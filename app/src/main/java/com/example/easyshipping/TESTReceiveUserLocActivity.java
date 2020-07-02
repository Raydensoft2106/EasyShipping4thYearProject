package com.example.easyshipping;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * 1. Get delivery list (as driver)
 * 2. Get receiver email, then use this to get the email in the Locations Table and the LatLng
 * 3. Then get their LatLng from the other table
 * 4. if(custLoc LatLng = a certain distance from the adress LatLng)
 *      {
 *          Prompt: would you like to change the marker plotline
 *          Or would you like to phone the customer
 *          Dismiss Button
 *      }
 *
 *
 *      NOTE: CODE WILL ONLY FIRE IF RECEIVER_ID != 0!!!!!!!
 */

class TESTReceiveUserLocActivity extends AppCompatActivity {
    private ProgressDialog pd;
    private TextView recEmail;
    private TextView recLoc;
    private TextView addLoc;
    private RequestQueue mQueue;

    int userID;
    String depot, str_recid, str_recName, str_conNo, str_recEmail = "", str_phoneNo, str_userLatLng, str_addLatLng;
    private String encEmail;

    public double tempLat = 0;
    public double tempLong = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiverloc);
        pd = new ProgressDialog(TESTReceiveUserLocActivity.this);
        pd.setMessage("loading");
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);

        userID=getIntent().getIntExtra("userID", 0);//from intent
        depot=getIntent().getStringExtra("depot");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle("EasyShipping");

        recEmail = findViewById(R.id.tv_recEmail);
        recLoc = findViewById(R.id.tv_recLoc);
        addLoc = findViewById(R.id.tv_addressLoc);

        mQueue = Volley.newRequestQueue(this);
        downloadJSON("https://easyshippingrh.000webhostapp.com/listDeliveries.php?currentDepot="+depot);

    }


    private void downloadJSON(final String urlWebService) {

        class DownloadJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                try {
                    loadIntoListView(s);//set the textview values here
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                //pd.show();
                try {
                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }
            }
        }
        DownloadJSON getJSON = new DownloadJSON();
        getJSON.execute();
    }

    private void loadIntoListView(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        //Log.i("tagconvertstr", "["+response+"]");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            str_recid = obj.getString("receiver_id");
            str_recEmail = obj.getString("email").trim();
            str_conNo = "Consignment Number: " + obj.getString( "consignment_id");
            str_recName = "Receiver Name: " + obj.getString("name");
            str_phoneNo = obj.getString("phoneno");

            ArrayList<String> addressArrList = new ArrayList<String>();
            String add1 = obj.getString("addressline1").trim();
            String add2 = obj.getString("addressline2").trim();
            String add3 = obj.getString("towncity").trim();
            String add4 = obj.getString("countystate").trim();

            addressArrList.add(add1);
            addressArrList.add(add2);
            addressArrList.add(add3);
            addressArrList.add(add4);

            StringBuffer sb = new StringBuffer();
            for (int j = 0; j < addressArrList.size(); j++) {
                sb.append(" " + addressArrList.get(j));
            }
            String fulladr = sb.toString();
            new TESTReceiveUserLocActivity.GetCoordinates().execute(fulladr.replace(" ", "+"));
        }
        if(Integer.parseInt(str_recid) != 0)
        {
            encodeUserEmail(str_recEmail);
            System.out.println(encEmail);
            getReceiverLoc();
        } else {
            //
        }
    }

    public void encodeUserEmail(String userEmail) {
        encEmail = userEmail.replace(".", ",");
    }

    private void getReceiverLoc() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Query query = reference.child("UserLocs").child(encEmail);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    str_userLatLng = dataSnapshot.child("userLatLng").getValue().toString();
                    // dataSnapshot is the "UserLocs" node with all children with email str_recEmail
                    //prompt();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void prompt(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(TESTReceiveUserLocActivity.this);
        dialog.setTitle("Before you proceed...");
        dialog.setMessage("This customer is not at home, would you like to phone them?");
        dialog.setCancelable(true);


        dialog.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int
                            id) {
                        // Phone the phone number
                        dialContactPhone(str_phoneNo);

                    }
                });

// A null listener allows the button to dismiss the dialog and take no further action.

        dialog.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dialog.cancel();
                    }
                });

        dialog.setNeutralButton("Just direct me to them", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {

                //function to redraw plotline on map

            }});

        AlertDialog alert = dialog.create();
        alert.show();
        //pd.dismiss();
    }

    private void dialContactPhone(final String phoneNumber) {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
    }

    private class GetCoordinates extends AsyncTask<String,Void,String> {
        @Override
        protected void onPreExecute() {
            Log.i("hello:", "Thread here");
        }

        @Override
        protected String doInBackground(String... strings) {
            String response;
            try {
                String address = strings[0];
                HttpDataHandler http = new HttpDataHandler();
                String url = String.format("https://maps.googleapis.com/maps/api/geocode/json?address=%s", address + "&key=" + getString(R.string.google_maps_api));
                response = http.getHTTPData(url);
                return response;
            } catch (Exception ex) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                String lat = ((JSONArray) jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry")
                        .getJSONObject("location").get("lat").toString();
                String lng = ((JSONArray) jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry")
                        .getJSONObject("location").get("lng").toString();

                tempLat = Double.parseDouble(lat);
                tempLong = Double.parseDouble(lng);

                str_addLatLng = (tempLat + ", " + tempLong);

                recEmail.append(str_recEmail);
                recLoc.append(str_userLatLng);
                addLoc.append(str_addLatLng);

                //if latlng of address is different from/not similar to latlng of
                //prompt
                String[] addresslatlong =  str_addLatLng.split(", ");
                double latitude1 = Double.parseDouble(addresslatlong[0]);
                double longitude1 = Double.parseDouble(addresslatlong[1]);
                //To constructs a LatLng with the given latitude and longitude coordinates

                String[] userlatlong =  str_userLatLng.split(", ");
                double latitude2 = Double.parseDouble(userlatlong[0]);
                double longitude2 = Double.parseDouble(userlatlong[1]);

                Location locationA = new Location("point A");
                locationA.setLatitude(latitude1);
                locationA.setLongitude(longitude1);

                Location locationB = new Location("point B");
                locationB.setLatitude(latitude2);
                locationB.setLongitude(longitude2);

                double distance = locationA.distanceTo(locationB); //in meters
                if(distance > 500)
                {
                    prompt();
                }
                pd.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}

