package d.pintoptech.ten;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import d.pintoptech.ten.Prefs.PinPref;
import d.pintoptech.ten.util.PinEntryEditText;

public class CreatePinScreen extends AppCompatActivity {

    private PinPref pinPref;

    private PinEntryEditText PIN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pin_screen);

        pinPref = new PinPref(this);

        PIN = findViewById(R.id.pin);

        PIN.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length() == 6) {
                    String code = PIN.getText().toString().trim();
                    pinPref.setKeyLoginType("PIN");
                    pinPref.setKeyPin(code);
                    startActivity(new Intent(CreatePinScreen.this,ConfirmPinScreen.class));
                }
            }
        });
    }
}
