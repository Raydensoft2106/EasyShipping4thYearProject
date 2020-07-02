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

public class DriverEmpHomeActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;
    ProgressDialog PD;
    GridLayout mainGrid;
    CardView ScanBtn, DepotBtn, DeliveryListBtn, RouteBtn, DeliveredBtn, LogoutBtn;

    private int userID;//from intent
    private String depot;
    private String driverEmail;
    private String driverName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driveremployeemain);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("EasyShipping");

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        PD = new ProgressDialog(this);
        PD.setMessage("Loading...");
        PD.setCancelable(true);
        PD.setCanceledOnTouchOutside(false);

        driverEmail = getIntent().getStringExtra("email");
        getDriId(driverEmail);
        userID=getIntent().getIntExtra("userID", 0);//from intent

        mainGrid = (GridLayout)findViewById(R.id.mainGrid);

        LogoutBtn = (CardView) findViewById(R.id.btnSignOut);
        ScanBtn = (CardView) findViewById(R.id.ScanBtn);
        DepotBtn = (CardView) findViewById(R.id.DepotBtn);
        DeliveryListBtn = (CardView) findViewById(R.id.DeliveryListBtn);
        RouteBtn = (CardView) findViewById(R.id.RouteBtn);
        DeliveredBtn = (CardView) findViewById(R.id.DeliveredBtn);

        ScanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gIntent = new Intent(DriverEmpHomeActivity.this, DriverReaderActivity.class);
                gIntent.putExtra("userID",userID);
                gIntent.putExtra("depot",depot);
                startActivity(gIntent);
            }
        });
        DepotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent rIntent = new Intent(DriverEmpHomeActivity.this, DepotMapActivity.class);
                startActivity(rIntent);
            }
        });

        RouteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent rIntent = new Intent(DriverEmpHomeActivity.this, PermissionsActivity.class);
                Bundle extras = new Bundle();
                extras.putInt("userID",userID);
                extras.putString("depot",depot);
                extras.putString("driverEmail",driverEmail);
                extras.putString("DriverName", driverName);
                rIntent.putExtras(extras);
                startActivity(rIntent);
            }
        });

        DeliveryListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent rIntent = new Intent(DriverEmpHomeActivity.this, DeliveryListActivity.class);
                rIntent.putExtra("userID",userID);
                rIntent.putExtra("depot",depot);
                startActivity(rIntent);
            }
        });

        DeliveredBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent rIntent = new Intent(DriverEmpHomeActivity.this, DeliveredParcelsActivity.class);
                rIntent.putExtra("userID",userID);
                rIntent.putExtra("depot",depot);
                startActivity(rIntent);
            }
        });


        LogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });
    }


    @Override    protected void onResume() {
        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(DriverEmpHomeActivity.this, LoginActivity.class));
            finish();
        }
        super.onResume();
    }

    private void getDriId(String email) {

        String url= "https://easyshippingrh.000webhostapp.com/SelectCurrentDriveUser.php?email="+email;
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            System.out.println("Response: "+response);
                            JSONArray jsonarray = new JSONArray(response);
                            for(int i=0; i < jsonarray.length(); i++) {
                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                String driid = jsonobject.getString("driver_id");
                                depot = jsonobject.getString("depot");
                                userID = Integer.parseInt(driid);
                                driverEmail = jsonobject.getString("email");
                                String fName = jsonobject.getString("fName");
                                String lName = jsonobject.getString("lName");
                                driverName = fName + " " + lName;
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
