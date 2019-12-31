package com.example.myapplication.ui.home;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.ListFragment;
import com.example.myapplication.R;
import com.example.myapplication.ReportFragment;
import com.example.myapplication.ui.SectionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;


public class HomeFragment extends Fragment {

    private  TabLayout tabs;
    private  SectionsPagerAdapter sectionsPagerAdapter;
    private  ViewPager viewPager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        View root = inflater.inflate(R.layout.fragment_home, container, false);
        sectionsPagerAdapter = new SectionsPagerAdapter(getActivity(), getActivity().getSupportFragmentManager(), new Fragment[]{new ListFragment(),new ReportFragment()} );
        viewPager = root.findViewById(R.id.viewPager);
        viewPager.setAdapter(sectionsPagerAdapter);
        tabs = root.findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        return root;
    }
}