package d.pintoptech.ten;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import d.pintoptech.ten.Prefs.UserInfo;

public class ResetPasswordActivity extends AppCompatActivity {

    private CircularProgressButton NEXT;
    private EditText PASSWORD,CPASSWORD;
    private UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        userInfo = new UserInfo(this);

        PASSWORD = findViewById(R.id.password);
        CPASSWORD = findViewById(R.id.cpassword);


        NEXT = findViewById(R.id.next);

        NEXT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    public void startAnimation(CircularProgressButton btn){
        PASSWORD.setFocusable(false);
        PASSWORD.setFocusableInTouchMode(false);
        PASSWORD.setClickable(false);

        CPASSWORD.setFocusable(false);
        CPASSWORD.setFocusableInTouchMode(false);
        CPASSWORD.setClickable(false);
        btn.startAnimation();
    }

    public void endAnimation(CircularProgressButton btn){
        PASSWORD.setFocusable(true);
        PASSWORD.setFocusableInTouchMode(true);
        PASSWORD.setClickable(true);

        CPASSWORD.setFocusable(true);
        CPASSWORD.setFocusableInTouchMode(true);
        CPASSWORD.setClickable(true);
        btn.revertAnimation();
    }

    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

    private void register(){
        final String password = PASSWORD.getText().toString().trim();
        final String cpassword = CPASSWORD.getText().toString().trim();

        if (TextUtils.isEmpty(password)) {
            PASSWORD.setError("Please enter your password");
            PASSWORD.requestFocus();
            return;
        }else if(password.length()<8 &&!isValidPassword(password)){
            PASSWORD.setError("Invalid password format");
            PASSWORD.requestFocus();
            return;
        }else if (!cpassword.equals(password)) {
            CPASSWORD.setError("Passwords does not match");
            CPASSWORD.requestFocus();
            return;
        }

        startAnimation(NEXT);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.RESET,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        endAnimation(NEXT);

                        if(response.equalsIgnoreCase("success")){
                            startAnimation(NEXT);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ResetPasswordActivity.this);
                                    builder.setMessage("Password reset successful");
                                    builder.setNegativeButton("Login Now", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                            startActivity(new Intent(ResetPasswordActivity.this, LoginActivity.class));
                                            finish();
                                        }
                                    });
                                }
                            }, 4000);
                        }else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ResetPasswordActivity.this);
                            builder.setMessage("Unknown server error!");
                            builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            builder.setPositiveButton("Login", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(ResetPasswordActivity.this,LoginActivity.class));
                                }
                            });
                            builder.show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ResetPasswordActivity.this);
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
                params.put("phone", userInfo.getKeyPhone().substring(1,11));
                params.put("password", password);

                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
