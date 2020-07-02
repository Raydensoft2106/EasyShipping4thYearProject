package com.example.easyshipping;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.easyshipping.Model.User;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    //test
    private EditText inputEmail, inputPassword;
    private FirebaseAuth firebaseAuth;
    private Button btnSignUp, btnLogin;
    private ProgressBar progressBar;

    RelativeLayout rellay1, rellay2;

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            rellay1.setVisibility(View.VISIBLE);
            rellay2.setVisibility(View.VISIBLE);
        }
    };

    public static String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("EasyShipping");

        rellay1 = (RelativeLayout) findViewById(R.id.rellay1);
        rellay2 = (RelativeLayout) findViewById(R.id.rellay2);

        handler.postDelayed(runnable, 2000); //2000 is the timeout for the splash


        findViewById(R.id.btnLogin).setOnClickListener(this);

        findViewById(R.id.forget_password_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ForgetAndChangePasswordActivity.class).putExtra("Mode", 0));
            }
        });
    }

    public void userLogin(String email, String password) {
        email=inputEmail.getText().toString().trim();

        if (email.isEmpty()) {
            inputEmail.setError(getString(R.string.input_error_email));
            inputEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            inputPassword.setError(getString(R.string.input_error_password));
            inputPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);


        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if(task.isSuccessful()){
                            onAuthSuccess(task.getResult().getUser());
                        }
                        else {
                            Toast.makeText(LoginActivity.this, "Could not login, password or email wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void onAuthSuccess(FirebaseUser user) {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String userId = firebaseUser.getUid();
        if (user != null) {
            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
            DatabaseReference usersRef = rootRef.child("Users2").child(userId);

            usersRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child("userType").getValue(String.class).equals("Receiving Customer") ){
                        Intent i=new Intent(LoginActivity.this, ReceiverCustHomeActivity.class);
                        i.putExtra("email", inputEmail.getText().toString().trim());
                        startActivity(i);
                        //startActivity(new Intent(LoginActivity.this, ReceiverCustHomeActivity.class));
                        Toast.makeText(LoginActivity.this, "You're Logged in as Receiving Customer", Toast.LENGTH_SHORT).show();
                    } else if(dataSnapshot.child("userType").getValue(String.class).equals("Sending Customer")){
                        Intent i=new Intent(LoginActivity.this, SenderCustHomeActivity.class);
                        i.putExtra("email", inputEmail.getText().toString().trim());
                        startActivity(i);
                        //startActivity(new Intent(LoginActivity.this, SenderCustHomeActivity.class));
                        Toast.makeText(LoginActivity.this, "You're Logged in as Sending Customer", Toast.LENGTH_SHORT).show();
                    } else if(dataSnapshot.child("userType").getValue(String.class).equals("Warehouse Employee")){
                        Intent i=new Intent(LoginActivity.this, WarehouseEmpHomeActivity.class);
                        i.putExtra("email", inputEmail.getText().toString().trim());
                        startActivity(i);
                        //startActivity(new Intent(LoginActivity.this, WarehouseEmpHomeActivity.class));
                        Toast.makeText(LoginActivity.this, "You're Logged in as Warehouse Employee", Toast.LENGTH_SHORT).show();
                    } else if(dataSnapshot.child("userType").getValue(String.class).equals("Delivery Driver")){
                        Intent i=new Intent(LoginActivity.this, DriverEmpHomeActivity.class);
                        i.putExtra("email", inputEmail.getText().toString().trim());
                        startActivity(i);
                        //startActivity(new Intent(LoginActivity.this, DriverEmpHomeActivity.class));
                        Toast.makeText(LoginActivity.this, "You're Logged in as Delivery Driver", Toast.LENGTH_SHORT).show();
                    } else {
                        //startActivity(new Intent(LoginActivity.this, NotMainActivity.class));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            //inputEmail.setText("");
            inputPassword.setText("");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                userLogin(inputEmail.getText().toString().trim(), inputPassword.getText().toString().trim());
                break;
        }
    }

    @Override
    protected void onResume() {
        if (firebaseAuth.getCurrentUser() != null) {
            firebaseAuth.signOut();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }
        super.onResume();
    }

    public void onLoginClick(View view) {
        startActivity(new Intent(getApplicationContext(), RegisterActivity.class).putExtra("Mode", 2));
    }
}