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

public class RegisterFour extends AppCompatActivity {

    private EditText PASSWORD,CPASSWORD;
    private CircularProgressButton FINISH;
    private UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_four);

        userInfo = new UserInfo(this);

        PASSWORD = findViewById(R.id.password);
        CPASSWORD = findViewById(R.id.confirm_password);

        FINISH = findViewById(R.id.finsih);

        FINISH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });


    }

    private void register(){
        final String password = PASSWORD.getText().toString().trim();
        final String cpassword = CPASSWORD.getText().toString().trim();
        final String email_address = userInfo.getKeyEmail();
        final String name = userInfo.getKeyName();
        final String phone = userInfo.getKeyPhone();
        final String address = userInfo.getKeyAddress();


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

        startAnimation();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.REGISTER_THREE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        endAnimation();

                        if(response.equalsIgnoreCase("success")){
                            startAnimation();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(new Intent(RegisterFour.this,RegisterFinish.class));
                                    finish();
                                }
                            }, 4000);
                        }else {
                            String errorMsg = response;
                            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterFour.this);
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
                                    startActivity(new Intent(RegisterFour.this,LoginActivity.class));
                                }
                            });
                            builder.show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        endAnimation();
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterFour.this);
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
                params.put("name", name);
                params.put("email", email_address);
                params.put("password", password);
                params.put("address", address);
                params.put("phone", phone);

                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

    public void startAnimation(){
        PASSWORD.setFocusable(false);
        PASSWORD.setFocusableInTouchMode(false);
        PASSWORD.setClickable(false);

        CPASSWORD.setFocusable(false);
        CPASSWORD.setFocusableInTouchMode(false);
        CPASSWORD.setClickable(false);
        FINISH.startAnimation();
    }

    public void endAnimation(){
        PASSWORD.setFocusable(true);
        PASSWORD.setFocusableInTouchMode(true);
        PASSWORD.setClickable(true);

        CPASSWORD.setFocusable(true);
        CPASSWORD.setFocusableInTouchMode(true);
        CPASSWORD.setClickable(true);
        FINISH.revertAnimation();
    }
}
