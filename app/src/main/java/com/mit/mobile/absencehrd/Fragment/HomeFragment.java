package com.mit.mobile.absencehrd.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.mit.mobile.absencehrd.Activity.HomeActivity;
import com.mit.mobile.absencehrd.Helper.Constants;
import com.mit.mobile.absencehrd.Helper.SessionManager;
import com.mit.mobile.absencehrd.R;

public class HomeFragment extends Fragment {

    SessionManager sessionManager;
    RelativeLayout relativeLayout;
    boolean doubleBackToExitPressedOnce = false;

    public HomeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        sessionManager = new SessionManager(getActivity());


        Button toLogin;


        Log.e("AAA", ""+ Constants.currentUserID);

        relativeLayout = view.findViewById(R.id.relativeLayout);

        if (!sessionManager.isConnectedToNetwork())
        {
            Snackbar snackbar = Snackbar.make(relativeLayout, "Network Is Not Available!", Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sessionManager.isConnectedToNetwork();
                            startActivity(new Intent(getActivity(), HomeActivity.class));
                        }
                    });
            snackbar.show();
        }


        return view;
    }


}
