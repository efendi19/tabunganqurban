package com.efendiyt.tabunganqurban;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.efendiyt.tabunganqurban.ui.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        //initialization FirebaseAuth getInstance
        firebaseAuth = FirebaseAuth.getInstance();
        return v;
    }

//    @Override
//    public void onStart() {
//        checkUserStatus();
//        super.onStart();
//    }

//    private void checkUserStatus() {
//        FirebaseUser user = firebaseAuth.getCurrentUser();
//
//        if (user != null) {
//            //user is signed in stay here
//        } else {
//            //user is not signed in, go to Login Activity
//            startActivity(new Intent(getActivity(), LoginActivity.class));
//            //finish();
//        }
//    }
}
