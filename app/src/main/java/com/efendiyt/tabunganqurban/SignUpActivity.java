package com.efendiyt.tabunganqurban;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.efendiyt.tabunganqurban.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    private  FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    EditText usernameEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        final TextView signInText = findViewById(R.id.signIn);
        final Button signUpBtn = findViewById(R.id.daftar);
        final EditText emailEt = findViewById(R.id.email);
        usernameEt = findViewById(R.id.username);
        final EditText passwordEt = findViewById(R.id.password);
        final EditText confirmPassEt = findViewById(R.id.confirm_password);
        final ProgressBar loadingPB = findViewById(R.id.loading);

        //when text sign in click
        signInText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Go to login activity
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                finish();
            }
        });

        //initialize Firebase Auth instance
        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sedang didaftarkan, mohon tunggu...");

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username_et = usernameEt.getText().toString().trim();
                String email_et = emailEt.getText().toString().trim();
                String password_et = passwordEt.getText().toString().trim();
                String confirmPass_et = confirmPassEt.getText().toString().trim();

                if (TextUtils.isEmpty(username_et)) {
                    usernameEt.setError("Masukan nama anda!");
                    return;
                } if (TextUtils.isEmpty(email_et)) {
                    emailEt.setError("Masukan email anda!");
                    return;
                } if (TextUtils.isEmpty(password_et)) {
                    passwordEt.setError("Masukan password anda!");
                    return;
                } if (TextUtils.isEmpty(confirmPass_et)) {
                    confirmPassEt.setError("Masukan kembali password anda!");
                    return;
                } if (!Patterns.EMAIL_ADDRESS.matcher(email_et).matches()) {
                    emailEt.setError("Email tidak valid!");
                    emailEt.setFocusable(true);
                    return;
                } if (password_et.length()<6) {
                    Toast.makeText(SignUpActivity.this, "Password terlalu pendek!.", Toast.LENGTH_SHORT).show();
                    passwordEt.setFocusable(true);
                }

                if (password_et.equals(confirmPass_et)) {
                    registerUser(email_et, password_et);
                } else {
                    Toast.makeText(SignUpActivity.this, "Password tidak sama!.", Toast.LENGTH_SHORT).show();
                    confirmPassEt.setFocusable(true);
                }
            }
        });

    }

    private void registerUser(String email_et, String password_et) {
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email_et, password_et)
                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            String emailUser = user.getEmail();
                            String uid = user.getUid();
                            String username = usernameEt.getText().toString().trim();

                            HashMap<Object, String> hashMap = new HashMap<>();

                            hashMap.put("email", emailUser);
                            hashMap.put("uid",uid);
                            hashMap.put("name",username);
                            hashMap.put("phone", "");
                            hashMap.put("image", "");

                            FirebaseDatabase fbdb = FirebaseDatabase.getInstance();

                            DatabaseReference reference = fbdb.getReference("Users");

                            reference.child(uid).setValue(hashMap);

                            Toast.makeText(SignUpActivity.this, "Terdaftar dengan email : "+user.getEmail(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignUpActivity.this, DashboardActivity.class));
                            finish();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(SignUpActivity.this, "Email/Password tidak benar!.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(SignUpActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
