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

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import d.pintoptech.ten.util.RecyclerviewAdapter;
import d.pintoptech.ten.util.WalletAdapter;
import d.pintoptech.ten.util.WalletRepo;

/**
 * A simple {@link Fragment} subclass.
 */
public class WalletFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    View fragView;
    private FragmentManager fragmentManager;
    private TextView DEBITS,BALANCE;

    List<WalletRepo> data;
    private RecyclerView recyclerview;
    private WalletAdapter adapter;
    private UserInfo userInfo;

    SwipeRefreshLayout swipeRefreshLayout;

    private ShimmerFrameLayout mShimmerViewContainer;
    private LinearLayout MAIN;


    public WalletFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragView = inflater.inflate(R.layout.fragment_wallet, container, false);
        fragmentManager = getActivity().getSupportFragmentManager();
        FragmentUtility.FragmentUtilityGen(getActivity(), fragmentManager, R.string.wallet);

        mShimmerViewContainer = fragView.findViewById(R.id.shimmer_view_container);
        MAIN = fragView.findViewById(R.id.main);

        swipeRefreshLayout = getActivity().findViewById(R.id.container);
        swipeRefreshLayout.setOnRefreshListener(this);

        DEBITS = fragView.findViewById(R.id.debits);
        BALANCE = fragView.findViewById(R.id.balance);

        userInfo = new UserInfo(getActivity());

        recyclerview = fragView.findViewById(R.id.recyclerview);
        data = new ArrayList<>();
        adapter = new WalletAdapter(getActivity().getApplicationContext(), data);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerview.setLayoutManager(mLayoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setNestedScrollingEnabled(true);
        recyclerview.setAdapter(adapter);
        fetchWallet();
        return fragView;
    }

    private void fetchWallet() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.GET_WALLET,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        MAIN.setVisibility(View.VISIBLE);
                        mShimmerViewContainer.setVisibility(View.GONE);
                        swipeRefreshLayout.setRefreshing(false);

                        try{
                            JSONObject jobj = new JSONObject(response);
                            String balance = jobj.getString("balance");
                            String debits = jobj.getString("debits");

                            BALANCE.setText("₦"+balance);
                            DEBITS.setText("₦"+debits);


                            JSONArray dat = jobj.getJSONArray("data");


                            for (int i = 0; i < dat.length(); i++) {
                                JSONObject dataobj = dat.getJSONObject(i);
                                String day = dataobj.getString("dday");
                                String month = dataobj.getString("dmonth");
                                String year = dataobj.getString("dyear");
                                String desc = dataobj.getString("ddesc");
                                String time = dataobj.getString("dtime");
                                String amount = dataobj.getString("damount");
                                String type = dataobj.getString("dtype");
                                WalletRepo walletRepo = new WalletRepo(day,month,year,desc,time,amount,type);
                                data.add(walletRepo);
                            }

                            adapter.notifyDataSetChanged();

                        }catch (JSONException e) {
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
                params.put("email", userInfo.getKeyEmail());

                return params;
            }
        };

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);

    }


    @Override
    public void onRefresh() {
        fetchWallet();
    }
}