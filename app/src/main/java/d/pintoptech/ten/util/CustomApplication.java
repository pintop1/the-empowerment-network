package d.pintoptech.ten.util;

import android.app.Application;

import d.pintoptech.ten.BuildConfig;
import d.pintoptech.ten.R;
import d.pintoptech.ten.Utils;
import io.customerly.Customerly;

public class CustomApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //See step 3)
        Customerly.configure(this, Utils.CUSTOMERLY_APP_ID, this.getResources().getColor(R.color.colorAccent));
        Customerly.setVerboseLogging(BuildConfig.DEBUG);
    }
}
