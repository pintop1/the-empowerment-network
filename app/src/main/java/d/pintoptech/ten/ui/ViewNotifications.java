package d.pintoptech.ten.ui;


import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

import d.pintoptech.ten.Profile;
import d.pintoptech.ten.R;
import d.pintoptech.ten.Utils;
import d.pintoptech.ten.VolleySingleton;
import d.pintoptech.ten.util.FragmentUtility;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewNotifications extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    View fragView;
    private FragmentManager fragmentManager;
    String id;

    private CircleImageView ICON;
    private TextView MSG,DESC,TIME;


    SwipeRefreshLayout swipeRefreshLayout;


    public ViewNotifications() {
        // Required empty public constructor
    }


    @Override
    public void onRefresh() {
        getData();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragView = inflater.inflate(R.layout.fragment_view_notifications, container, false);

        fragmentManager = getActivity().getSupportFragmentManager();
        //FragmentUtility.FragmentUtilityGen(getActivity(),fragmentManager,R.string.notifications);


        swipeRefreshLayout = getActivity().findViewById(R.id.container);
        swipeRefreshLayout.setOnRefreshListener(this);

        ICON = fragView.findViewById(R.id.icon);
        MSG = fragView.findViewById(R.id.msg);
        DESC = fragView.findViewById(R.id.desc);
        TIME = fragView.findViewById(R.id.time);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            id = bundle.getString("id", "");
        }

        getData();

        return fragView;
    }

    private void getData(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.CURRENT_NOTIFICATION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jobj = new JSONObject(response);

                            String msg = jobj.getString("msg");
                            String icon = jobj.getString("icon");
                            String desc = jobj.getString("desc");
                            String time = jobj.getString("time");
                            String type = jobj.getString("type");

                            MSG.setText(msg);
                            Picasso.with(getActivity()).load(icon)
                                    .placeholder(R.drawable.unknown_person)
                                    .error(R.drawable.unknown_person)
                                    .into(ICON);
                            DESC.setText(desc);
                            TIME.setText(time);

                            if(type.equalsIgnoreCase("activity")){
                                FragmentUtility.FragmentUtilityGen(getActivity(),fragmentManager,R.string.activity);
                                DESC.setVisibility(View.GONE);
                            }else {
                                FragmentUtility.FragmentUtilityGen(getActivity(),fragmentManager,R.string.notifications);
                                DESC.setVisibility(View.VISIBLE);
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Please ensure you have a working internet!");
                        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);

                return params;
            }
        };

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

}
