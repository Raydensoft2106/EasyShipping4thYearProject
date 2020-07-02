package com.example.easyshipping;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.skyfishjy.library.RippleBackground;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class RouteMapActivity extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private PlacesClient placesClient;

    private Location mLastKnownLocation;
    private LocationCallback locationCallback;

    private View mapView;
    private Button btnNext, btnScan, btnExit;
    private TextView deliveryCount;
    private RippleBackground rippleBg;
    private MarkerOptions place1, place2, myPlace, destination, custPlace;
    private MarkerOptions points = new MarkerOptions();
    private Polyline currentPolyline;

    public double myLat = 53.4178;
    public double myLng = -7.9080;
    private final float DEFAULT_ZOOM = 15;

    public double tempLat = 0;
    public double tempLong = 0;

    private ProgressDialog pd;
    Map<String, String> myAddressesUnsorted = new LinkedHashMap<String, String>();
    public ArrayList<LatLng> deliveriesLatLngs = new ArrayList<>();
    public LatLng latLng;

    public ArrayList<String> recipientNames = new ArrayList<>();
    Map<LatLng, String> myAddressesUnsGeocodes = new LinkedHashMap<LatLng, String>();
    String name;
    int counter = 0;

    public Map<LatLng, Integer> locationsDistance=new LinkedHashMap<LatLng, Integer>();
    public TreeMap<String, Integer> namesDistance=new TreeMap<String, Integer>();
    LinkedHashMap<String, Integer> sortedAddresses = new LinkedHashMap<>();

    private int userID;//from intent
    private String depot;
    private String driverEmail;
    private String DriverName;
    private String str_recEmail = "";
    private String encEmail;
    private String str_phoneNo;
    private String str_addLatLng;
    private String str_custLatLng = "";

    String CurrentReceiverID;
    String CurrentConsignmentNum;
    String CurrentNearestCustName;

    public RouteMapActivity() {
    }

    /**
     * <p>
     * Steps should be as follows:
     * <p>
     * 1. onCreate, driverLocation gotten*
     * 2. Delivery list pulled from db and stored into a list or array or map, the addr -> latLng is run against each address
     * and each latLng is also added to the list/array/map
     * 3. This list is then used to create a list of latLngs
     * 4. LatLngs is sorted according to the closest address to the user
     * 5. The destination marker is set to the first LatLng
     * 6. The strings (email, recID, etc) are then equalled to the instance in Delivery list where the LatLng is
     * equal to the LatLng at the top of the LatLngs list
     * 7. If recNo is not 0, run the tracking code and prompt
     * 8. On scan the first index of both lists is deleted before getting the new first index
     * 9. Destination marker is set
     *
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routemap);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        //driver details
        userID = extras.getInt("userID", 0);//from intent
        depot = extras.getString("depot");
        driverEmail = extras.getString("driverEmail");
        DriverName = extras.getString("DriverName");

        //buttons
        btnNext = findViewById(R.id.btn_next);
        btnScan = findViewById(R.id.btn_scan);
        btnExit = findViewById(R.id.btn_exit);
        rippleBg = findViewById(R.id.ripple_bg);

        //counter
        deliveryCount = findViewById(R.id.delcount);

        //map object
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapView = mapFragment.getView();

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(RouteMapActivity.this);
        Places.initialize(RouteMapActivity.this, getString(R.string.google_maps_api));
        placesClient = Places.createClient(this);
        final AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deliveryCount.append(Integer.toString(counter));
                /** Step 5 **/
                if(!CurrentReceiverID.equals("0")) {
                    getReceiverLoc();
                }

                LatLng deliveryRunDestinationLatLng = deliveriesLatLngs.get(0);
                /** method for Step 6 must go here, then that takes us to step 7 **/
//                if(sortedMap.size() == myAddressesUnsorted.size()) {
//                    setStrings();
//                }

                destination = new MarkerOptions();
                destination.position(deliveryRunDestinationLatLng);
                destination.title("Delivery Address");
                destination.snippet(CurrentNearestCustName + ", " + CurrentConsignmentNum);
                mMap.addMarker(destination);

                myLat = mLastKnownLocation.getLatitude();
                myLng = mLastKnownLocation.getLongitude();
                //Toast.makeText(RouteMapActivity.this, "lat: " + myLat + "lng: " + myLng, Toast.LENGTH_SHORT).show();
                myPlace = new MarkerOptions().position(new LatLng(myLat, myLng)).title("myPlace");
                //new FetchURL(RouteMapActivity.this).execute(getUrl(myPlace.getPosition(), place2.getPosition(), "driving"), "driving");
                deliveriesLatLngs.remove(destination.getPosition());
                //test
//                int numOfPoints = 1;
                for (LatLng point : deliveriesLatLngs) {
                    points.position(point);
                    points.title("Delivery");
                    points.snippet("address");
                    mMap.addMarker(points);
                }
                new FetchURL(RouteMapActivity.this).execute(getUrl(myPlace.getPosition(), destination.getPosition(), "driving"), "driving");

            }
        });

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gIntent = new Intent(RouteMapActivity.this, DriverReaderActivity.class);
                gIntent.putExtra("userID", userID);
                gIntent.putExtra("depot", depot);
                gIntent.putExtra("recEmail", str_recEmail);
                gIntent.putExtra("DriverName", DriverName);
                gIntent.putExtra("driverEmail", driverEmail);
                startActivity(gIntent);
                finish();
                //implement auto fill for con id
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gIntent = new Intent(RouteMapActivity.this, DriverEmpHomeActivity.class);
                gIntent.putExtra("userID", userID);
                gIntent.putExtra("depot",depot);
                gIntent.putExtra("email", driverEmail);
                gIntent.putExtra("DriverName", DriverName);
                startActivity(gIntent);
                finish(); //kills activity so it can't be accessed from back button
            }
        });
        pd = new ProgressDialog(RouteMapActivity.this);
        pd.setMessage("loading");
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_api);
        return url;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        //mMap.addMarker(place1);
        //mMap.addMarker(place2);

        if (mapView != null && mapView.findViewById(Integer.parseInt("1")) != null) {
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 40, 180);
        }

        //check if gps is enabled or not and then request user to enable it
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        SettingsClient settingsClient = LocationServices.getSettingsClient(RouteMapActivity.this);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());

        task.addOnSuccessListener(RouteMapActivity.this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                getDeviceLocation();
            }
        });

        task.addOnFailureListener(RouteMapActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    try {
                        resolvable.startResolutionForResult(RouteMapActivity.this, 51);
                    } catch (IntentSender.SendIntentException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 51) {
            if (resultCode == RESULT_OK) {
                getDeviceLocation();
            }
        }
    }

    /** Step 1 **/
    @SuppressLint("MissingPermission")
    private void getDeviceLocation() {
        mFusedLocationProviderClient.getLastLocation()
                .addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            mLastKnownLocation = task.getResult();
                            if (mLastKnownLocation != null) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            } else {
                                final LocationRequest locationRequest = LocationRequest.create();
                                locationRequest.setInterval(10000);
                                locationRequest.setFastestInterval(5000);
                                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                                locationCallback = new LocationCallback() {
                                    @Override
                                    public void onLocationResult(LocationResult locationResult) {
                                        super.onLocationResult(locationResult);
                                        if (locationResult == null) {
                                            return;
                                        }
                                        mLastKnownLocation = locationResult.getLastLocation();
                                        latLng = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                                        mFusedLocationProviderClient.removeLocationUpdates(locationCallback);
                                    }
                                };
                                mFusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);

                            }
                        } else {
                            Toast.makeText(RouteMapActivity.this, "unable to get last location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        try {
            getDeliveries("https://easyshippingrh.000webhostapp.com/listDeliveries.php?currentDepot=" + depot);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }

    /** Step 2 **/
    private void getDeliveries(String url) throws InterruptedException {
        //String url = "https://easyshippingrh.000webhostapp.com/listDeliveries.php?currentDepot=" + depot;
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url,
                response -> {

//                        pd.hide();
                    try {
                        JSONArray jsonarray = new JSONArray(response);
                        //String[] deliveriesLatLngs = new String[jsonarray.length()];
                        for (int i = 0; i < jsonarray.length(); i++) {

                            JSONObject jsonobject = jsonarray.getJSONObject(i);

                            ArrayList<String> addressArrList = new ArrayList<>();
                            String key = jsonobject.getString("name");
                            String recNo = jsonobject.getString("receiver_id").trim();
                            String conNo = jsonobject.getString("consignment_id").trim();
                            String email = jsonobject.getString("email").trim();
                            String number = jsonobject.getString("phoneno").trim();
                            String add1 = jsonobject.getString("addressline1").trim();
                            String add2 = jsonobject.getString("addressline2").trim();
                            String add3 = jsonobject.getString("towncity").trim();
                            String add4 = jsonobject.getString("countystate").trim();

                            addressArrList.add(recNo);
                            addressArrList.add(conNo);
                            addressArrList.add(email);
                            addressArrList.add(number);
                            addressArrList.add(add1);
                            addressArrList.add(add2);
                            addressArrList.add(add3);
                            addressArrList.add(add4);
                            recipientNames.add(key);

                            StringBuffer sb = new StringBuffer();
                            for (int j = 0; j < addressArrList.size(); j++) {
                                sb.append("+" + addressArrList.get(j));
                            }
                            String fulladr = sb.toString();
                            if (!myAddressesUnsorted.containsKey(key)) {
                                myAddressesUnsorted.put(key, fulladr);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    pd.hide();
                    printMap(myAddressesUnsorted);
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error != null) {

                            Toast.makeText(getApplicationContext(), "Something went wrong.", Toast.LENGTH_LONG).show();
                        }
                    }
                }

        );

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public void encodeUserEmail(String userEmail) {
        encEmail = userEmail.replace(".", ",");
    }

    public void printMap(Map mp) {
        Iterator it = mp.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            new GetCoordinates().execute(pair.getValue().toString());
            //it.remove(); // avoids a ConcurrentModificationException
        }
    }

    private void getReceiverLoc() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Query query = reference.child("UserLocs").child(encEmail);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    str_custLatLng = dataSnapshot.child("userLatLng").getValue().toString();
                    // dataSnapshot is the "UserLocs" node with all children with email str_recEmail
                    /** Step 7 **/
                    if (str_custLatLng.length() != 0) {
                        //if latlng of address is different from/not similar to latlng of customer
                        //prompt
                        String[] addresslatlong = str_addLatLng.split(", ");
                        double latitude1 = Double.parseDouble(addresslatlong[0]);
                        double longitude1 = Double.parseDouble(addresslatlong[1]);
                        //To constructs a LatLng with the given latitude and longitude coordinates


                        String[] custlatlong = str_custLatLng.split(", ");
                        double latitude2 = Double.parseDouble(custlatlong[0]);
                        double longitude2 = Double.parseDouble(custlatlong[1]);

                        Location locationA = new Location("point A");
                        locationA.setLatitude(latitude1);
                        locationA.setLongitude(longitude1);

                        Location locationB = new Location("point B");
                        locationB.setLatitude(latitude2);
                        locationB.setLongitude(longitude2);

                        double distance = locationA.distanceTo(locationB); //in meters
                        if (distance > 500) {
                            prompt();
                        }
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void dialContactPhone(final String phoneNumber) {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
    }

    public void prompt() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(RouteMapActivity.this);
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
                String[] custlatlong = str_custLatLng.split(", ");
                double custLat = Double.parseDouble(custlatlong[0]);
                double custLng = Double.parseDouble(custlatlong[1]);
                custPlace = new MarkerOptions().position(new LatLng(custLat, custLng)).title("Customer Location");
                mMap.addMarker(custPlace);

                myLat = mLastKnownLocation.getLatitude();
                myLng = mLastKnownLocation.getLongitude();
                //Toast.makeText(RouteMapActivity.this, "lat: " + myLat + "lng: " + myLng, Toast.LENGTH_SHORT).show();
                myPlace = new MarkerOptions().position(new LatLng(myLat, myLng)).title("myPlace");
                new FetchURL(RouteMapActivity.this).execute(getUrl(myPlace.getPosition(), custPlace.getPosition(), "driving"), "driving");
            }
        });

        AlertDialog alert = dialog.create();
        alert.show();
        //pd.dismiss();
    }

    public double distanceFrom(double fromLat, double fromLon, double toLat, double toLon) {
        double radius = 6378137;   // approximate Earth radius, *in meters*
        double deltaLat = toLat - fromLat;
        double deltaLon = toLon - fromLon;
        double angle = 2 * Math.asin(Math.sqrt(
                Math.pow(Math.sin(deltaLat / 2), 2) +
                        Math.cos(fromLat) * Math.cos(toLat) *
                                Math.pow(Math.sin(deltaLon / 2), 2)));
        return radius * angle;
    }

    private class GetCoordinates extends AsyncTask<String, Void, String> {
        ProgressDialog dialog = new ProgressDialog(RouteMapActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Please wait....");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
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
                deliveriesLatLngs.add(new LatLng(tempLat, tempLong));
                //This is the drivers latLng
                latLng = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());

                //LatLng of the address
                str_addLatLng = (tempLat + ", " + tempLong);
                name = recipientNames.get(counter);
                System.out.println(name);
                myAddressesUnsGeocodes.put(new LatLng(tempLat, tempLong), name);
                counter++;

                /** Step 4 **/
                if(deliveriesLatLngs.size() == myAddressesUnsorted.size()) {


                    Collections.sort(deliveriesLatLngs, new SortPlaces(latLng));
                    System.out.println(deliveriesLatLngs);

                    System.out.println("Name and code: " + myAddressesUnsGeocodes);

                    for(int i = 0;i<deliveriesLatLngs.size();i++)
                    {
                        LatLng delivery = deliveriesLatLngs.get(i);
                        double trek = distanceFrom(delivery.latitude, delivery.longitude, mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
                        int dist = (int)trek;
                        System.out.println(dist);
                        locationsDistance.put(delivery, dist);
                    }
                    System.out.println(locationsDistance);

                    //namesDistance
                    for(LatLng key:myAddressesUnsGeocodes.keySet()){
                        // copy needed keys ONLY to a new sorted map
                        if (locationsDistance.keySet().contains(key)){
                            namesDistance.put(myAddressesUnsGeocodes.get(key), locationsDistance.get(key));
                        }
                    }

                    System.out.println(namesDistance);

                    namesDistance.entrySet()
                            .stream()
                            .sorted(Map.Entry.comparingByValue())
                            .forEachOrdered(x -> sortedAddresses.put(x.getKey(), x.getValue()));

                    System.out.println("sortedAddresses: " + sortedAddresses);

                    String[] strings = sortedAddresses.keySet().toArray(new String[myAddressesUnsorted.size()]);
                    CurrentNearestCustName = strings[0];
                    String valuee = myAddressesUnsorted.get(CurrentNearestCustName);
                    System.out.println(valuee);

                    String[] arrOfStr = valuee.split("\\+");
                    CurrentReceiverID = arrOfStr[1];
                    CurrentConsignmentNum= arrOfStr[2];
                    str_recEmail= arrOfStr[3];
                    str_phoneNo = arrOfStr[4];
                    if(!CurrentReceiverID.equals("0")) {
                        encodeUserEmail(str_recEmail);
                        //getReceiverLoc();
                    }
                }
                if (dialog.isShowing())
                    dialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public class SortPlaces implements Comparator<LatLng> {
        LatLng currentLoc;

        public SortPlaces(LatLng current) {
            currentLoc = current;
        }

        public double distance(double fromLat, double fromLon, double toLat, double toLon) {
            double radius = 6378137;   // approximate Earth radius, *in meters*
            double deltaLat = toLat - fromLat;
            double deltaLon = toLon - fromLon;
            double angle = 2 * Math.asin(Math.sqrt(
                    Math.pow(Math.sin(deltaLat / 2), 2) +
                            Math.cos(fromLat) * Math.cos(toLat) *
                                    Math.pow(Math.sin(deltaLon / 2), 2)));
            return radius * angle;
        }

        @Override
        public int compare(final LatLng o1, final LatLng o2) {
            double lat1 = o1.latitude;
            double lon1 = o1.longitude;
            double lat2 = o2.latitude;
            double lon2 = o2.longitude;

            double distanceToPlace1 = distance(currentLoc.latitude, currentLoc.longitude, lat1, lon1);
            double distanceToPlace2 = distance(currentLoc.latitude, currentLoc.longitude, lat2, lon2);
            return (int) (distanceToPlace1 - distanceToPlace2);
        }
    }
}