package com.example.easyshipping;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.itextpdf.text.BadElementException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ReturnParcelActivity extends AppCompatActivity {

    Random r = new Random();
    int randId;
    EditText conNo;
    Button reprintbtn, ScanBtn;
    String consignment_id;
    private String str_senderid,str_receiverid,str_fName,str_lName,str_addressline1,str_addressline2,str_towncity,str_countystate,str_country,
            str_postcode,str_noofparcels,str_totalweight,str_phoneno, str_email;

    private ProgressDialog pd;
    private int scanSound;
    private SoundPool soundPool;
    String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
    String url = "https://easyshippingrh.000webhostapp.com/createLabel.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_returnparcel);
        conNo = findViewById(R.id.conNo);
        ScanBtn = findViewById(R.id.ScanBtn);
        reprintbtn = findViewById(R.id.reprintbtn);
        final Activity activity = this;
        pd = new ProgressDialog(ReturnParcelActivity.this);
        pd.setMessage("loading");
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("EasyShipping");

        ScanBtn.setOnClickListener(new View.OnClickListener() {
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
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void ReturnLabel(View view)
    {
        getConSender(conNo.getText().toString().trim());
    }

    public void ReturnParcel() throws WriterException, IOException, BadElementException {
        //final ProgressDialog progressDialog = new ProgressDialog(this);
        //progressDialog.setMessage("Please Wait...");

        if (conNo.getText().toString().trim().equals("")) {
            Toast.makeText(this, "Enter a Consignment Number", Toast.LENGTH_SHORT).show();
        }

        randId = r.nextInt(999999-100000) + 100000;
        consignment_id = Integer.toString(randId);

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(ReturnParcelActivity.this, response,Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //progressDialog.dismiss();
                Toast.makeText(ReturnParcelActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        }

        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();

                params.put("consignment_id", consignment_id);
                params.put("sender_id",str_senderid);
                params.put("receiver_id",str_receiverid);
                params.put("currentDepot","0");
                params.put("name",str_fName + " " + str_lName);
                params.put("addressline1",str_addressline1);
                params.put("addressline2",str_addressline2);
                params.put("towncity",str_towncity);
                params.put("countystate",str_countystate);
                params.put("country",str_country);
                params.put("postcode",str_postcode);
                params.put("noOfParcels",str_noofparcels);
                params.put("totalWeight",str_totalweight);
                params.put("phoneno",str_phoneno);
                params.put("email",str_email);
                params.put("additionalNote","RETURN TO SENDER");
                params.put("status","Dispatched");
                params.put("latestReport",date+": This parcel is being returned by the recipient");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(ReturnParcelActivity.this);
        requestQueue.add(request);


        JavaMailAPI javaMailAPI = new JavaMailAPI(this,str_email,"RETURN: Your parcel from EasyShipping","Hi, "+str_fName + " " + str_lName+"\n\nThe parcel "+conNo.getText().toString().trim()+" you sent is on the way back to you, as the receiver has returned it. You can track this parcel with the" +
                " tracking number: "+consignment_id+".\n\nThe EasyShipping Delivery Team");
        javaMailAPI.execute();

        conNo.setText("");

        //Generate QRCode
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        BitMatrix bitMatrix = multiFormatWriter.encode(consignment_id, BarcodeFormat.QR_CODE,300,300);
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);

        Intent rIntent = new Intent(ReturnParcelActivity.this, PrintActivity.class);
        Bundle extras = new Bundle();
        extras.putString("str_conNo", consignment_id);
        extras.putString("str_name", str_fName + " " + str_lName);
        extras.putString("str_addressline1", str_addressline1);
        extras.putString("str_addressline2", str_addressline2);
        extras.putString("str_towncity", str_towncity);
        extras.putString("str_countystate", str_countystate);
        extras.putString("str_country", str_country);
        extras.putString("str_postcode", str_postcode);
        extras.putString("str_noofparcels", str_noofparcels);
        extras.putString("str_totalweight", str_totalweight);
        extras.putString("str_phoneno", str_phoneno);
        extras.putString("str_additionalnote", "RETURN TO SENDER");
        extras.putParcelable("qrcode", bitmap);
        extras.putString("email",str_email);
        rIntent.putExtras(extras);
        startActivity(rIntent);

    }

    private void getConSender(String con){
        String url= "https://easyshippingrh.000webhostapp.com/trackParcel.php?consignment_id="+con;
        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonarray = new JSONArray(response);
                            for(int i=0; i < jsonarray.length(); i++) {
                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                str_senderid = jsonobject.getString("sender_id").trim();
                                str_receiverid = jsonobject.getString("receiver_id").trim();
                                str_noofparcels = jsonobject.getString("noOfParcels").trim();
                                str_totalweight = jsonobject.getString("totalWeight").trim();
                            }
                            //pd.hide();
                            getSenderDetails();
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

    private void getSenderDetails(){
        String url= "https://easyshippingrh.000webhostapp.com/ReturnParcel.php?sender_id="+str_senderid;
        //pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonarray = new JSONArray(response);
                            for(int i=0; i < jsonarray.length(); i++) {
                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                str_fName = jsonobject.getString("fName").trim();
                                str_lName = jsonobject.getString("lName").trim();
                                str_email = jsonobject.getString("email").trim();
                                str_addressline1 = jsonobject.getString("addressline1").trim();
                                str_addressline2 = jsonobject.getString("addressline2").trim();
                                str_towncity = jsonobject.getString("towncity").trim();
                                str_countystate = jsonobject.getString("countystate").trim();
                                str_country = jsonobject.getString("country").trim();
                                str_phoneno = jsonobject.getString("phoneno").trim();
                                str_postcode = jsonobject.getString("postcode").trim();
                            }
                            pd.hide();
                            ReturnParcel();
                        } catch (JSONException | WriterException | IOException | BadElementException e) {
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