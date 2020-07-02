package com.example.easyshipping;

import android.Manifest;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.telephony.SmsManager;
import android.view.View;
import android.content.Intent;
import android.app.ProgressDialog;
import android.widget.GridLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
//import com.twilio.Twilio;
//import com.twilio.rest.api.v2010.account.Message;
//import com.twilio.type.PhoneNumber;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SenderCustHomeActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    ProgressDialog PD;
    GridLayout mainGrid;
    CardView CreateBtn, TrackBtn, ChangeConBtn, ViewShipBtn, DeactivateBtn, btnSignOut;

    private int userID;//from intent
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendercustmain);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("EasyShipping");

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        PD = new ProgressDialog(this);
        PD.setMessage("Loading...");
        PD.setCancelable(true);
        PD.setCanceledOnTouchOutside(false);

        email = getIntent().getStringExtra("email");
        getSenId();
        userID=getIntent().getIntExtra("userID", 0);//from intent

        mainGrid = (GridLayout)findViewById(R.id.mainGrid);
        btnSignOut = (CardView) findViewById(R.id.btnSignOut);
        CreateBtn = (CardView) findViewById(R.id.CreateBtn);
        ChangeConBtn = (CardView) findViewById(R.id.ChangeConBtn);
        TrackBtn = (CardView) findViewById(R.id.TrackBtn);
        DeactivateBtn = (CardView) findViewById(R.id.DeactivateBtn);
        ViewShipBtn = (CardView) findViewById(R.id.ViewShipBtn);


        CreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gIntent = new Intent(SenderCustHomeActivity.this, CreateLabelActivity.class);
                gIntent.putExtra("userID",userID);
                gIntent.putExtra("email",email);
                startActivity(gIntent);
            }
        });
        ChangeConBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gIntent = new Intent(SenderCustHomeActivity.this, ChangeLabelActivity.class);
                startActivity(gIntent);
            }
        });
        TrackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent rIntent = new Intent(SenderCustHomeActivity.this, TrackParcelActivity.class);
                rIntent.putExtra("userID",userID);
                startActivity(rIntent);
            }
        });
        ViewShipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gIntent = new Intent(SenderCustHomeActivity.this, ViewSentOrdersActivity.class);
                gIntent.putExtra("userID",userID);
                startActivity(gIntent);

            }
        });
        DeactivateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gIntent = new Intent(SenderCustHomeActivity.this, ForgetAndChangePasswordActivity.class);
                startActivity(gIntent);
            }
        });
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });
    }

    private void requestSmsSendPermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.SEND_SMS },
                23);
    }

    @Override
    protected void onResume() {
        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(SenderCustHomeActivity.this, LoginActivity.class));
            finish();
        }
        super.onResume();
    }

    private void getSenId() {

        String url= "https://easyshippingrh.000webhostapp.com/SelectCurrentSenUser.php?email="+email;
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonarray = new JSONArray(response);
                            for(int i=0; i < jsonarray.length(); i++) {
                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                String senid = jsonobject.getString("sender_id");
                                userID = Integer.parseInt(senid);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error != null){
                            Toast.makeText(getApplicationContext(), "Something went wrong.", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}