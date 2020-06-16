package d.pintoptech.ten;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import d.pintoptech.ten.Prefs.UserInfo;

public class ForgetPasswordActivity extends AppCompatActivity {

    private CircularProgressButton NEXT;
    private EditText PHONE;
    private UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        userInfo = new UserInfo(this);

        PHONE = findViewById(R.id.phone);

        NEXT = findViewById(R.id.next);

        NEXT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    public void startAnimation(CircularProgressButton btn){

        PHONE.setFocusable(false);
        PHONE.setFocusableInTouchMode(false);
        PHONE.setClickable(false);
        btn.startAnimation();
    }

    public void endAnimation(CircularProgressButton btn){

        PHONE.setFocusable(true);
        PHONE.setFocusableInTouchMode(true);
        PHONE.setClickable(true);
        btn.revertAnimation();
    }

    private void register(){
        final String phone = PHONE.getText().toString().trim();

        if (TextUtils.isEmpty(phone)) {
            PHONE.setError("Please enter your phone number");
            PHONE.requestFocus();
            return;
        }

        userInfo.setPhone(phone);

        startAnimation(NEXT);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.FORGET_PASSWORD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        endAnimation(NEXT);
                        startActivity(new Intent(ForgetPasswordActivity.this,ResetVerification.class));
                        finish();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        startActivity(new Intent(ForgetPasswordActivity.this,ResetVerification.class));
                        finish();
                        endAnimation(NEXT);
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
