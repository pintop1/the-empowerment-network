package d.pintoptech.ten;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import d.pintoptech.ten.Prefs.UserInfo;
import d.pintoptech.ten.util.PinEntryEditText;

public class ResetVerification extends AppCompatActivity {

    private CircularProgressButton SUBMIT;
    private TextView RETRY;
    private PinEntryEditText PIN;

    private UserInfo userInfo;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_verification);

        userInfo = new UserInfo(this);

        PIN = findViewById(R.id.pin);
        RETRY = findViewById(R.id.retry);
        SUBMIT = findViewById(R.id.submit);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);



        RETRY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resend();
            }
        });

        SUBMIT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = PIN.getText().toString().trim();
                verify(code);
            }
        });
    }

    private void verify(final String code){


        progressDialog.setMessage("verifying pin");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.CONFIRM_CODE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.hide();

                        if(response.equalsIgnoreCase("success")){
                            startActivity(new Intent(ResetVerification.this, ResetPasswordActivity.class));
                            finish();
                        }else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ResetVerification.this);
                            builder.setMessage("Error occurred while verifying your code.");
                            builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            builder.show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ResetVerification.this);
                        builder.setMessage("Please ensure you have a working internet!");
                        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                        progressDialog.hide();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("phone", userInfo.getKeyPhone().substring(1,11));
                params.put("code", code);

                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void startAnimation(){
        PIN.setFocusable(false);
        PIN.setFocusableInTouchMode(false);
        PIN.setClickable(false);
    }

    public void endAnimation(){
        PIN.setFocusable(true);
        PIN.setFocusableInTouchMode(true);
        PIN.setClickable(true);
    }

    private void resend(){
        final String phone = userInfo.getKeyPhone();

        progressDialog.setMessage("resending pin");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.FORGET_PASSWORD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.hide();

                        if(response.equalsIgnoreCase("error")){

                        }else {
                            Log.e("TAG", response+ " the number= "+phone.substring(1,11));
                            String errorMsg = "User with <b>phone number</b> could not be found!";
                            AlertDialog.Builder builder = new AlertDialog.Builder(ResetVerification.this);
                            builder.setMessage(Html.fromHtml(errorMsg));
                            builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            builder.show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ResetVerification.this);
                        builder.setMessage("Please ensure you have a working internet!");
                        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                        progressDialog.hide();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("phone", phone.substring(1,11));

                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }


}
