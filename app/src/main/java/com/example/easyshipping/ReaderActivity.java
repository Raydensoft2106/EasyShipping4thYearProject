package com.example.easyshipping;

import android.app.Activity;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class ReaderActivity extends AppCompatActivity {
    private Button scan_btn;
    private Button send_btn;
    private Button reprint_btn;
    private TextView textView;
    EditText conNo;

    //public String readCode;
    String status = "In Hub", myCounty, parcelDestCounty = "", recEmail, recName;
    public String currentDepot = "";
    public String latestReport = ": Your parcel has been scanned at Depot" + currentDepot;
    private int scanSound;
    private SoundPool soundPool;

    String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
    String mainUrl = "https://easyshippingrh.000webhostapp.com/scanParcel.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);
        scan_btn = (Button) findViewById(R.id.scan_btn);
        send_btn = (Button) findViewById(R.id.send_btn);
        reprint_btn = (Button) findViewById(R.id.reprint_btn);
        conNo = (EditText) findViewById(R.id.conNo);
        textView = (TextView) findViewById(R.id.textView);
        currentDepot = getIntent().getStringExtra("depot");//from intent
        getDepotInfo();
        final Activity activity = this;

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

        reprint_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (conNo.getText().toString().isEmpty()) {
                    conNo.requestFocus();
                    return;
                }

                //pass con number
                Intent rIntent = new Intent(ReaderActivity.this, ReprintLabelActivity.class);
                rIntent.putExtra("conNum",conNo.getText().toString());
                startActivity(rIntent);
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

        conNo.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void afterTextChanged(Editable mEdit)
            {
                String num = conNo.getText().toString();
                getLabelDetails(num);
                //this might fail if wrong con no
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after){}

            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });


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
                //readCode = result.getContents();
                conNo.setText(result.getContents());
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }



    public void ScanToDb(View view)
    {
        if(!conNo.getText().toString().trim().equals("") && !textView.getText().toString().trim().equals("")) {
            //getConInfo("https://easyshippingrh.000webhostapp.com/trackParcel.php?consignment_id=" + conNo.getText().toString());
            getLabelDetails(conNo.getText().toString());
            sendtoDB(conNo.getText().toString());
        }
        else{
            conNo.setError("Please enter a valid consignment number");
            conNo.requestFocus();
        }
    }

    private void getDepotInfo() {

        String url= "https://easyshippingrh.000webhostapp.com/getDepotInfo.php?depot="+currentDepot;
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonarray = new JSONArray(response);
                            for(int i=0; i < jsonarray.length(); i++) {
                                Log.i("tagconvertstr", "["+response+"]");
                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                myCounty = jsonobject.getString("county");
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

    private void sendtoDB(String trackingNum) {
        if(myCounty.equals(parcelDestCounty))
        {
            status = "Ready For Delivery";
            JavaMailAPI javaMailAPI = new JavaMailAPI(this,recEmail,"Your parcel from EasyShipping","Hi, "+recName+"\nYour parcel is "+status+", and will be with you in the morning. If you are going to be at work please enable your location tracker when you get to the address of your workplace.\n\nThe EasyShipping Delivery Team");
            javaMailAPI.execute();

        }
        else if(parcelDestCounty.equals(""))
        {
            status = "In Hub";
        }
        else {
            status = "In Hub";
            JavaMailAPI javaMailAPI = new JavaMailAPI(this,recEmail,"Your parcel from EasyShipping","Hi, "+recName+"\nYour parcel has just been scanned "+status+" at Depot "+currentDepot+", "+myCounty+".\n\nThe EasyShipping Delivery Team");
            javaMailAPI.execute();
        }

        final String latestReport = ": Your parcel has been scanned at Depot " + currentDepot + ", " +myCounty;
        if (conNo.getText().toString().equals("") || trackingNum.equals("") || parcelDestCounty.equals("")) {
            Toast.makeText(this, "Enter a valid Consignment Number", Toast.LENGTH_SHORT).show();
        } else {
            StringRequest request = new StringRequest(Request.Method.POST, mainUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    conNo.setText("");
                    Toast.makeText(ReaderActivity.this, response,Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(ReaderActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String, String>();

                    params.put("consignment_id",trackingNum);
                    params.put("status",status);
                    params.put("latestReport","\n"+date+latestReport);
                    params.put("currentDepot",currentDepot);
                    return params;
                }
            };
            textView.setText(null);
            RequestQueue requestQueue = Volley.newRequestQueue(ReaderActivity.this);
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

                                String name = jsonobject.getString("name").trim();
                                String addL1 = jsonobject.getString("addressline1").trim();
                                String addL2 = jsonobject.getString("addressline2").trim();
                                String addL3 = jsonobject.getString("towncity").trim();
                                String addL4 = jsonobject.getString("countystate").trim();
                                parcelDestCounty = addL4;
                                recName = name;
                                recEmail = jsonobject.getString("email");
                                textView.setText(name+"\n"+addL1+"\n"+addL2+"\n"+addL3+"\n"+addL4);

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
