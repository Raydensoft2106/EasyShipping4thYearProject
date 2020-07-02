package com.example.easyshipping;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.easyshipping.Model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;

public class TrackParcelActivity extends AppCompatActivity {
    //test
    TextView result;
    EditText conNo;
    Button TrackBtn;
    String consignment_id;
    int userID;//from intent
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trackparcel);
        TrackBtn = (Button)findViewById(R.id.TrackBtn);
        result = (TextView)findViewById(R.id.textView);
        conNo = (EditText)findViewById(R.id.conNo);

        //from intent
        userID=getIntent().getIntExtra("userID", 0);

        pd = new ProgressDialog(TrackParcelActivity.this);
        pd.setMessage("loading");
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("EasyShipping");


    TrackBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            result.setText("");
            consignment_id = conNo.getText().toString().trim();
            if(consignment_id.length()!=0)
                getSqlDetails("https://easyshippingrh.000webhostapp.com/trackParcel.php?consignment_id="+consignment_id);
            else
                conNo.setError("Please enter a valid tracking number");
                conNo.requestFocus();
        }
    });


}

    public void getSqlDetails(String url) {
        //String url= "https://easyshippingrh.000webhostapp.com/trackParcel.php?consignment_id="+consignment_id;
        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.hide();
                        try {
                            JSONArray jsonarray = new JSONArray(response);
                            for(int i=0; i < jsonarray.length(); i++) {
                                JSONObject jsonobject = jsonarray.getJSONObject(i);

                                String id = jsonobject.getString("consignment_id");
                                String lastReport = jsonobject.getString("latestReport").trim();
                                String status = jsonobject.getString("status").trim();
                                String dispatchdate = jsonobject.getString("dispatchdate").trim();

                                //Split up latest report by date or by \n here
                                // split by new line
                                int count = 1;
                                String[] lines = lastReport.split("\\r?\\n");

                                result.setText("Parcel Number: "+id+"\nTracking Log:\n"+ Arrays.toString(lines)+"\nStatus: "+status+"\nDispatch Date: "+dispatchdate);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            conNo.setError("Please enter a valid tracking number");
                            conNo.requestFocus();
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

