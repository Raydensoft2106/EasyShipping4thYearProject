package com.example.easyshipping;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
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

public class ReprintLabelActivity extends AppCompatActivity {

    EditText TVsendEmail, TVconNo;
    String consignment_id;
    String senderEmail;
    String passedCon;
    String str_name,str_addressline1,str_addressline2,str_towncity,str_countystate,str_country,
            str_postcode,str_noofparcels,str_totalweight,str_phoneno,str_additionalnote;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reprintlabel);
        TVsendEmail = findViewById(R.id.sendEmail);
        TVconNo = findViewById(R.id.conNo);

        passedCon = getIntent().getStringExtra("conNum");//from intent
        //if(!passedCon.isEmpty())
            TVconNo.setText(passedCon);

        pd = new ProgressDialog(ReprintLabelActivity.this);
        pd.setMessage("loading");
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("EasyShipping");
    }

    public void ReprintLabel(View view) throws WriterException, IOException, BadElementException {
        consignment_id = TVconNo.getText().toString().trim();
        senderEmail = TVsendEmail.getText().toString().trim();
        getSqlDetails();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");

        if (TVsendEmail.getText().toString().equals("")) {
            Toast.makeText(this, "Enter an Email", Toast.LENGTH_SHORT).show();
            TVsendEmail.setError("Please enter a valid email");
            TVsendEmail.requestFocus();
        } else if (TVconNo.getText().toString().equals("")) {
            Toast.makeText(this, "Enter a Consignment Number", Toast.LENGTH_SHORT).show();
            TVconNo.setError("Please enter a valid tracking number");
            TVconNo.requestFocus();
        }
    }

    private void getSqlDetails() {

        String url= "https://easyshippingrh.000webhostapp.com/trackParcel.php?consignment_id="+consignment_id;
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonarray = new JSONArray(response);
                            for(int i=0; i < jsonarray.length(); i++) {
                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                str_name = jsonobject.getString("name").trim();
                                str_addressline1 = jsonobject.getString("addressline1").trim();
                                str_addressline2 = jsonobject.getString("addressline2").trim();
                                str_towncity = jsonobject.getString("towncity").trim();
                                str_countystate = jsonobject.getString("countystate").trim();
                                str_country = jsonobject.getString("country").trim();
                                str_postcode = jsonobject.getString("postcode").trim();
                                str_noofparcels = jsonobject.getString("noOfParcels").trim();
                                str_totalweight = jsonobject.getString("totalWeight").trim();
                                str_phoneno = jsonobject.getString("phoneno").trim();
                                str_additionalnote = jsonobject.getString("additionalNote").trim();
                                toPrinter(consignment_id, str_name, str_addressline1, str_addressline2, str_towncity, str_countystate, str_country, str_postcode, str_noofparcels, str_totalweight, str_phoneno, str_additionalnote, senderEmail);
                            }
                        } catch (JSONException | WriterException e) {
                            e.printStackTrace();
                            TVconNo.setError("Please enter a valid tracking number");
                            TVconNo.requestFocus();
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

    public void toPrinter(String parcelNo, String name, String add1, String add2, String tc, String cs, String nat, String pc, String nop, String weight, String num, String note, String email) throws WriterException {
        //Generate QRCode
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        BitMatrix bitMatrix = multiFormatWriter.encode(consignment_id, BarcodeFormat.QR_CODE,300,300);
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);

        TVconNo.setText("");
        TVsendEmail.setText("");

        Intent rIntent = new Intent(ReprintLabelActivity.this, PrintActivity.class);
        Bundle extras = new Bundle();
        extras.putString("str_conNo", parcelNo);
        extras.putString("str_name", name);
        extras.putString("str_addressline1", add1);
        extras.putString("str_addressline2", add2);
        extras.putString("str_towncity", tc);
        extras.putString("str_countystate", cs);
        extras.putString("str_country", nat);
        extras.putString("str_postcode", pc);
        extras.putString("str_noofparcels", nop);
        extras.putString("str_totalweight", weight);
        extras.putString("str_phoneno", num);
        extras.putString("str_additionalnote", note);
        extras.putParcelable("qrcode", bitmap);
        extras.putString("email",email);
        rIntent.putExtras(extras);
        startActivity(rIntent);
    }
}
