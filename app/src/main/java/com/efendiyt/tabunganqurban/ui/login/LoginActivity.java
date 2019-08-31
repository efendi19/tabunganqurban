package com.efendiyt.tabunganqurban.ui.login;

import androidx.annotation.NonNull;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.efendiyt.tabunganqurban.DashboardActivity;
import com.efendiyt.tabunganqurban.MainActivity;
import com.efendiyt.tabunganqurban.R;
import com.efendiyt.tabunganqurban.SignUpActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

  //  private LoginViewModel loginViewModel;
    private  FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    static final int GOOGLE_SIGN = 123;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText emailEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        final TextView registerText = findViewById(R.id.register);
        final ImageView loginGoogle_im = findViewById(R.id.im_google);
        final TextView forgotPass_tv = findViewById(R.id.forgotPass);
      //  final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                finish();
            }
        });

        forgotPass_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show dialog forgot password
                recoveryPasswordDialog();
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder()
                .requestProfile()
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String email_et = emailEditText.getText().toString().trim();
                final String password_et = passwordEditText.getText().toString().trim();

                if (TextUtils.isEmpty(email_et)) {
                    emailEditText.setError("Masukan email anda!");
                } if (TextUtils.isEmpty(password_et)) {
                    passwordEditText.setError("Masukan password anda!");
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email_et).matches()) {
                    //invalid email type
                    emailEditText.setError("Email tidak benar!");
                    emailEditText.setFocusable(true);
                } else {
                    //valid email
                    loginUser(email_et, password_et);
                }
            }
        });

        loginGoogle_im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignInWithGoogle();
            }
        });
    }

    private void recoveryPasswordDialog() {
        //AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Lupa password");

        //setLayout AlertDialog
        LinearLayout ly = new LinearLayout(this);
        //object on AlertDialog
        EditText emailFP = new EditText(this);
        emailFP.setHint("Email");
        emailFP.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        emailFP.layout(20,0,0,20);
        emailFP.setMinEms(20);

        ly.addView(emailFP);
        ly.setPadding(20, 10, 20, 10);
        builder.setView(ly);

        //send click
        builder.setPositiveButton("Kirim ulang kata sandi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //input email forgotten
                String email_fp = emailFP.getText().toString().trim();
                sendRecovery(email_fp);
            }
        });
        //cancel click
        builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }); builder.create().show();


    }

    private void sendRecovery(String email_fp) {
        progressDialog.setMessage("Mengirim email...");
        progressDialog.show();
        firebaseAuth.sendPasswordResetEmail(email_fp).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Silahkan check email anda", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Ada yang tidak beres..", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, "error:"+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loginUser(String email_et, String password_et) {
        progressDialog.setMessage("Mohon tunggu...");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email_et, password_et)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                            finish();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Gagal masuk!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //show info error message
                Toast.makeText(LoginActivity.this, "error :"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    void SignInWithGoogle(){
        progressDialog.show();
        Intent signIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signIntent, GOOGLE_SIGN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_SIGN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                e.printStackTrace();
                Toast.makeText(LoginActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        Log.d("TAG", "firebaseAuthWithGoogle: " + account.getId());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task ->{
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();

                        FirebaseUser user = firebaseAuth.getCurrentUser();

                        //if user is sign in first time the get and show info from google account
                        if (task.getResult().getAdditionalUserInfo().isNewUser()) {
                            String emailUser = user.getEmail();
                            String uid = user.getUid();

                            HashMap<Object, String> hashMap = new HashMap<>();

                            hashMap.put("email", emailUser);
                            hashMap.put("uid",uid);
                            hashMap.put("name","");
                            hashMap.put("phone", "");
                            hashMap.put("image", "");

                            FirebaseDatabase fbdb = FirebaseDatabase.getInstance();

                            DatabaseReference reference = fbdb.getReference("Users");

                            reference.child(uid).setValue(hashMap);
                        }
                        startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                        finish();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Login gagal!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, ""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
