package d.pintoptech.ten.Prefs;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by PINTOP TECHNOLOGIES LIMITED on 7/13/2018.
 */

public class UserInfo {
    private static final String PREF_NAME = "login_data";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_NAME = "name";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_ID = "id";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_PASSPORT = "passport";


    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Context ctx;

    public UserInfo(Context ctx){
        this.ctx = ctx;
        prefs = ctx.getSharedPreferences(PREF_NAME, ctx.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void setEmail(String email){
        editor.putString(KEY_EMAIL,email);
        editor.apply();
    }

    public void setPhone(String phone){
        editor.putString(KEY_PHONE,phone);
        editor.apply();
    }

    public void setId(String id){
        editor.putString(KEY_ID,id);
        editor.apply();
    }

    public void setName(String name){
        editor.putString(KEY_NAME,name);
        editor.apply();
    }

    public void setAddress(String address){
        editor.putString(KEY_ADDRESS,address);
        editor.apply();
    }

    public void setPassport(String passport){
        editor.putString(KEY_PASSPORT,passport);
        editor.apply();
    }

    public  String getKeyEmail(){
        return prefs.getString(KEY_EMAIL, "");
    }

    public  String getKeyId(){
        return prefs.getString(KEY_ID, "");
    }

    public  String getKeyPhone(){
        return prefs.getString(KEY_PHONE, "");
    }

    public  String getKeyName(){
        return prefs.getString(KEY_NAME, "");
    }

    public  String getKeyAddress(){
        return prefs.getString(KEY_ADDRESS, "");
    }

    public  String getKeyPassport(){
        return prefs.getString(KEY_PASSPORT, "");
    }


    public void clearData(){
        editor.clear();
        editor.commit();
    }

}
