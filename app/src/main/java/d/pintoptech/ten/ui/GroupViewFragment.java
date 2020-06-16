package d.pintoptech.ten.ui;


import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import d.pintoptech.ten.R;
import d.pintoptech.ten.Utils;
import d.pintoptech.ten.VolleySingleton;
import d.pintoptech.ten.util.DashboardRepo;
import d.pintoptech.ten.util.FragmentUtility;
import d.pintoptech.ten.util.MembersRecyclerAdapter;
import d.pintoptech.ten.util.MembersRepo;
import d.pintoptech.ten.util.MemebersRecyclerAdapterTwo;
import d.pintoptech.ten.util.RecyclerTouchListener;
import d.pintoptech.ten.util.RecyclerviewAdapter;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupViewFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    View fragView;

    String id, name;

    private RecyclerView RECYCLERVIEW;
    String add;

    private ShimmerFrameLayout mShimmerViewContainer;
    private RelativeLayout MAIN;

    private FragmentManager fragmentManager;

    LinearLayout empty;

    String query;

    List<MembersRepo> data;
    List<MembersRepo> dataFiltered;
    private EditText SEARCH;
    private ArrayList<MembersRepo> arrayList;
    private MemebersRecyclerAdapterTwo adapter;

    SwipeRefreshLayout swipeRefreshLayout;


    public GroupViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragView = inflater.inflate(R.layout.fragment_group_view, container, false);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            id = bundle.getString("id", "");
            name = bundle.getString("name", "");
        }

        SEARCH = fragView.findViewById(R.id.search);

        fragView.findViewById(R.id.navigate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, new AddBeneficiaries(), "contentd").addToBackStack("landing").commit();
            }
        });
        empty = fragView.findViewById(R.id.empty);

        swipeRefreshLayout = getActivity().findViewById(R.id.container);
        swipeRefreshLayout.setOnRefreshListener(this);

        mShimmerViewContainer = fragView.findViewById(R.id.shimmer_view_container);
        MAIN = fragView.findViewById(R.id.main);

        fragmentManager = getActivity().getSupportFragmentManager();
        FragmentUtility.FragmentUtilityGenTwo(getActivity(),fragmentManager,name);
        RECYCLERVIEW = fragView.findViewById(R.id.recyclerview);
        arrayList = new ArrayList<>();
        adapter = new MemebersRecyclerAdapterTwo(getActivity(), arrayList, getActivity());
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        RECYCLERVIEW.setLayoutManager(mLayoutManager);
        RECYCLERVIEW.setItemAnimator(new DefaultItemAnimator());
        RECYCLERVIEW.setNestedScrollingEnabled(true);
        RECYCLERVIEW.setAdapter(adapter);
        fetchMembers();
        return fragView;
    }

    private void fetchMembers() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.GET_BENEFICIARIES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray ar = new JSONArray(response);
                            if(ar.length() < 1){
                                empty.setVisibility(View.VISIBLE);
                                MAIN.setVisibility(View.GONE);
                                mShimmerViewContainer.setVisibility(View.GONE);
                                swipeRefreshLayout.setRefreshing(false);
                            }else {
                                data = new Gson().fromJson(response, new TypeToken<List<MembersRepo>>() {
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
                params.put("id", id);

                return params;
            }
        };

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);

    }

    @Override
    public void onRefresh() {
        fetchMembers();
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

                    final String text = data.get(i).getName().toUpperCase();
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
