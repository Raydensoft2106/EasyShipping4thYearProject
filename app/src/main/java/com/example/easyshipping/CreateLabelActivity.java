package com.example.easyshipping;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
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

public class CreateLabelActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //private static final int SEND_SMS_CODE = 23;

    EditText customerID, name, addressLine1, addressLine2, towncity, countystate, postcode, noOfParcels, totalWeight, phoneno, recEmail, additionalNote;
    Spinner country;

    Random r = new Random();
    int randId;
    String str_consignment_id;
    String str_sender_id;
    String custID;
    String str_currentdep = "0";

    public String str_receiver_id, str_name,str_addressline1,str_addressline2,str_towncity,
            str_countystate,str_country,str_postcode,str_noofparcels,str_totalweight,
            str_phoneno, str_recEmail, str_additionalnote;

    String str_status = "Dispatched";
    String str_latestReport = ": Your parcel has been dispatched by the sender";
    String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
    String url = "https://easyshippingrh.000webhostapp.com/createLabel.php";
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createlabel);

        Spinner spinner = findViewById(R.id.Country);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.countries, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        int userID=getIntent().getIntExtra("userID", 0);//from intent
        email = getIntent().getStringExtra("email");
        str_sender_id = Integer.toString(userID);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("EasyShipping");

        customerID = findViewById(R.id.customerID);
        name = findViewById(R.id.Name);
        addressLine1 = findViewById(R.id.AddressLine1);
        addressLine2 = findViewById(R.id.AddressLine2);
        towncity = findViewById(R.id.TownCity);
        countystate = findViewById(R.id.CountyState);
        country = findViewById(R.id.Country);
        postcode = findViewById(R.id.Postcode);
        noOfParcels = findViewById(R.id.NoOfParcels);
        totalWeight = findViewById(R.id.TotalWeight);
        phoneno = findViewById(R.id.PhoneNo);
        recEmail = findViewById(R.id.recEmail);
        additionalNote = findViewById(R.id.AddNote);

        customerID.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void afterTextChanged(Editable mEdit)
            {
                custID = mEdit.toString();
                getRecParams(custID);
                //if recid is 0 this will fail but it's not a show stopper bug
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after){}

            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //We take the item at this position, turn it into a string and save it to text variable
        String text = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(adapterView.getContext(), text, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void CreateLabel(View view) throws WriterException, IOException, BadElementException {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");

        if (name.getText().toString().equals("")) {
            Toast.makeText(this, "Enter a Name", Toast.LENGTH_SHORT).show();
            name.setError("Enter a Name");
            name.requestFocus();
        } else if (customerID.getText().toString().equals("")) {
            Toast.makeText(this, "Enter a Receiver ID, or 0", Toast.LENGTH_SHORT).show();
            customerID.setError("Enter a Receiver ID");
            customerID.requestFocus();
        } else if (addressLine1.getText().toString().equals("") || addressLine1.getText().toString().length() < 3) {
            Toast.makeText(this, "Enter all Address Fields", Toast.LENGTH_SHORT).show();
            addressLine1.setError("Enter all Address Fields");
            addressLine1.requestFocus();
        } else if (addressLine2.getText().toString().equals("") || addressLine2.getText().toString().length() < 3) {
            Toast.makeText(this, "Enter all Address Fields", Toast.LENGTH_SHORT).show();
            addressLine2.setError("Enter all Address Fields");
            addressLine2.requestFocus();
        } else if (towncity.getText().toString().equals("") || towncity.getText().toString().length() < 3) {
            Toast.makeText(this, "Enter all Address Fields", Toast.LENGTH_SHORT).show();
            towncity.setError("Enter all Address Fields");
            towncity.requestFocus();
        } else if (countystate.getText().toString().equals("") || countystate.getText().toString().length() < 3) {
            Toast.makeText(this, "Enter all Address Fields", Toast.LENGTH_SHORT).show();
            countystate.setError("Enter all Address Fields");
            countystate.requestFocus();
        } else if (country.getSelectedItem().toString().equals("")) {
            Toast.makeText(this, "Enter all Address Fields", Toast.LENGTH_SHORT).show();
        } else if (postcode.getText().toString().equals("")) {
            Toast.makeText(this, "Enter all Address Fields", Toast.LENGTH_SHORT).show();
            postcode.setError("Enter all Address Fields");
            postcode.requestFocus();
        } else if (noOfParcels.getText().toString().equals("")) {
            Toast.makeText(this, "Enter all Parcel Fields", Toast.LENGTH_SHORT).show();
            noOfParcels.setError("Enter all Parccel Fields");
            noOfParcels.requestFocus();
        } else if (totalWeight.getText().toString().equals("")) {
            totalWeight.setError("Enter all Parcel Fields");
            totalWeight.requestFocus();
            Toast.makeText(this, "Enter all Parcel Fields", Toast.LENGTH_SHORT).show();
        } else if (phoneno.getText().toString().equals("")) {
            phoneno.setError("Enter a Phone Number");
            phoneno.requestFocus();
            Toast.makeText(this, "Enter a phone number", Toast.LENGTH_SHORT).show();
        } else if (recEmail.getText().toString().equals("")) {
            recEmail.setError("Enter an email");
            recEmail.requestFocus();
            Toast.makeText(this, "Enter an Email", Toast.LENGTH_SHORT).show();
        } else if (additionalNote.getText().toString().equals("")) {
            additionalNote.setError("Enter an Additional  Note");
            additionalNote.requestFocus();
            Toast.makeText(this, "Enter an Additional  Note", Toast.LENGTH_SHORT).show();
        }


        else{
            progressDialog.show();
            randId = r.nextInt(999999-100000) + 100000;
            str_consignment_id = Integer.toString(randId);
            str_receiver_id = customerID.getText().toString().trim();
            str_name = name.getText().toString().trim();
            str_addressline1 = addressLine1.getText().toString().trim();
            str_addressline2 = addressLine2.getText().toString().trim();
            str_towncity = towncity.getText().toString().trim();
            str_countystate = countystate.getText().toString().trim();
            str_country = country.getSelectedItem().toString().trim();
            str_postcode = postcode.getText().toString().trim();
            str_noofparcels = noOfParcels.getText().toString().trim();
            str_totalweight = totalWeight.getText().toString().trim();
            str_phoneno = phoneno.getText().toString().trim();
            str_recEmail = recEmail.getText().toString().trim();
            str_additionalnote = additionalNote.getText().toString().trim();

            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.dismiss();
                    customerID.setText("");
                    name.setText("");
                    addressLine1.setText("");
                    addressLine2.setText("");
                    towncity.setText("");
                    countystate.setText("");
                    postcode.setText("");
                    noOfParcels.setText("");
                    totalWeight.setText("");
                    phoneno.setText("");
                    recEmail.setText("");
                    additionalNote.setText("");
                    Toast.makeText(CreateLabelActivity.this, response,Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(CreateLabelActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String, String>();

                    params.put("consignment_id", str_consignment_id);
                    params.put("sender_id",str_sender_id);
                    params.put("receiver_id",str_receiver_id);
                    params.put("currentDepot",str_currentdep);
                    params.put("name",str_name);
                    params.put("addressline1",str_addressline1);
                    params.put("addressline2",str_addressline2);
                    params.put("towncity",str_towncity);
                    params.put("countystate",str_countystate);
                    params.put("country",str_country);
                    params.put("postcode",str_postcode);
                    params.put("noOfParcels",str_noofparcels);
                    params.put("totalWeight",str_totalweight);
                    params.put("phoneno",str_phoneno);
                    params.put("email",str_recEmail);
                    params.put("additionalNote",str_additionalnote);
                    params.put("status",str_status);
                    params.put("latestReport",date+str_latestReport);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(CreateLabelActivity.this);
            requestQueue.add(request);


            JavaMailAPI javaMailAPI = new JavaMailAPI(this,str_recEmail,"Your parcel from EasyShipping","Hi, "+str_name+"\n\nYour parcel has just been dispatched from the sender, you can track it using the" +
                    " tracking number: "+str_consignment_id+".\n\nThe EasyShipping Delivery Team");
            javaMailAPI.execute();

            //Generate QRCode
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            BitMatrix bitMatrix = multiFormatWriter.encode(str_consignment_id, BarcodeFormat.QR_CODE,300,300);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);

            Intent rIntent = new Intent(CreateLabelActivity.this, PrintActivity.class);
            Bundle extras = new Bundle();
            extras.putString("str_conNo", str_consignment_id);
            extras.putString("str_name", str_name);
            extras.putString("str_addressline1", str_addressline1);
            extras.putString("str_addressline2", str_addressline2);
            extras.putString("str_towncity", str_towncity);
            extras.putString("str_countystate", str_countystate);
            extras.putString("str_country", str_country);
            extras.putString("str_postcode", str_postcode);
            extras.putString("str_noofparcels", str_noofparcels);
            extras.putString("str_totalweight", str_totalweight);
            extras.putString("str_phoneno", str_phoneno);
            extras.putString("str_additionalnote", str_additionalnote);
            extras.putString("str_status", str_status);
            extras.putString("str_latestReport", str_latestReport);
            extras.putString("str_consignment_id", str_consignment_id);
            extras.putParcelable("qrcode", bitmap);
            extras.putString("email",email);
            rIntent.putExtras(extras);
            startActivity(rIntent);
        }
    }

    private void getRecParams(String id) {
        String url= "https://easyshippingrh.000webhostapp.com/SelectCustAddress.php?custID="+id;
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
                                name.setText(jsonobject.getString("fName") +" "+jsonobject.getString("lName"));
                                addressLine1.setText(jsonobject.getString("addressline1"));
                                addressLine2.setText(jsonobject.getString("addressline2"));
                                towncity.setText(jsonobject.getString("towncity"));
                                countystate.setText(jsonobject.getString("countystate"));
                                postcode.setText(jsonobject.getString("postcode"));
                                phoneno.setText(jsonobject.getString("phoneno"));
                                recEmail.setText(jsonobject.getString("email"));
                                additionalNote.setText(jsonobject.getString("hiddenloc"));
                                //userID = Integer.parseInt(recid);
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