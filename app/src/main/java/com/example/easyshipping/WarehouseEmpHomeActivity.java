package com.example.easyshipping;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WarehouseEmpHomeActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    ProgressDialog PD;
    GridLayout mainGrid;
    CardView ScanBtn, DepotBtn, ReprintBtn, btnSignOut;

    private int userID;//from intent
    private String depot;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hubemployeemain);

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
        getWarId(email);

        userID=getIntent().getIntExtra("userID", 0);//from intent

        mainGrid = (GridLayout)findViewById(R.id.mainGrid);

        btnSignOut = (CardView) findViewById(R.id.btnSignOut);
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });

        ScanBtn = (CardView) findViewById(R.id.ScanBtn);
        DepotBtn = (CardView) findViewById(R.id.DepotBtn);
        ReprintBtn = (CardView) findViewById(R.id.ReprintBtn);

        ScanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gIntent = new Intent(WarehouseEmpHomeActivity.this, ReaderActivity.class);
                gIntent.putExtra("userID",userID);
                gIntent.putExtra("depot",depot);
                startActivity(gIntent);
            }
        });
        DepotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent rIntent = new Intent(WarehouseEmpHomeActivity.this, DepotMapActivity.class);
                rIntent.putExtra("depot",depot);
                startActivity(rIntent);
            }
        });
        ReprintBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent rIntent = new Intent(WarehouseEmpHomeActivity.this, ReprintLabelActivity.class);
                startActivity(rIntent);
            }
        });
    }

        @Override
        protected void onResume() {
        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(WarehouseEmpHomeActivity.this, LoginActivity.class));
            finish();
        }
        super.onResume();
    }

    private void getWarId(String email) {

        String url= "https://easyshippingrh.000webhostapp.com/SelectCurrentWareUser.php?email="+email;
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("tagconvertstr", "["+response+"]");
                            JSONArray jsonarray = new JSONArray(response);
                            for(int i=0; i < jsonarray.length(); i++) {
                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                String warid = jsonobject.getString("scanner_id");
                                depot = jsonobject.getString("depot");
                                userID = Integer.parseInt(warid);
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
