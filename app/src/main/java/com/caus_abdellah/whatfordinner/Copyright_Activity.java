package com.caus_abdellah.whatfordinner;

import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Copyright_Activity extends AppCompatDialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflter, ViewGroup group, Bundle savedInstanceState) {
        View view = inflter.inflate(R.layout.copyright_activity_layout, group,  false);
        getDialog().setCanceledOnTouchOutside(true);
        return view;
    }

}
