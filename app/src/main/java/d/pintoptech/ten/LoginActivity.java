package d.pintoptech.ten;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import d.pintoptech.ten.Prefs.PinPref;
import d.pintoptech.ten.Prefs.UserInfo;
import d.pintoptech.ten.Prefs.UserSession;

public class LoginActivity extends AppCompatActivity {

    private EditText EMAIL,PASSWORD;
    //private CheckBox REMEMBER;
    private CircularProgressButton LOGIN;
    private UserSession userSession;
    private UserInfo userInfo;
    private TextView RESET, REGISTER;
    private PinPref pinPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        EMAIL = findViewById(R.id.email);
        PASSWORD = findViewById(R.id.password);
        //REMEMBER = findViewById(R.id.remember);
        REGISTER = findViewById(R.id.register);
        LOGIN = findViewById(R.id.login);
        pinPref = new PinPref(this);

        RESET = findViewById(R.id.reset);
        RESET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,ForgetPasswordActivity.class));
            }
        });

        userInfo = new UserInfo(this);
        userSession = new UserSession(this);

        REGISTER.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                finish();
            }
        });

        LOGIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });


    }



    private void login(){
        final String email_address = EMAIL.getText().toString().trim();
        final String password = PASSWORD.getText().toString().trim();

        if (TextUtils.isEmpty(email_address)) {
            EMAIL.setError("Please enter your email");
            EMAIL.requestFocus();
            return;
        }else if (TextUtils.isEmpty(password)) {
            PASSWORD.setError("Please enter your password");
            PASSWORD.requestFocus();
            return;
        }else if (!Patterns.EMAIL_ADDRESS.matcher(email_address).matches()) {
            EMAIL.setError("Enter a valid email");
            EMAIL.requestFocus();
            return;
        }

        startAnimation(LOGIN);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        endAnimation(LOGIN);

                            startAnimation(LOGIN);
                            Log.e("ERROR TAG: ", response);

                            try {
                                JSONObject jobj = new JSONObject(response);


                                String name = jobj.getString("name");
                                String email = jobj.getString("email");
                                String phone = jobj.getString("phone");
                                String address = jobj.getString("address");

                                userInfo.setAddress(address);
                                userInfo.setPhone(phone);
                                userInfo.setName(name);
                                userInfo.setEmail(email);

                                //if(REMEMBER.isChecked()){
                                    userSession.setLoggedIn(true);
                                //}

                                checkStatus(email);

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e("TAG", response);
                            }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        builder.setMessage("Please ensure you have a working internet!");
                        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                        endAnimation(LOGIN);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("password", password);
                params.put("email", email_address);

                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void checkStatus(final String email_address){

        startAnimation(LOGIN);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.GET_REG_STATUS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        endAnimation(LOGIN);

                        if(response.equalsIgnoreCase("completed")){
                            if(pinPref.getKeyLoginType().equals("FINGERPRINT")){
                                startActivity(new Intent(LoginActivity.this,FingerPrintActivity.class));
                                finish();
                            }else if(pinPref.getKeyLoginType().equals("PIN")){
                                startActivity(new Intent(LoginActivity.this,EnterPinScreen.class));
                                finish();
                            }else {
                                startActivity(new Intent(LoginActivity.this,CreatePinScreen.class));
                                finish();
                            }
                        }else {
                            startActivity(new Intent(LoginActivity.this,CompleteProfileActivity.class));
                            finish();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        endAnimation(LOGIN);
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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
                params.put("email", email_address);

                return params;
            }
        };

        VolleySingleton.getInstance(LoginActivity.this).addToRequestQueue(stringRequest);
    }

    public void endAnimation(CircularProgressButton btn){
        EMAIL.setFocusable(true);
        EMAIL.setFocusableInTouchMode(true);
        EMAIL.setClickable(true);

        PASSWORD.setFocusable(true);
        PASSWORD.setFocusableInTouchMode(true);
        PASSWORD.setClickable(true);

        //REMEMBER.setFocusable(true);
        //REMEMBER.setFocusableInTouchMode(true);
        //REMEMBER.setClickable(true);
        btn.revertAnimation();
    }

    public void startAnimation(CircularProgressButton btn){
        EMAIL.setFocusable(false);
        EMAIL.setFocusableInTouchMode(false);
        EMAIL.setClickable(false);

        PASSWORD.setFocusable(false);
        PASSWORD.setFocusableInTouchMode(false);
        PASSWORD.setClickable(false);

        //REMEMBER.setFocusable(false);
        //REMEMBER.setFocusableInTouchMode(false);
        //REMEMBER.setClickable(false);
        btn.startAnimation();
    }


}