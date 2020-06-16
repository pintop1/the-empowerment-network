package d.pintoptech.ten.util;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import d.pintoptech.ten.R;

public class FragmentUtility {

    public static void FragmentUtilityAdd(FragmentActivity activity, final FragmentManager fragmentManager, int title){
        changeStatusBar(activity);
        activity.findViewById(R.id.genToolbar).setVisibility(View.GONE);
        activity.findViewById(R.id.toolbar).setVisibility(View.GONE);
        activity.findViewById(R.id.nextToolbar).setVisibility(View.VISIBLE);
        //fragmentManager = activity.getSupportFragmentManager();
        activity.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.popBackStack();
            }
        });
        TextView TITLE = activity.findViewById(R.id.titleTwo);
        TITLE.setTextColor(ContextCompat.getColor(activity, R.color.whiteCardColor));
        TITLE.setText(title);
    }

    private static void changeStatusBar(FragmentActivity activity){
        Window window = activity.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(activity.getResources().getColor(R.color.colorPrimary));
    }

    public static void FragmentUtilityMain(FragmentActivity activity, int title) {
        changeStatusBarTwo(activity);
        activity.findViewById(R.id.toolbar).setVisibility(View.VISIBLE);
        activity.findViewById(R.id.nextToolbar).setVisibility(View.GONE);
        activity.findViewById(R.id.genToolbar).setVisibility(View.GONE);
        TextView TITLE = activity.findViewById(R.id.title);
        TITLE.setText(title);
    }

    public static void FragmentUtilityGen(FragmentActivity activity, final FragmentManager fragmentManager, int title){
        changeStatusBarTwo(activity);
        activity.findViewById(R.id.genToolbar).setVisibility(View.VISIBLE);
        activity.findViewById(R.id.nextToolbar).setVisibility(View.GONE);
        activity.findViewById(R.id.toolbar).setVisibility(View.GONE);
        activity.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.popBackStack();
            }
        });
        TextView TITLE = activity.findViewById(R.id.theTitle);
        TITLE.setText(title);
    }

    public static void FragmentUtilityGenTwo(FragmentActivity activity, final FragmentManager fragmentManager, String title){
        changeStatusBarTwo(activity);
        activity.findViewById(R.id.genToolbar).setVisibility(View.VISIBLE);
        activity.findViewById(R.id.nextToolbar).setVisibility(View.GONE);
        activity.findViewById(R.id.toolbar).setVisibility(View.GONE);
        activity.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.popBackStack();
            }
        });
        TextView TITLE = activity.findViewById(R.id.theTitle);
        TITLE.setText(title);
    }

    public static void FragmentUtilityWallet(FragmentActivity activity){
        activity.findViewById(R.id.genToolbar).setVisibility(View.GONE);
        activity.findViewById(R.id.toolbar).setVisibility(View.GONE);
        activity.findViewById(R.id.nextToolbar).setVisibility(View.GONE);
    }

    private static void changeStatusBarTwo(FragmentActivity activity){
        Window window = activity.getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        window.setStatusBarColor(activity.getResources().getColor(R.color.colorPrimaryDark));
    }
}
