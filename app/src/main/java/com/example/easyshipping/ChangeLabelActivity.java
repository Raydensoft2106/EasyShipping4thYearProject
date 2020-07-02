package com.example.easyshipping;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangeLabelActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText ConsignmentNo, name, addressLine1, addressLine2, towncity, countystate, postcode, noOfParcels, totalWeight, phoneno, recEmail, additionalNote;
    Spinner country;

    String str_receiver_id = "";

    String str_consignment_id,str_name,str_addressline1,str_addressline2,str_towncity,
            str_countystate,str_country,str_postcode,str_noofparcels,str_totalweight,
            str_phoneno,str_recEmail,str_additionalnote;


    String url = "https://easyshippingrh.000webhostapp.com/changeLabel.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changelabel);

        Spinner spinner = findViewById(R.id.Country);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.countries, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("EasyShipping");

        ConsignmentNo = findViewById(R.id.ConsignmentNo);
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

        str_receiver_id=Integer.toString(getIntent().getIntExtra("userID", 0));//from intent

        ConsignmentNo.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void afterTextChanged(Editable mEdit)
            {
                str_consignment_id = mEdit.toString();
                getConParams(str_consignment_id);
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

    public void ChangeLabel(View view) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");

        if (name.getText().toString().equals("")) {
            Toast.makeText(this, "Enter a Name", Toast.LENGTH_SHORT).show();
            name.setError("Enter a Name");
            name.requestFocus();
        } else if (ConsignmentNo.getText().toString().equals("")) {
            Toast.makeText(this, "Enter a Consignment Number", Toast.LENGTH_SHORT).show();
            ConsignmentNo.setError("Enter a Consignment Number");
            ConsignmentNo.requestFocus();
        } else if (addressLine1.getText().toString().equals("")) {
            Toast.makeText(this, "Enter all Address Fields", Toast.LENGTH_SHORT).show();
            addressLine1.setError("Enter all Address Fields");
            addressLine1.requestFocus();
        } else if (addressLine2.getText().toString().equals("")) {
            Toast.makeText(this, "Enter all Address Fields", Toast.LENGTH_SHORT).show();
            addressLine2.setError("Enter all Address Fields");
            addressLine2.requestFocus();
        } else if (towncity.getText().toString().equals("")) {
            Toast.makeText(this, "Enter all Address Fields", Toast.LENGTH_SHORT).show();
            towncity.setError("Enter all Address Fields");
            towncity.requestFocus();
        } else if (countystate.getText().toString().equals("")) {
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
            str_consignment_id = ConsignmentNo.getText().toString().trim();
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
                    ConsignmentNo.setText("");
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
                    Toast.makeText(ChangeLabelActivity.this, response,Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(ChangeLabelActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String, String>();

                    params.put("consignment_id",str_consignment_id);
                    params.put("receiver_id",str_receiver_id);
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

                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(ChangeLabelActivity.this);
            requestQueue.add(request);
        }
    }

    private void getConParams(String consignment_id) {

        String url= "https://easyshippingrh.000webhostapp.com/trackParcel.php?consignment_id="+consignment_id;
        //pd.show();
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
                                name.setText(jsonobject.getString("name"));
                                addressLine1.setText(jsonobject.getString("addressline1"));
                                addressLine2.setText(jsonobject.getString("addressline2"));
                                towncity.setText(jsonobject.getString("towncity"));
                                countystate.setText(jsonobject.getString("countystate"));
                                postcode.setText(jsonobject.getString("postcode"));
                                phoneno.setText(jsonobject.getString("phoneno"));
                                recEmail.setText(jsonobject.getString("email"));
                                additionalNote.setText(jsonobject.getString("hiddenloc"));                            }
                            //pd.hide();
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