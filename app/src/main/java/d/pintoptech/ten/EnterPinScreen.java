package d.pintoptech.ten;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import d.pintoptech.ten.Prefs.PinPref;
import d.pintoptech.ten.util.PinEntryEditText;

public class EnterPinScreen extends AppCompatActivity {

    private PinPref pinPref;

    private PinEntryEditText PIN;
    private TextView STEAD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_pin_screen);

        pinPref = new PinPref(this);
        STEAD = findViewById(R.id.stead);
        PIN = findViewById(R.id.pin);

        STEAD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EnterPinScreen.this, FingerPrintActivity.class));
                finish();
            }
        });

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
                        AlertDialog.Builder builder = new AlertDialog.Builder(EnterPinScreen.this);
                        builder.setMessage("Invalid pin supplied!");
                        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                    }else {
                        startActivity(new Intent(EnterPinScreen.this,Profile.class));
                        finish();
                    }

                }
            }
        });
    }
}
