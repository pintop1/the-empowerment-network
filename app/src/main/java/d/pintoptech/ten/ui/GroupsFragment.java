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

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import d.pintoptech.ten.Prefs.UserInfo;
import d.pintoptech.ten.R;
import d.pintoptech.ten.Utils;
import d.pintoptech.ten.VolleySingleton;
import d.pintoptech.ten.util.FragmentUtility;
import d.pintoptech.ten.util.GroupsRecyclerView;
import d.pintoptech.ten.util.GroupsRepo;
import d.pintoptech.ten.util.RecyclerTouchListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    View fragView;
    private FragmentManager fragmentManager;
    List<GroupsRepo> data;
    List<GroupsRepo> dataFiltered;
    private RecyclerView recyclerview;
    private ArrayList<GroupsRepo> arrayList;
    private GroupsRecyclerView adapter;
    private UserInfo userInfo;

    private EditText SEARCH;

    LinearLayout empty;

    String query;

    SwipeRefreshLayout swipeRefreshLayout;

    private ShimmerFrameLayout mShimmerViewContainer;
    private LinearLayout MAIN;



    public GroupsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragView = inflater.inflate(R.layout.fragment_groups, container, false);

        swipeRefreshLayout = getActivity().findViewById(R.id.container);
        swipeRefreshLayout.setOnRefreshListener(this);

        mShimmerViewContainer = fragView.findViewById(R.id.shimmer_view_container);
        MAIN = fragView.findViewById(R.id.main);

        SEARCH = fragView.findViewById(R.id.search);

        fragmentManager = getActivity().getSupportFragmentManager();
        FragmentUtility.FragmentUtilityGen(getActivity(),fragmentManager,R.string.groups);
        fragView.findViewById(R.id.navigate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, new AddGroupFragment(), "contentd").addToBackStack("landing").commit();
            }
        });
        userInfo = new UserInfo(getActivity());
        empty = fragView.findViewById(R.id.empty);

        recyclerview = fragView.findViewById(R.id.recyclerview);
        arrayList = new ArrayList<>();
        adapter = new GroupsRecyclerView(getActivity(), arrayList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerview.setLayoutManager(mLayoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setNestedScrollingEnabled(true);
        recyclerview.setAdapter(adapter);
        recyclerview.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerview, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                TextView dId = view.findViewById(R.id.did);
                TextView dName = view.findViewById(R.id.dname);
                final String id = dId.getText().toString();
                final String name = dName.getText().toString();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                Bundle arguments = new Bundle();
                arguments.putString("id", id);
                arguments.putString("name", name);
                GroupViewFragment myFragment = new GroupViewFragment();
                myFragment.setArguments(arguments);
                fm.beginTransaction().replace(R.id.content_frame, myFragment, "contentc").addToBackStack("dashboard").commit();

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        fetchGroups();
        return fragView;
    }

    public void fetchGroups() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.FETCH_GROUPS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try{

                            JSONArray adr = new JSONArray(response);
                            if(adr.length() < 1){
                                empty.setVisibility(View.VISIBLE);
                                MAIN.setVisibility(View.GONE);
                                mShimmerViewContainer.setVisibility(View.GONE);
                                swipeRefreshLayout.setRefreshing(false);
                            }else {
                                data = new Gson().fromJson(response, new TypeToken<List<GroupsRepo>>() {
                                }.getType());

                                arrayList.clear();
                                arrayList.addAll(data);
                                adapter.notifyDataSetChanged();

                                MAIN.setVisibility(View.VISIBLE);
                                mShimmerViewContainer.setVisibility(View.GONE);
                                swipeRefreshLayout.setRefreshing(false);
                                empty.setVisibility(View.GONE);

                                getSearch();
                            }

                        }catch(JSONException e){
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
        fetchGroups();
    }

    public void getSearch(){
        SEARCH.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                query = s.toString().toUpperCase();

                dataFiltered = new ArrayList<>();

                for (int i = 0; i < data.size(); i++) {

                    final String text = data.get(i).getGroupName().toUpperCase();
                    if (text.contains(query)) {

                        dataFiltered.add(data.get(i));
                    }
                }
                arrayList.clear();
                arrayList.addAll(dataFiltered);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}
