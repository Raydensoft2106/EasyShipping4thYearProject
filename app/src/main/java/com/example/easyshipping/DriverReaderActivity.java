package com.example.easyshipping;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DriverReaderActivity extends AppCompatActivity {
    private Button scan_btn;
    private Button send_btn;
    private Button return_btn;
    EditText conNo;

    private int userID;//from intent
    private String depot;
    private String recEmail;
    private String recName;
    private String usersName;
    private String driverEmail;

    //public static String readCode;
    String latestReport = ": Your parcel has been delivered by Driver: ";
    String status = "Delivered";
    String currentDepot = "0";

    String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
    String url = "https://easyshippingrh.000webhostapp.com/deliverScanParcel.php";

    private int scanSound;
    private SoundPool soundPool;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drivereader);
        scan_btn = (Button) findViewById(R.id.scan_btn);
        send_btn = (Button) findViewById(R.id.send_btn);
        return_btn = (Button) findViewById(R.id.return_btn);
        conNo = (EditText) findViewById(R.id.conNo);
        final Activity activity = this;
        userID=getIntent().getIntExtra("userID", 0);//from intent
        usersName=getIntent().getStringExtra("DriverName");
        depot=getIntent().getStringExtra("depot");
        recEmail=getIntent().getStringExtra("recEmail");
        driverEmail = getIntent().getStringExtra("driverEmail");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("EasyShipping");

        scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });

        return_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gIntent = new Intent(DriverReaderActivity.this, RouteMapActivity.class);
                gIntent.putExtra("userID",userID);
                gIntent.putExtra("depot",depot);
                gIntent.putExtra("DriverName", usersName);
                gIntent.putExtra("driverEmail", driverEmail);
                startActivity(gIntent);
                finish(); //kills activity so it can't be accessed from back button
            }
        });

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setMaxStreams(1)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            soundPool = new SoundPool(6, AudioManager.STREAM_MUSIC, 0);
        }
        scanSound = soundPool.load(this, R.raw.scan1, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            if(result.getContents()==null){
                Toast.makeText(this, "You cancelled the scanning", Toast.LENGTH_LONG).show();
            }
            else {
                soundPool.play(scanSound,1,1,0,0,1);
                Toast.makeText(this, result.getContents(),Toast.LENGTH_LONG).show();
                conNo.setText(result.getContents());
                getLabelDetails(conNo.getText().toString().trim());
                Intent i = new Intent(getApplicationContext(), SignatureActivity.class);
                startActivity(i);
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        //startActivity(new Intent(this, RouteMapActivity.class));
    }

    public void ScanToDb(View view)
    {
        if (conNo.getText().toString().trim().equals("")) {
            Toast.makeText(this, "Enter a Consignment Number", Toast.LENGTH_SHORT).show();
        }

        else{
            JavaMailAPI javaMailAPI = new JavaMailAPI(this,recEmail,"Your parcel from EasyShipping","Hi "+recName+",\nYour parcel has been Delivered by Driver: "+usersName.toUpperCase()+". If you have any queries or issues regarding your delivery, please give us a call or email. Thanks!\n\nThe EasyShipping Delivery Team");
            javaMailAPI.execute();
            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(DriverReaderActivity.this, response,Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(DriverReaderActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("consignment_id",conNo.getText().toString().trim());
                    params.put("status",status);
                    params.put("latestReport","\n"+date+latestReport+usersName.toUpperCase());
                    params.put("currentDepot",currentDepot);
                    //conNo.setText("");

                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(DriverReaderActivity.this);
            requestQueue.add(request);
        }
    }

    public void getLabelDetails(String trackNo)
    {
        String url= "https://easyshippingrh.000webhostapp.com/trackParcel.php?consignment_id="+trackNo;
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonarray = new JSONArray(response);
                            for(int i=0; i < jsonarray.length(); i++) {
                                JSONObject jsonobject = jsonarray.getJSONObject(i);

                                recName = jsonobject.getString("name").trim();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
//                            conNo.setError("Please enter a valid consignment number");
//                            conNo.requestFocus();
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
