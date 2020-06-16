package d.pintoptech.ten;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import d.pintoptech.ten.Prefs.PinPref;
import d.pintoptech.ten.util.PinEntryEditText;

public class ConfirmPinScreen extends AppCompatActivity {

    private PinPref pinPref;

    private PinEntryEditText PIN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_pin_screen);

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
                    String old = pinPref.getKeyPin();
                    pinPref.setKeyLoginType("PIN");
                    if(!old.equals(code)){
                        AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmPinScreen.this);
                        builder.setMessage("Pin does not match");
                        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                    }else {
                        startActivity(new Intent(ConfirmPinScreen.this,Profile.class));
                        finish();
                    }

                }
            }
        });
    }
}
