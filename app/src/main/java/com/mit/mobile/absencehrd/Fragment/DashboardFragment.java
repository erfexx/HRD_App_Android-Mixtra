package com.mit.mobile.absencehrd.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import com.mit.mobile.absencehrd.Helper.GPSTracker;
import com.mit.mobile.absencehrd.Helper.SessionManager;
import com.mit.mobile.absencehrd.R;

public class DashboardFragment extends Fragment {

    CardView cvTM, cvA, cvS, cvSO;
    GPSTracker tracker;

    SessionManager sessionManager;

    public DashboardFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_menu, container, false);
        sessionManager = new SessionManager(getActivity());

        cvTM = view.findViewById(R.id.mytaskID);
        cvA = view.findViewById(R.id.attendanceID);
        cvS = view.findViewById(R.id.stockID);
        cvSO = view.findViewById(R.id.salesorderID);

        cvA.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (tracker.inLocation)
                        {

                        }
                        else
                        {
                            Toast.makeText(getActivity(), "Can't Get The Last Position", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
        );

        return view;
    }
}
