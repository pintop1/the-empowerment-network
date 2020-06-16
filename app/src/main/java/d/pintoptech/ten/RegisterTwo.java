package d.pintoptech.ten;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import d.pintoptech.ten.Prefs.UserInfo;

public class RegisterTwo extends AppCompatActivity {

    private CircularProgressButton NEXT;
    private EditText ADDRESS,PHONE;
    private UserInfo userInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_two);

        userInfo = new UserInfo(this);

        ADDRESS = findViewById(R.id.address);
        PHONE = findViewById(R.id.phone);

        NEXT = findViewById(R.id.next);

        setValues();

        NEXT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });


    }

    public void startAnimation(CircularProgressButton btn){
        ADDRESS.setFocusable(false);
        ADDRESS.setFocusableInTouchMode(false);
        ADDRESS.setClickable(false);

        PHONE.setFocusable(false);
        PHONE.setFocusableInTouchMode(false);
        PHONE.setClickable(false);
        btn.startAnimation();
    }

    public void endAnimation(CircularProgressButton btn){
        ADDRESS.setFocusable(true);
        ADDRESS.setFocusableInTouchMode(true);
        ADDRESS.setClickable(true);

        PHONE.setFocusable(true);
        PHONE.setFocusableInTouchMode(true);
        PHONE.setClickable(true);
        btn.revertAnimation();
    }

    private void register(){
        final String phone = PHONE.getText().toString().trim();
        final String email_address = userInfo.getKeyEmail();
        final String address = ADDRESS.getText().toString().trim();

        if (TextUtils.isEmpty(phone)) {
            PHONE.setError("Please enter your phone number");
            PHONE.requestFocus();
            return;
        }else if (TextUtils.isEmpty(address)) {
            ADDRESS.setError("Please enter your house address");
            ADDRESS.requestFocus();
            return;
        }

        startAnimation(NEXT);
        sendToken(phone);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.REGISTER_TWO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        endAnimation(NEXT);

                        if(response.equalsIgnoreCase("error")){
                            startAnimation(NEXT);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    userInfo.setPhone(phone);
                                    userInfo.setAddress(address);
                                    startActivity(new Intent(RegisterTwo.this,RegisterThree.class));
                                    finish();
                                }
                            }, 4000);
                        }else {
                            String errorMsg = "User with <b>phone number</b> already exists!";
                            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterTwo.this);
                            builder.setMessage(Html.fromHtml(errorMsg));
                            builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            builder.setPositiveButton("Login", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(RegisterTwo.this,LoginActivity.class));
                                }
                            });
                            builder.show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterTwo.this);
                        builder.setMessage("Please ensure you have a working internet!");
                        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                        endAnimation(NEXT);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("phone", phone);
                params.put("address", address);
                params.put("email", email_address);

                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void setValues(){
        String dphone = userInfo.getKeyPhone();
        String daddress = userInfo.getKeyAddress();
        if(!dphone.isEmpty()){
            PHONE.setText(dphone);
        }
        if(!daddress.isEmpty()){
            ADDRESS.setText(daddress);
        }
    }

    private void sendToken(final String phone){


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.TOKEN_REG,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

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
