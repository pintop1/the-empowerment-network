package d.pintoptech.ten;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Matrix;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import d.pintoptech.ten.Prefs.UserInfo;
import d.pintoptech.ten.Prefs.UserSession;
import d.pintoptech.ten.ui.AddAll;
import d.pintoptech.ten.ui.AddBeneficiaries;
import d.pintoptech.ten.ui.AddGroupFragment;
import d.pintoptech.ten.ui.AllBeneficiaries;
import d.pintoptech.ten.ui.BeneficiariesFragment;
import d.pintoptech.ten.ui.DashboardFragment;
import d.pintoptech.ten.ui.GroupsFragment;
import d.pintoptech.ten.ui.MyProfile;
import d.pintoptech.ten.ui.NotificationList;
import d.pintoptech.ten.ui.ShowBeneficiary;
import d.pintoptech.ten.ui.WalletFragment;
import d.pintoptech.ten.util.MembersRepo;
import de.hdodenhof.circleimageview.CircleImageView;
import io.customerly.Callback;
import io.customerly.Customerly;

public class Profile extends AppCompatActivity {

    private FragmentManager fragmentManager;

    private CircleImageView DP,DPGEN;
    private UserInfo userInfo;
    private UserSession userSession;
    String dTag;
    private FloatingActionButton FAB;
    ProgressDialog progressDialog;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_dashboard:
                    fragmentManager.beginTransaction().replace(R.id.content_frame, new DashboardFragment()).addToBackStack("landing").commit();
                    return true;
                case R.id.nav_beneficiaries:
                    fragmentManager.beginTransaction().replace(R.id.content_frame, new AllBeneficiaries()).addToBackStack("landing").commit();
                    return true;
                case R.id.nav_groups:
                    fragmentManager.beginTransaction().replace(R.id.content_frame, new GroupsFragment()).addToBackStack("landing").commit();
                    return true;
                case R.id.nav_wallet:
                    fragmentManager.beginTransaction().replace(R.id.content_frame, new WalletFragment()).addToBackStack("landing").commit();
                    return true;
                case R.id.nav_chat:
                    Customerly.openSupport(Profile.this);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile2);

        userInfo = new UserInfo(this);

        userSession = new UserSession(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        FAB = findViewById(R.id.fab);
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(Profile.this, FAB);
                popup.inflate(R.menu.add_menu);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.nav_add_beneficiaries:
                                fragmentManager.beginTransaction().replace(R.id.content_frame, new AddBeneficiaries()).addToBackStack("landing").commit();
                                return true;
                            case R.id.nav_add_groups:
                                fragmentManager.beginTransaction().replace(R.id.content_frame, new AddGroupFragment()).addToBackStack("landing").commit();
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popup.show();

            }
        });


        ImageView NOTIFY = findViewById(R.id.notification);
        NOTIFY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.content_frame, new NotificationList()).addToBackStack("landing").commit();
            }
        });

        DP = findViewById(R.id.avatar);
        DPGEN = findViewById(R.id.avatarGen);

        DP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.content_frame, new MyProfile()).addToBackStack("landing").commit();
            }
        });
        DPGEN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.content_frame, new MyProfile()).addToBackStack("landing").commit();
            }
        });
        String avatar = "https://www.theempowermentnetwork.ng/"+userInfo.getKeyPassport();
        //if(!avatar.isEmpty()) {
            Picasso.with(Profile.this).load(avatar)
                    .placeholder(R.drawable.unknown_person)
                    .error(R.drawable.unknown_person)
                    .into(DP);
            Picasso.with(Profile.this).load(avatar)
                    .placeholder(R.drawable.unknown_person)
                    .error(R.drawable.unknown_person)
                    .into(DPGEN);
        //}

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, new DashboardFragment(), "main").addToBackStack("landing").commit();


        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Customerly.registerUser(
                userInfo.getKeyEmail(),
                //"12345",                //OPTIONALLY you can pass the user ID or null
                userInfo.getKeyName()               //OPTIONALLY you can pass the user name or null
                //attributesMap,          //OPTIONALLY you can pass some custom attributes or null (See the *Attributes* section below for the map building)
                //companyMap,             //OPTIONALLY you can pass the user company informations or null (See the *Companies* section below for the map building)
                );

    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        // TODO Auto-generated method stub
        super.onAttachFragment(fragment);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}
