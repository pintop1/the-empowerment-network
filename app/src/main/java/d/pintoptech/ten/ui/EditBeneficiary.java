package d.pintoptech.ten.ui;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import d.pintoptech.ten.R;
import d.pintoptech.ten.util.FragmentUtility;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditBeneficiary extends Fragment {

    View fragView;
    private FragmentManager fragmentManager;


    public EditBeneficiary() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragView = inflater.inflate(R.layout.fragment_edit_beneficiary, container, false);
        fragmentManager = getActivity().getSupportFragmentManager();
        FragmentUtility.FragmentUtilityGenTwo(getActivity(),fragmentManager,"Edit Beneficiary");
        return fragView;
    }

}
