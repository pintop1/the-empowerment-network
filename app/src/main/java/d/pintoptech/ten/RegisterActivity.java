package d.pintoptech.ten;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import d.pintoptech.ten.Prefs.UserInfo;


public class RegisterActivity extends AppCompatActivity {

    private EditText NAME,EMAIL;
    CircularProgressButton STARTI;
    private UserInfo userInfo;
    private TextView ALREADY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //changeStatusBar();


        ALREADY = findViewById(R.id.already);
        NAME = findViewById(R.id.name);
        EMAIL = findViewById(R.id.email);
        STARTI = findViewById(R.id.start);
        userInfo = new UserInfo(this);

        ALREADY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userInfo.clearData();
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });

        setValues();

        STARTI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });


    }

    public void changeStatusBar(){
        Window window = this.getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorBlackLight));
    }

    private void register(){
        final String email_address = EMAIL.getText().toString().trim();
        final String name = NAME.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            NAME.setError("Please enter your name");
            NAME.requestFocus();
            return;
        }else if (TextUtils.isEmpty(email_address)) {
            EMAIL.setError("Please enter email");
            EMAIL.requestFocus();
            return;
        }else if (!Patterns.EMAIL_ADDRESS.matcher(email_address).matches()) {
            EMAIL.setError("Enter a valid email");
            EMAIL.requestFocus();
            return;
        }

        startAnimation();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.REGISTER_ONE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        endAnimation();

                        if(response.equalsIgnoreCase("success")){
                            startAnimation();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    userInfo.setEmail(email_address);
                                    userInfo.setName(name);
                                    startActivity(new Intent(RegisterActivity.this,RegisterTwo.class));
                                    finish();
                                }
                            }, 4000);
                        }else {
                            String errorMsg = "User with <b>email address</b> already exists!";
                            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
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
                                    startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                                }
                            });
                            builder.show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                        builder.setMessage("Please ensure you have a working internet!");
                        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                        endAnimation();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("email", email_address);

                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void startAnimation(){
        NAME.setFocusable(false);
        NAME.setFocusableInTouchMode(false);
        NAME.setClickable(false);

        EMAIL.setFocusable(false);
        EMAIL.setFocusableInTouchMode(false);
        EMAIL.setClickable(false);
        STARTI.startAnimation();
    }

    public void endAnimation(){
        NAME.setFocusable(true);
        NAME.setFocusableInTouchMode(true);
        NAME.setClickable(true);

        EMAIL.setFocusable(true);
        EMAIL.setFocusableInTouchMode(true);
        EMAIL.setClickable(true);
        STARTI.revertAnimation();
    }

    public void setValues(){
        String demail = userInfo.getKeyEmail();
        String dname = userInfo.getKeyName();
        if(!demail.isEmpty()){
            EMAIL.setText(demail);
        }
        if(!dname.isEmpty()){
            NAME.setText(dname);
        }
    }
}