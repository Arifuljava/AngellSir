package com.grozziie.grozziie_pdf;;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;



import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.grozziie.grozziie_pdf.BlueTooth.BlueToothMainActivity;

public class MeFragment extends Fragment {
    private Button deviceButton;
    private Button barButton;
    private Button helpButton;
    private Button setButton;
    private ImageView backLastMe;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_me,container,false);
        //return super.onCreateView(inflater, container, savedInstanceState);
        return view;
}

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        deviceButton=view.findViewById(R.id.meDevice);
        barButton=view.findViewById(R.id.meBar);
        helpButton=view.findViewById(R.id.meHelp);
        setButton=view.findViewById(R.id.meSet);
        backLastMe=view.findViewById(R.id.backLastMe);
        backLastMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager MF = getActivity().getSupportFragmentManager();
                MF.beginTransaction().replace(R.id.goodsDisplay,new HomeFragment()).commitAllowingStateLoss();
            }
        });
        deviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inent=new Intent(getActivity(), BlueToothMainActivity.class);
                startActivity(inent);
            }
        });

    }
}
