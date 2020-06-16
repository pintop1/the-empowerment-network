package d.pintoptech.ten.ui;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import d.pintoptech.ten.R;
import d.pintoptech.ten.util.FragmentUtility;

/**
 * A simple {@link Fragment} subclass.
 */
public class BeneficiariesFragment extends Fragment {

    View fragView;
    TextView TITLE;
    private FragmentManager fragmentManager;

    public BeneficiariesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragView = inflater.inflate(R.layout.fragment_beneficiaries, container, false);
        fragmentManager = getActivity().getSupportFragmentManager();
        FragmentUtility.FragmentUtilityGen(getActivity(),fragmentManager,R.string.beneficiaries);
        fragView.findViewById(R.id.groups).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.content_frame, new GroupsFragment()).addToBackStack("landing").commit();
            }
        });
        fragView.findViewById(R.id.addGroup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.content_frame, new AddGroupFragment(), "contentd").addToBackStack("landing").commit();
            }
        });
        fragView.findViewById(R.id.beneficiaries).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.content_frame, new AllBeneficiaries()).addToBackStack("landing").commit();
            }
        });
        fragView.findViewById(R.id.addBeneficiaries).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.content_frame, new AddBeneficiaries(), "contentd").addToBackStack("landing").commit();
            }
        });
        return fragView;
    }

}
