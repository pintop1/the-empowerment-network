package d.pintoptech.ten;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import d.pintoptech.ten.Prefs.UserInfo;

public class RegisterFinish extends AppCompatActivity {

    CircularProgressButton LOGIN;
    UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_finish);

        userInfo = new UserInfo(this);

        LOGIN = findViewById(R.id.login);
        LOGIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterFinish.this, LoginActivity.class));
                finish();
            }
        });
    }
}
