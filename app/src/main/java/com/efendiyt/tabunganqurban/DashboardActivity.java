package com.efendiyt.tabunganqurban;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.efendiyt.tabunganqurban.ui.login.LoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class DashboardActivity extends AppCompatActivity {

    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        BottomNavigationView navigationView = findViewById(R.id.bottom_nav);
        navigationView.setOnNavigationItemSelectedListener(selectedListener);

        firebaseAuth = FirebaseAuth.getInstance();

        //default bottom navigation view
        setDefaultBottomNav();
    }

    private void setDefaultBottomNav() {
        HomeFragment fm1 = new HomeFragment();
        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
        ft1.replace(R.id.content, fm1, "");
        ft1.commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener selectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            //handle click navigation view
            switch (menuItem.getItemId()) {
                case R.id.home:
                    //home fragment transaction
                    HomeFragment fm1 = new HomeFragment();
                    FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
                    ft1.replace(R.id.content, fm1, "");
                    ft1.commit();
                    return true;
                case R.id.tabungan:
                    //tabungan fragment transaction
                    TabunganFragment fm2 = new TabunganFragment();
                    FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                    ft2.replace(R.id.content, fm2, "");
                    ft2.commit();
                    return true;
                case R.id.history:
                    //history fragment transaction
                    HistoryFragment fm3 = new HistoryFragment();
                    FragmentTransaction ft3 = getSupportFragmentManager().beginTransaction();
                    ft3.replace(R.id.content, fm3, "");
                    ft3.commit();
                    return true;
                case R.id.notif:
                    //notification fragment transaction
                    NotifFragment fm4 = new NotifFragment();
                    FragmentTransaction ft4 = getSupportFragmentManager().beginTransaction();
                    ft4.replace(R.id.content, fm4, "");
                    ft4.commit();
                    return true;
                case R.id.profile:
                    //profile fragment transaction
                    ProfileFragment fm5 = new ProfileFragment();
                    FragmentTransaction ft5 = getSupportFragmentManager().beginTransaction();
                    ft5.replace(R.id.content, fm5, "");
                    ft5.commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onStart() {
        checkUserStatus();
        super.onStart();
    }

    private void checkUserStatus() {
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user != null) {
            //user is signed in stay here
        } else {
            //user is not signed in, go to Login Activity
            startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
            finish();
        }
    }
}
