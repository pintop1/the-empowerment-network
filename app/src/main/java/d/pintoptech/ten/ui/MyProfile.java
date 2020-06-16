package d.pintoptech.ten.ui;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import d.pintoptech.ten.MainActivityTwo;
import d.pintoptech.ten.Prefs.UserInfo;
import d.pintoptech.ten.Prefs.UserSession;
import d.pintoptech.ten.Profile;
import d.pintoptech.ten.R;
import d.pintoptech.ten.Utils;
import d.pintoptech.ten.VolleySingleton;
import d.pintoptech.ten.util.FragmentUtility;
import d.pintoptech.ten.util.MaterialSpinnerModelForBank;
import d.pintoptech.ten.util.SpinnerForCountries;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyProfile extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    SwipeRefreshLayout swipeRefreshLayout;

    View fragView;
    private FragmentManager fragmentManager;

    private UserInfo userInfo;
    private UserSession userSession;
    private ProgressDialog progressDialog;

    private CircularProgressButton LOGOUT;


    private TextView NAME,GROUPS,MEMBERS;
    private TextView EMAIL,PHONE,ADDRESS,BVN,BANK,BANK_NUMBER,GENDER,EMP,DOB,NATIONALITY,STATE_OF_ORIGIN,STATE_OF_RESIDENCE,LG,KIDS,MSTATUS;
    private CircleImageView AVATAR;


    public MyProfile() {
        // Required empty public constructor
    }


    @Override
    public void onRefresh() {
        getData();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragView = inflater.inflate(R.layout.fragment_my_profile, container, false);

        swipeRefreshLayout = getActivity().findViewById(R.id.container);
        swipeRefreshLayout.setOnRefreshListener(this);

        LOGOUT = fragView.findViewById(R.id.logout);

        fragmentManager = getActivity().getSupportFragmentManager();

        userInfo = new UserInfo(getActivity());
        userSession = new UserSession(getActivity());
        progressDialog = new ProgressDialog(getActivity());

        LOGOUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Are you sure you want to sign out of your account?");
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progressDialog.setMessage("signing out");
                        progressDialog.show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.hide();
                                Logout();
                            }
                        }, 4000);
                    }
                });
                builder.show();
            }
        });

        FragmentUtility.FragmentUtilityGen(getActivity(),fragmentManager,R.string.my_profile);

        getActivity().findViewById(R.id.avatarGen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        userInfo = new UserInfo(getActivity());

        AVATAR = fragView.findViewById(R.id.avatar);
        NAME = fragView.findViewById(R.id.name);
        GROUPS = fragView.findViewById(R.id.groups);
        MEMBERS = fragView.findViewById(R.id.members);
        //EMAIL = fragView.findViewById(R.id.email);
        PHONE = fragView.findViewById(R.id.phone);
        ADDRESS = fragView.findViewById(R.id.address);
        BVN = fragView.findViewById(R.id.bvn);
        BANK = fragView.findViewById(R.id.bank);
        BANK_NUMBER = fragView.findViewById(R.id.bank_number);
        GENDER = fragView.findViewById(R.id.gender);
        EMP = fragView.findViewById(R.id.emp);
        DOB = fragView.findViewById(R.id.dob);
        NATIONALITY = fragView.findViewById(R.id.nationality);
        STATE_OF_ORIGIN = fragView.findViewById(R.id.state_of_origin);
        STATE_OF_RESIDENCE = fragView.findViewById(R.id.state_of_residence);
        LG = fragView.findViewById(R.id.lg);
        KIDS = fragView.findViewById(R.id.kids);
        MSTATUS = fragView.findViewById(R.id.mstatus);
        getData();

        return fragView;
    }

    private void getData(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.GET_PROFILE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jobj = new JSONObject(response);

                            String id = jobj.getString("id");
                            String name = jobj.getString("name");
                            String email = jobj.getString("email");
                            String phone = jobj.getString("phone");
                            String bvn = jobj.getString("bvn");
                            String address = jobj.getString("address");
                            String identification = jobj.getString("identification");
                            String bank = jobj.getString("bank");
                            String bank_number = jobj.getString("bank_number");
                            String gender = jobj.getString("gender");
                            String passport = jobj.getString("passport");
                            String occupation = jobj.getString("occupation");
                            String date_of_birth = jobj.getString("date_of_birth");
                            String nationality = jobj.getString("nationality");
                            String state_of_origin = jobj.getString("state_of_origin");
                            String state_of_residence = jobj.getString("state_of_residence");
                            String local_government = jobj.getString("local_government");
                            String number_of_kids = jobj.getString("number_of_kids");
                            String marital_status = jobj.getString("marital_status");
                            String biometrics = jobj.getString("biometrics");
                            String status = jobj.getString("status");
                            String date_joined = jobj.getString("date_joined");
                            String beneficiaries = jobj.getString("beneficiaries");
                            String groups = jobj.getString("groups");

                            Picasso.with(getActivity()).load("https://www.theempowermentnetwork.ng/"+passport)
                                    .placeholder(R.drawable.unknown_person)
                                    .error(R.drawable.unknown_person)
                                    .into(AVATAR);

                            NAME.setText(name);
                            GROUPS.setText(groups+" groups");
                            MEMBERS.setText(beneficiaries+" beneficiaries");
                            //EMAIL.setText(email);
                            //PHONE.setText(phone);
                            ADDRESS.setText(address);
                            BVN.setText(bvn);
                            BANK.setText(bank);
                            BANK_NUMBER.setText(bank_number);
                            GENDER.setText(gender);
                            //EMP.setText(occupation);
                            DOB.setText(date_of_birth);
                            NATIONALITY.setText(nationality);
                            STATE_OF_ORIGIN.setText(state_of_origin);
                            STATE_OF_RESIDENCE.setText(state_of_residence);
                            LG.setText(local_government);
                            KIDS.setText(number_of_kids+" kids");
                            MSTATUS.setText(marital_status);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Please ensure you have a working internet!");
                        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", userInfo.getKeyEmail());

                return params;
            }
        };

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    private void Logout(){
        userInfo.clearData();
        userSession.setLoggedIn(false);
        startActivity(new Intent(getActivity(), MainActivityTwo.class));
        getActivity().finish();
    }

}
