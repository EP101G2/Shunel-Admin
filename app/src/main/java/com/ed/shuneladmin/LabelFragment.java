package com.ed.shuneladmin;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class LabelFragment extends Fragment {

    private static final String ARG_COUNT = "param1";
    private Integer counter;
    //    private int[] label={R.string.Popular_product,R.string.Promotion,R.string.All,R.string.Perfume_necklace,R.string.Fragrance_earrings,R.string.Necklace,R.string.Earrings};
    private Activity activity;


    public LabelFragment() {
        // Required empty public constructor
    }


    public static LabelFragment newInstance(Integer counter) {
        LabelFragment fragment = new LabelFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COUNT, counter);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            counter = getArguments().getInt(ARG_COUNT);
        }
        activity=getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_label, container, false);
    }


    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);







    }


}