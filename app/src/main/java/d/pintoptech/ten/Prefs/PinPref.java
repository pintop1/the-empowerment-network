package d.pintoptech.ten.Prefs;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by PINTOP TECHNOLOGIES LIMITED on 7/13/2018.
 */

public class PinPref {
    private static final String PREF_NAME = "login_data";
    private static final String KEY_LOGIN_TYPE = "type";
    private static final String KEY_PIN = "pin";


    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Context ctx;

    public PinPref(Context ctx){
        this.ctx = ctx;
        prefs = ctx.getSharedPreferences(PREF_NAME, ctx.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void setKeyLoginType(String type){
        editor.putString(KEY_LOGIN_TYPE,type);
        editor.apply();
    }

    public void setKeyPin(String pin){
        editor.putString(KEY_PIN,pin);
        editor.apply();
    }

    public  String getKeyLoginType(){
        return prefs.getString(KEY_LOGIN_TYPE, "");
    }

    public  String getKeyPin(){
        return prefs.getString(KEY_PIN, "");
    }

    public void clearData(){
        editor.clear();
        editor.commit();
    }

}