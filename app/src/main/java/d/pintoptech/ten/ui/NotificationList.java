package d.pintoptech.ten.ui;


import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dinuscxj.progressbar.CircleProgressBar;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import d.pintoptech.ten.Prefs.UserInfo;
import d.pintoptech.ten.R;
import d.pintoptech.ten.Utils;
import d.pintoptech.ten.VolleySingleton;
import d.pintoptech.ten.util.DashboardRepo;
import d.pintoptech.ten.util.FragmentUtility;
import d.pintoptech.ten.util.RecyclerTouchListener;
import d.pintoptech.ten.util.RecyclerviewAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationList extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    View fragView;
    UserInfo userInfo;

    SwipeRefreshLayout swipeRefreshLayout;

    List<DashboardRepo> data;
    private RecyclerView recyclerview;
    private ArrayList<DashboardRepo> arrayList;
    private RecyclerviewAdapter adapter;

    private FragmentManager fragmentManager;

    private ShimmerFrameLayout mShimmerViewContainer;
    private LinearLayout MAIN;


    public NotificationList() {
        // Required empty public constructor
    }


    @Override
    public void onRefresh() {
        fetchstats();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragView = inflater.inflate(R.layout.fragment_notification_list, container, false);

        userInfo = new UserInfo(getActivity());

        fragmentManager = getActivity().getSupportFragmentManager();
        FragmentUtility.FragmentUtilityGen(getActivity(),fragmentManager,R.string.notifications);

        mShimmerViewContainer = fragView.findViewById(R.id.shimmer_view_container);
        MAIN = fragView.findViewById(R.id.main);

        swipeRefreshLayout = getActivity().findViewById(R.id.container);
        swipeRefreshLayout.setOnRefreshListener(this);

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
        return fragView;
    }


    private void fetchstats() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.ALL_NOTIFICATIONS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

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
