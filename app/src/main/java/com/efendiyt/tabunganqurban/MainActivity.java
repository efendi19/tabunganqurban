package com.efendiyt.tabunganqurban;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.efendiyt.tabunganqurban.ui.login.LoginActivity;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    GoogleSignInClient mGoogleSignInClient;
    TextView username_tv, email_tv;
    ImageView photo_iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username_tv  = findViewById(R.id.usernya);
        email_tv = findViewById(R.id.email_g);
        final Button logout_btn = findViewById(R.id.logout);
        photo_iv = findViewById(R.id.photo);

        //initialization FirebaseAuth getInstance
        firebaseAuth = FirebaseAuth.getInstance();

        logout_btn.setOnClickListener(view -> {
            logout();
        });

    }

    @Override
    protected void onStart() {
        //check on start of app first time
        checkUserStatus();
        super.onStart();
    }

    private void checkUserStatus() {
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user != null) {
            //user is signed in stay here
            String photo = String.valueOf(user.getPhotoUrl());
            username_tv.setText(user.getDisplayName());
            email_tv.setText(user.getEmail());
            Picasso.get().load(photo).into(photo_iv);
        } else {
            //user is not signed in, go to Login Activity
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
    }

    void logout () {
        firebaseAuth.signOut();
        checkUserStatus();
//        FirebaseAuth.getInstance().signOut();
//        mGoogleSignInClient.signOut().addOnCompleteListener(this, task -> {
//            //updateUI(null);
//            Toast.makeText(MainActivity.this, "Berhasil logout", Toast.LENGTH_SHORT).show();
//        });
    }
}
