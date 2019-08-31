package com.efendiyt.tabunganqurban;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.efendiyt.tabunganqurban.ui.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    TextView iniNama_tv, iniEmail_tv;
    ImageView iniPhoto_iv;
    Button keluar_btn;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        //initialization Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");

        //initialization views
        iniNama_tv = v.findViewById(R.id.iniNama);
        iniEmail_tv = v.findViewById(R.id.iniEmail);
        iniPhoto_iv = v.findViewById(R.id.iniPhoto);
        keluar_btn = v.findViewById(R.id.keluar);

        keluar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut();
                Toast.makeText(getContext(), "Berhasil keluar", Toast.LENGTH_SHORT).show();
            }
        });

        if (firebaseUser !=null) {
            onUserData();
        } else {
            //
        }

        return v;
    }

    private void onUserData() {
        Query query = databaseReference.orderByChild("email").equalTo(firebaseUser.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //check until required data get
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    //get data
                    String username = ""+ds.child("name").getValue();
                    String email = ""+ds.child("email").getValue();
                    String photo = ""+ds.child("photo").getValue();

                    //set data
                    iniNama_tv.setText(username);
                    iniEmail_tv.setText(email);
                    try {
                        Picasso.get().load(photo).into(iniPhoto_iv);
                    } catch (Exception e) {
                        Picasso.get().load(R.drawable.ic_profile).into(iniPhoto_iv);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //
            }
        });
    }

    private void logOut() {
        firebaseAuth.signOut();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }

}
