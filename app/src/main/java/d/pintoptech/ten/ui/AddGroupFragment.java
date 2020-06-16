package d.pintoptech.ten.ui;


import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import d.pintoptech.ten.Prefs.UserInfo;
import d.pintoptech.ten.Profile;
import d.pintoptech.ten.R;
import d.pintoptech.ten.Utils;
import d.pintoptech.ten.VolleySingleton;
import d.pintoptech.ten.util.FragmentUtility;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddGroupFragment extends Fragment {

    View fragView;
    private FragmentManager fragmentManager;
    private CircularProgressButton circularProgressButton;
    private EditText DESC;
    private UserInfo userInfo;


    public AddGroupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragView = inflater.inflate(R.layout.fragment_add_group, container, false);

        fragmentManager = getActivity().getSupportFragmentManager();
        FragmentUtility.FragmentUtilityAdd(getActivity(),fragmentManager,R.string.add_new_group);
        userInfo = new UserInfo(getActivity());
        circularProgressButton = fragView.findViewById(R.id.submit);
        DESC = fragView.findViewById(R.id.description);
        getActivity().findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.popBackStack();
            }
        });
        circularProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addGroup();
            }
        });
        return fragView;
    }

    private void addGroup(){
        final String email_address = userInfo.getKeyEmail();
        final String description = DESC.getText().toString().trim();

        circularProgressButton.startAnimation();
        DESC.setFocusable(false);
        DESC.setClickable(false);
        DESC.setFocusableInTouchMode(false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.CREATE_GROUP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        circularProgressButton.revertAnimation();
                        DESC.setFocusable(true);
                        DESC.setClickable(true);
                        DESC.setFocusableInTouchMode(true);

                        if(response.equalsIgnoreCase("success")){
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setMessage(getResources().getString(R.string.groupCreated));
                            builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    theToolbarTwo();
                                    fragmentManager.popBackStack();
                                }
                            });
                            builder.show();

                        }else if(response.equalsIgnoreCase("error")) {
                            String errorMsg = "Please fill up the <b>current </b> group to create more group.<br><br>Each group requires <b><i>10 beneficiaries</i></b> max.<br>";
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setMessage(Html.fromHtml(errorMsg));
                            builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            builder.show();
                        }else {
                            //Log.e("TAG", response);
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
                        circularProgressButton.revertAnimation();
                        DESC.setFocusable(true);
                        DESC.setClickable(true);
                        DESC.setFocusableInTouchMode(true);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email_address);
                params.put("desc", description);

                return params;
            }
        };

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }



    public void theToolbarTwo(){
        changeStatusBarTwo();
        getActivity().findViewById(R.id.toolbar).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.nextToolbar).setVisibility(View.GONE);
    }

    public void changeStatusBarTwo(){
        Window window = getActivity().getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
    }

}
