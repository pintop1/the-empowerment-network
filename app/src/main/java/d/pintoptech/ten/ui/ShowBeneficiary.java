package d.pintoptech.ten.ui;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import d.pintoptech.ten.R;
import d.pintoptech.ten.Utils;
import d.pintoptech.ten.VolleySingleton;
import d.pintoptech.ten.util.FragmentUtility;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowBeneficiary extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private View fragView;
    private TextView NAME,ROLE,LEVEL,BVN,ADDRESS,BANK_NAME,BANK_NUMBER,GENDER,DOB,NATIONALITY,STATEORIGIN,STATERESIDENCE,LG,KIDS,MSTATUS,GROUP,DATE_ADDED;
    private ImageView PHONE,EMAIL;
    private String id, phone, email, name;
    private CircleImageView AVATAR;
    private FragmentManager fragmentManager;

    SwipeRefreshLayout swipeRefreshLayout;

    private ShimmerFrameLayout mShimmerViewContainer;
    private LinearLayout MAIN;


    public ShowBeneficiary() {
        // Required empty public constructor
    }


    @Override
    public void onRefresh() {
        getData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragView = inflater.inflate(R.layout.fragment_show_beneficiary, container, false);
        bindData();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            id = bundle.getString("id", "");
        }

        swipeRefreshLayout = getActivity().findViewById(R.id.container);
        swipeRefreshLayout.setOnRefreshListener(this);

        mShimmerViewContainer = fragView.findViewById(R.id.shimmer_view_container);
        MAIN = fragView.findViewById(R.id.main);

        fragmentManager = getActivity().getSupportFragmentManager();
        FragmentUtility.FragmentUtilityGen(getActivity(),fragmentManager,R.string.beneficiary);
        getData();
        PHONE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT > 22) {

                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 101);

                        return;
                    }
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + phone));
                    startActivity(callIntent);
                } else {

                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + phone));
                    startActivity(callIntent);
                }
            }
        });
        EMAIL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailintent = new Intent(android.content.Intent.ACTION_SEND);
                emailintent.setType("plain/text");
                emailintent.putExtra(android.content.Intent.EXTRA_EMAIL,new String[] {email });
                emailintent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
                emailintent.putExtra(android.content.Intent.EXTRA_TEXT,"");
                startActivity(Intent.createChooser(emailintent, "Hi, " +name));
            }
        });
        return fragView;
    }

    private void bindData(){
        NAME = fragView.findViewById(R.id.name);
        ROLE = fragView.findViewById(R.id.role);
        LEVEL = fragView.findViewById(R.id.level);
        BVN = fragView.findViewById(R.id.bvn);
        ADDRESS = fragView.findViewById(R.id.address);
        BANK_NAME = fragView.findViewById(R.id.bank_name);
        BANK_NUMBER = fragView.findViewById(R.id.bank_number);
        GENDER = fragView.findViewById(R.id.gender);
        DOB = fragView.findViewById(R.id.dob);
        NATIONALITY = fragView.findViewById(R.id.nationality);
        STATEORIGIN = fragView.findViewById(R.id.state_of_origin);
        STATERESIDENCE = fragView.findViewById(R.id.state_of_residence);
        LG = fragView.findViewById(R.id.lg);
        KIDS = fragView.findViewById(R.id.kids);
        MSTATUS = fragView.findViewById(R.id.mstatus);
        GROUP = fragView.findViewById(R.id.group);
        DATE_ADDED = fragView.findViewById(R.id.date_added);
        AVATAR = fragView.findViewById(R.id.avatar);
        PHONE = fragView.findViewById(R.id.phone);
        EMAIL = fragView.findViewById(R.id.email);
    }

    private void getData(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.GET_BENEFICIARY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                            try {
                                JSONObject jobj = new JSONObject(response);


                                name = jobj.getString("name");
                                String role = jobj.getString("role");
                                String level = "Level"+jobj.getString("level");
                                String bvn = jobj.getString("bvn");
                                email = jobj.getString("email");
                                phone = jobj.getString("phone");
                                String address = jobj.getString("address");
                                String bank_name = jobj.getString("bank_name");
                                String bank_number = jobj.getString("bank_number");
                                String gender = jobj.getString("gender");
                                String dob = jobj.getString("dob");
                                String nationality = jobj.getString("nationality");
                                String state_of_origin = jobj.getString("state_of_origin");
                                String state_of_residence = jobj.getString("state_of_residence");
                                String lg = jobj.getString("lg");
                                String kids = jobj.getString("kids");
                                String mstatus = jobj.getString("mstatus");
                                String group = jobj.getString("group");
                                String date_added = jobj.getString("date_added");
                                String avatar = "https://www.theempowermentnetwork.ng/"+jobj.getString("avatar");

                                NAME.setText(name);
                                ROLE.setText(role);
                                LEVEL.setText(level);
                                BVN.setText(bvn);
                                ADDRESS.setText(address);
                                BANK_NAME.setText(bank_name);
                                BANK_NUMBER.setText(bank_number);
                                GENDER.setText(gender);
                                DOB.setText(dob);
                                NATIONALITY.setText(nationality);
                                STATEORIGIN.setText(state_of_origin);
                                STATERESIDENCE.setText(state_of_residence);
                                LG.setText(lg);
                                KIDS.setText(kids);
                                MSTATUS.setText(mstatus);
                                GROUP.setText(group);
                                DATE_ADDED.setText(date_added);


                                Picasso.with(getActivity()).load(avatar).fit().centerCrop()
                                        .placeholder(R.drawable.unknown_person)
                                        .error(R.drawable.unknown_person)
                                        .into(AVATAR);

                                MAIN.setVisibility(View.VISIBLE);
                                mShimmerViewContainer.setVisibility(View.GONE);
                                swipeRefreshLayout.setRefreshing(false);


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
                params.put("id", id);

                return params;
            }
        };

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

}
