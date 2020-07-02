package com.example.easyshipping;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;

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

public class ReceiverCustHomeActivity extends AppCompatActivity {
    //test
    FirebaseAuth auth;
    FirebaseUser user;
    ProgressDialog PD;
    GridLayout mainGrid;
    CardView ViewOrdersBtn, TrackBtn, ChangeConBtn, ReturnParcelBtn, ConfigLocBtn, btnSignOut;

    private int userID;//from intent
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receivercustmain);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("EasyShipping");

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        PD = new ProgressDialog(this);
        PD.setMessage("Loading...");
        PD.setCancelable(true);
        PD.setCanceledOnTouchOutside(false);

        //userID=getIntent().getIntExtra("userID", 0);//from intent
        email=getIntent().getStringExtra("email");
        getRecId();

        mainGrid = (GridLayout)findViewById(R.id.mainGrid);
        ViewOrdersBtn = (CardView) findViewById(R.id.ViewOrdersBtn);
        TrackBtn = (CardView) findViewById(R.id.TrackBtn);
        ChangeConBtn = (CardView) findViewById(R.id.ChangeConBtn);
        ConfigLocBtn = (CardView) findViewById(R.id.ConfigLocBtn);
        ReturnParcelBtn = (CardView) findViewById(R.id.ReturnParcelBtn);
        btnSignOut = (CardView) findViewById(R.id.btnSignOut);

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });

        ReturnParcelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gIntent = new Intent(ReceiverCustHomeActivity.this, ReturnParcelActivity.class);
                gIntent.putExtra("userID",userID);
                startActivity(gIntent);
            }
        });
        ConfigLocBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gIntent = new Intent(ReceiverCustHomeActivity.this, ConfigLocationActivity.class);
                gIntent.putExtra("userID",userID);
                gIntent.putExtra("email",email);
                startActivity(gIntent);
            }
        });
        ViewOrdersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gIntent = new Intent(ReceiverCustHomeActivity.this, ViewConsignmentsActivity.class);
                gIntent.putExtra("userID",userID);
                startActivity(gIntent);
            }
        });
        TrackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent rIntent = new Intent(ReceiverCustHomeActivity.this, TrackParcelActivity.class);
                rIntent.putExtra("userID",userID);
                startActivity(rIntent);
            }
        });
        ChangeConBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cIntent = new Intent(ReceiverCustHomeActivity.this, ChangeLabelActivity.class);
                cIntent.putExtra("userID",userID);
                startActivity(cIntent);
            }
        });
    }

    @Override
    protected void onResume() {
        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(ReceiverCustHomeActivity.this, LoginActivity.class));
            finish();
        }
        super.onResume();
    }

    private void getRecId() {

        String url= "https://easyshippingrh.000webhostapp.com/SelectCurrentRecUser.php?email="+email;
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //Log.i("tagconvertstr", "["+response+"]");
                            JSONArray jsonarray = new JSONArray(response);
                            for(int i=0; i < jsonarray.length(); i++) {
                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                String recid = jsonobject.getString("receiver_id");
                                //email = jsonobject.getString("email");
                                userID = Integer.parseInt(recid);
                                //Log.i("ATTENTION:", "id is: "+recid);
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
