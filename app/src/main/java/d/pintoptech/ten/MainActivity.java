package d.pintoptech.ten;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

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

import d.pintoptech.ten.Prefs.PinPref;
import d.pintoptech.ten.Prefs.UserInfo;
import d.pintoptech.ten.Prefs.UserSession;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 1000;
    private UserSession userSession;
    private UserInfo userInfo;
    private PinPref pinPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userSession = new UserSession(this);

        userInfo = new UserInfo(this);
        pinPref = new PinPref(this);

        fullscreen();


        if(userSession.isUserLoggedIn()){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getData();
                }
            }, SPLASH_TIME_OUT);
        }else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    redirect();
                }
            }, SPLASH_TIME_OUT);
        }
    }

    private void redirect(){
        startActivity(new Intent(MainActivity.this,MainActivityTwo.class));
            finish();
    }

    private void redirectTwo(){
        startActivity(new Intent(MainActivity.this,FingerPrintActivity.class));
        finish();
    }

    private void redirectThree(){
        startActivity(new Intent(MainActivity.this,CompleteProfileActivity.class));
        finish();
    }

    private void redirectFour(){
        startActivity(new Intent(MainActivity.this,EnterPinScreen.class));
        finish();
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

    public void fullscreen(){
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void getData(){
        final String email_address = userInfo.getKeyEmail();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.GET_REG_STATUS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(response.equalsIgnoreCase("completed")){
                            if(pinPref.getKeyLoginType().equals("FINGERPRINT")){
                                redirectTwo();
                            }else if(pinPref.getKeyLoginType().equals("PIN")){
                                redirectFour();
                            }
                        }else {
                            redirectThree();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("Please ensure you have a working internet!");
                        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                finish();
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

        VolleySingleton.getInstance(MainActivity.this).addToRequestQueue(stringRequest);
    }
}