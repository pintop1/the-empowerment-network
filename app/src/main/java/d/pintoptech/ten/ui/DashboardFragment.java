package d.pintoptech.ten.ui;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dinuscxj.progressbar.CircleProgressBar;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import d.pintoptech.ten.LoginActivity;
import d.pintoptech.ten.Prefs.UserInfo;
import d.pintoptech.ten.Profile;
import d.pintoptech.ten.R;
import d.pintoptech.ten.Utils;
import d.pintoptech.ten.VolleySingleton;
import d.pintoptech.ten.util.DashboardRepo;
import d.pintoptech.ten.util.FragmentUtility;
import d.pintoptech.ten.util.RecyclerTouchListener;
import d.pintoptech.ten.util.RecyclerviewAdapter;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    View fragView;
    UserInfo userInfo;
    CircleProgressBar circleProgressBar;


    TextView LOG,SMALLPERCENT;

    List<DashboardRepo> data;
    private RecyclerView recyclerview;
    private ArrayList<DashboardRepo> arrayList;
    private RecyclerviewAdapter adapter;

    private ShimmerFrameLayout mShimmerViewContainer;
    private LinearLayout MAIN;

    String add;
    SwipeRefreshLayout swipeRefreshLayout;


    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragView = inflater.inflate(R.layout.fragment_dashboard2, container, false);

        FragmentUtility.FragmentUtilityMain(getActivity(),R.string.dashboard);

        swipeRefreshLayout = getActivity().findViewById(R.id.container);
        swipeRefreshLayout.setOnRefreshListener(this);

        userInfo = new UserInfo(getActivity());
        circleProgressBar = fragView.findViewById(R.id.progress);
        circleProgressBar.setProgressFormatter(new MyProgressFormatter());

        mShimmerViewContainer = fragView.findViewById(R.id.shimmer_view_container);
        MAIN = fragView.findViewById(R.id.main);

        LOG = fragView.findViewById(R.id.log);
        SMALLPERCENT = fragView.findViewById(R.id.smallPercent);
        recyclerview = fragView.findViewById(R.id.recyclerview);
        arrayList = new ArrayList<>();
        adapter = new RecyclerviewAdapter(getActivity(), arrayList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerview.setLayoutManager(mLayoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setNestedScrollingEnabled(true);
        recyclerview.setAdapter(adapter);
        recyclerview.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerview, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                TextView dId = view.findViewById(R.id.did);
                final String id = dId.getText().toString();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                Bundle arguments = new Bundle();
                arguments.putString("id", id);
                ViewNotifications myFragment = new ViewNotifications();
                myFragment.setArguments(arguments);
                fm.beginTransaction().replace(R.id.content_frame, myFragment).addToBackStack("landing").commit();

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        fetchstats();
        getData();
        getNotificationCounts();
        return fragView;
    }

    private void getNotificationCounts() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.NOTIFICATION_COUNTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        TextView BAdGE = getActivity().findViewById(R.id.badge);
                        int counts = Integer.parseInt(response);
                        if(counts > 0){
                            BAdGE.setVisibility(View.VISIBLE);
                        }else {
                            BAdGE.setVisibility(View.GONE);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Please ensure you have a working internet!", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", userInfo.getKeyEmail());

                return params;
            }
        };

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);

    }

    private void getData(){
        final String email_address = userInfo.getKeyEmail();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.DASHBOARD_DATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(!response.equalsIgnoreCase("error")){

                            try {
                                JSONObject jobj = new JSONObject(response);


                                int percentage = jobj.getInt("percentage");
                                String passport = jobj.getString("passport");
                                int log = jobj.getInt("beneficiaries");
                                String stLog = jobj.getString("beneficiaries");
                                String small = jobj.getString("smallPercent");
                                int smalli = jobj.getInt("beneficiaries");

                                userInfo.setPassport(passport);

                                CircleImageView circleImageView = getActivity().findViewById(R.id.avatar);
                                CircleImageView circleImageViewGen = getActivity().findViewById(R.id.avatarGen);

                                Picasso.with(getActivity()).load("https://www.theempowermentnetwork.ng/"+passport)
                                        .placeholder(R.drawable.unknown_person)
                                        .error(R.drawable.unknown_person)
                                        .into(circleImageView);
                                Picasso.with(getActivity()).load("https://www.theempowermentnetwork.ng/"+passport)
                                        .placeholder(R.drawable.unknown_person)
                                        .error(R.drawable.unknown_person)
                                        .into(circleImageViewGen);

                                circleProgressBar.setProgress(percentage);
                                LOG.setText(stLog);

                                if(smalli < 6){
                                    LOG.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_downward_black_24dp, 0);
                                }else {
                                    LOG.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_upward_black_24dp, 0);
                                }
                                if(log < 40){
                                    add = "-";
                                    SMALLPERCENT.setTextColor(getResources().getColor(R.color.colorDanger));
                                }else {
                                    add = "+";
                                    SMALLPERCENT.setTextColor(getResources().getColor(R.color.colorAccent));
                                }
                                SMALLPERCENT.setText(add+small+"%");

                                MAIN.setVisibility(View.VISIBLE);
                                mShimmerViewContainer.setVisibility(View.GONE);
                                swipeRefreshLayout.setRefreshing(false);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else {
                            String errorMsg = "User with <b>credentials </b> not found on this server!";
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setMessage(Html.fromHtml(errorMsg));
                            builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            builder.show();
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
                params.put("email", email_address);

                return params;
            }
        };

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }

    @Override
    public void onRefresh() {
        getNotificationCounts();
        getData();
        fetchstats();
    }

    private static final class MyProgressFormatter implements CircleProgressBar.ProgressFormatter {
        private static final String DEFAULT_PATTERN = "%d%%";

        @Override
        public CharSequence format(int progress, int max) {
            return String.format(DEFAULT_PATTERN, (int) ((float) progress / (float) max * 100));
        }
    }

    private void fetchstats() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.DASHBOARD_ACTIVIES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //Log.e("TAG", response);

                        data = new Gson().fromJson(response, new TypeToken<List<DashboardRepo>>() {
                        }.getType());

                        arrayList.clear();
                        arrayList.addAll(data);
                        adapter.notifyDataSetChanged();

                        MAIN.setVisibility(View.VISIBLE);
                        mShimmerViewContainer.setVisibility(View.GONE);
                        swipeRefreshLayout.setRefreshing(false);

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
                params.put("email", userInfo.getKeyEmail());

                return params;
            }
        };
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);

    }

}
