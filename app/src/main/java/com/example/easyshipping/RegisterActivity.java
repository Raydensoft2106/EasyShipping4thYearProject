package com.example.easyshipping;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.textclassifier.TextLinks;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.easyshipping.Model.User;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.view.filter.RangedFilter;
import com.itextpdf.xmp.impl.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

//test

    private EditText editTextEmail, editTextPassword, TvfName, TvlName, TvAdd1, TvAdd2, TvAdd3, TvAdd4, TvPostCode, TvPhone, TvHiddenLoc, TvDepot;
    Spinner country;
    private ProgressBar progressBar;

    private Button btnSignUp;
    private RadioGroup radioGroup;
    private RadioButton RadioButton, RadioButton2, RadioButton3, RadioButton4;

    public String userTypeString;

    public String fName, lName, email, addL1, addL2, addL3, addL4, nation, phone, hiddenLoc, postcode, depot;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        RadioButton = (RadioButton) findViewById(R.id.radioButton);
        RadioButton2 = (RadioButton) findViewById(R.id.radioButton2);
        RadioButton3 = (RadioButton) findViewById(R.id.radioButton3);
        RadioButton4 = (RadioButton) findViewById(R.id.radioButton4);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);


        TvfName = findViewById(R.id.TvfName);
        TvlName = findViewById(R.id.TvlName);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.editTextPassword);

        //Sending/Rec Customer
        TvAdd1 = findViewById(R.id.TvAdd1);
        TvAdd2 = findViewById(R.id.TvAdd2);
        TvAdd3 = findViewById(R.id.TvAdd3);
        TvAdd4 = findViewById(R.id.TvAdd4);
        TvPostCode = findViewById(R.id.TvPostcode);
        TvPhone = findViewById(R.id.TvPhone);
        country = findViewById(R.id.Country);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.countries, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        country.setAdapter(adapter);

        //Receiving Customer
        TvHiddenLoc = findViewById(R.id.TvHiddenLoc);

        //Employees
        TvDepot = findViewById(R.id.TvDepot);

        TvAdd1.setVisibility(View.GONE);
        TvAdd2.setVisibility(View.GONE);
        TvAdd3.setVisibility(View.GONE);
        TvAdd4.setVisibility(View.GONE);
        TvPostCode.setVisibility(View.GONE);
        TvPhone.setVisibility(View.GONE);
        TvHiddenLoc.setVisibility(View.GONE);
        TvDepot.setVisibility(View.GONE);
        country.setVisibility(View.GONE);

        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButton:
                        userTypeString = "Sending Customer";
                        TvAdd1.setVisibility(View.VISIBLE);
                        TvAdd2.setVisibility(View.VISIBLE);
                        TvAdd3.setVisibility(View.VISIBLE);
                        TvAdd4.setVisibility(View.VISIBLE);
                        TvPostCode.setVisibility(View.VISIBLE);
                        TvPhone.setVisibility(View.VISIBLE);
                        TvHiddenLoc.setVisibility(View.GONE);
                        TvDepot.setVisibility(View.GONE);
                        country.setVisibility(View.VISIBLE);
                        break;

                    case R.id.radioButton2:
                        userTypeString = "Receiving Customer";
                        TvAdd1.setVisibility(View.VISIBLE);
                        TvAdd2.setVisibility(View.VISIBLE);
                        TvAdd3.setVisibility(View.VISIBLE);
                        TvAdd4.setVisibility(View.VISIBLE);
                        TvPostCode.setVisibility(View.VISIBLE);
                        TvPhone.setVisibility(View.VISIBLE);
                        TvHiddenLoc.setVisibility(View.VISIBLE);
                        TvDepot.setVisibility(View.GONE);
                        country.setVisibility(View.VISIBLE);
                        break;

                    case R.id.radioButton3:
                        userTypeString = "Warehouse Employee";
                        TvAdd1.setVisibility(View.GONE);
                        TvAdd2.setVisibility(View.GONE);
                        TvAdd3.setVisibility(View.GONE);
                        TvAdd4.setVisibility(View.GONE);
                        TvPostCode.setVisibility(View.GONE);
                        TvPhone.setVisibility(View.GONE);
                        TvHiddenLoc.setVisibility(View.GONE);
                        TvDepot.setVisibility(View.VISIBLE);
                        country.setVisibility(View.GONE);
                        break;

                    case R.id.radioButton4:
                        userTypeString = "Delivery Driver";
                        TvAdd1.setVisibility(View.GONE);
                        TvAdd2.setVisibility(View.GONE);
                        TvAdd3.setVisibility(View.GONE);
                        TvAdd4.setVisibility(View.GONE);
                        TvPostCode.setVisibility(View.GONE);
                        TvPhone.setVisibility(View.GONE);
                        TvHiddenLoc.setVisibility(View.GONE);
                        TvDepot.setVisibility(View.VISIBLE);
                        country.setVisibility(View.GONE);
                        break;
                }
            }
        });

        btnSignUp = findViewById(R.id.btnSignUp);

        mAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("EasyShipping");

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }



    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null) {
            //log out, send to login page
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    private void registerUser() {
        final String userType = userTypeString;
        final String fname = TvfName.getText().toString().trim();
        final String lname = TvlName.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();

        if (!RadioButton.isChecked() & !RadioButton2.isChecked() & !RadioButton3.isChecked() & !RadioButton4.isChecked()) {
            Toast.makeText(getApplicationContext(), "Please select User Type", Toast.LENGTH_SHORT).show();
            return;
        }
//        else
//        {
//            userTypeId = RadioGroup.getCheckedRadioButtonId();
//        }

        if (fname.isEmpty()) {
            TvfName.setError(getString(R.string.input_error_name));
            TvfName.requestFocus();
            return;
        }

        if (lname.isEmpty()) {
            TvlName.setError(getString(R.string.input_error_name));
            TvlName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            editTextEmail.setError(getString(R.string.input_error_email));
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError(getString(R.string.input_error_password));
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 5) {
            editTextPassword.setError(getString(R.string.input_error_password_length));
            editTextPassword.requestFocus();
            return;
        }

        if (TvAdd1.isEnabled() && TvAdd1.getText().toString().trim().isEmpty()) {
            TvAdd1.setError("Address Lines should be longer than a character and not empty");
            TvAdd1.requestFocus();
            return;
        }

        if (TvAdd2.isEnabled() && TvAdd2.getText().toString().trim().isEmpty()) {
            TvAdd2.setError("Address Lines should be longer than a character and not empty");
            TvAdd2.requestFocus();
            return;
        }

        if (TvAdd3.isEnabled() && TvAdd3.getText().toString().trim().isEmpty()) {
            TvAdd3.setError("Address Lines should be longer than a character and not empty");
            TvAdd3.requestFocus();
            return;
        }

        if (TvAdd4.isEnabled() && TvAdd4.getText().toString().trim().isEmpty()) {
            TvAdd4.setError("Address Lines should be longer than a character and not empty");
            TvAdd4.requestFocus();
            return;
        }

        if (TvPostCode.isEnabled() && TvPostCode.getText().toString().trim().isEmpty()) {
            TvPostCode.setError("Address Lines should be longer than a character and not empty");
            TvPostCode.requestFocus();
            return;
        }

        if (TvPhone.isEnabled() && TvPhone.getText().toString().trim().isEmpty()) {
            TvPhone.setError("Address Lines should be longer than a character and not empty");
            TvPhone.requestFocus();
            return;
        }

        if (TvHiddenLoc.isEnabled() && TvHiddenLoc.getText().toString().trim().isEmpty()) {
            TvHiddenLoc.setError("Address Lines should be longer than a character and not empty");
            TvHiddenLoc.requestFocus();
            return;
        }

//        if (TvDepot.isEnabled() && TvDepot.getText().toString().trim().isEmpty()) {
//            TvDepot.setError("Address Lines should be longer than a character and not empty");
//            TvDepot.requestFocus();
//            return;
//        }


        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            User user = new User(
                                    fname,
                                    lname,
                                    email,
                                    userType
                            );

                            FirebaseDatabase.getInstance().getReference("Users2")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, getString(R.string.registration_success), Toast.LENGTH_LONG).show();
                                        //startActivity(new Intent(RegisterActivity.this, NotMainActivity.class));
                                        if(userTypeString.equals("Sending Customer"))
                                        {
                                            insertNewSendCustToDb();
                                            Intent i=new Intent(RegisterActivity.this, SenderCustHomeActivity.class);
                                            i.putExtra("email", email);
                                            startActivity(i);
                                            //startActivity(new Intent(RegisterActivity.this, SenderCustHomeActivity.class));
                                        } else if(userTypeString.equals("Receiving Customer")){
                                            insertNewRecCustToDb();
                                            Intent i=new Intent(RegisterActivity.this, ReceiverCustHomeActivity.class);
                                            i.putExtra("email", email);
                                            startActivity(i);
                                            //startActivity(new Intent(RegisterActivity.this, ReceiverCustHomeActivity.class));
                                        } else if(userTypeString.equals("Warehouse Employee")){
                                            insertNewWareEmpToDb();
                                            Intent i=new Intent(RegisterActivity.this, WarehouseEmpHomeActivity.class);
                                            i.putExtra("email", email);
                                            startActivity(i);
                                            //startActivity(new Intent(RegisterActivity.this, WarehouseEmpHomeActivity.class));
                                        } else if(userTypeString.equals("Delivery Driver")){
                                            insertNewDriveEmpToDb();
                                            Intent i=new Intent(RegisterActivity.this, DriverEmpHomeActivity.class);
                                            i.putExtra("email", email);
                                            startActivity(i);
                                            //startActivity(new Intent(RegisterActivity.this, DriverEmpHomeActivity.class));
                                        }
//                                        switch (userTypeString) {
//                                            case "Sending Customer":
//                                                startActivity(new Intent(RegisterActivity.this, SenderCustHomeActivity.class));
//                                                break;
//                                            case "Receiving Customer":
//                                                startActivity(new Intent(RegisterActivity.this, ReceiverCustHomeActivity.class));
//                                                break;
//                                            case "Warehouse Employee":
//                                                startActivity(new Intent(RegisterActivity.this, WarehouseEmpHomeActivity.class));
//                                                break;
//                                            case "Delivery Driver":
//                                                startActivity(new Intent(RegisterActivity.this, DriverEmpHomeActivity.class));
//                                                break;
                                    } else {
                                        Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        } else {
                            Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

//    public void onRadioButtonClicked(View view) {
//        switch (view.getId()){
//            case R.id.radioButton:
//                userTypeString="Sending Customer";
//                TvAdd1.setVisibility(View.VISIBLE);
//                TvAdd2.setVisibility(View.VISIBLE);
//                TvAdd3.setVisibility(View.VISIBLE);
//                TvAdd4.setVisibility(View.VISIBLE);
//                TvPostCode.setVisibility(View.VISIBLE);
//                TvPhone.setVisibility(View.VISIBLE);
//                TvHiddenLoc.setVisibility(View.GONE);
//                TvDepot.setVisibility(View.GONE);
//                country.setVisibility(View.VISIBLE);
//                RadioButton.setChecked(true);
//                RadioButton2.setChecked(false);
//                RadioButton3.setChecked(false);
//                RadioButton4.setChecked(false);
//                break;
//
//            case R.id.radioButton2:
//                userTypeString="Receiving Customer";
//                TvAdd1.setVisibility(View.VISIBLE);
//                TvAdd2.setVisibility(View.VISIBLE);
//                TvAdd3.setVisibility(View.VISIBLE);
//                TvAdd4.setVisibility(View.VISIBLE);
//                TvPostCode.setVisibility(View.VISIBLE);
//                TvPhone.setVisibility(View.VISIBLE);
//                TvHiddenLoc.setVisibility(View.VISIBLE);
//                TvDepot.setVisibility(View.GONE);
//                country.setVisibility(View.VISIBLE);
//                RadioButton.setChecked(false);
//                RadioButton2.setChecked(true);
//                RadioButton3.setChecked(false);
//                RadioButton4.setChecked(false);
//                break;
//
//            case R.id.radioButton3:
//                userTypeString = "Warehouse Employee";
//                TvAdd1.setVisibility(View.GONE);
//                TvAdd2.setVisibility(View.GONE);
//                TvAdd3.setVisibility(View.GONE);
//                TvAdd4.setVisibility(View.GONE);
//                TvPostCode.setVisibility(View.GONE);
//                TvPhone.setVisibility(View.GONE);
//                TvHiddenLoc.setVisibility(View.GONE);
//                TvDepot.setVisibility(View.VISIBLE);
//                country.setVisibility(View.GONE);
//                RadioButton.setChecked(false);
//                RadioButton2.setChecked(false);
//                RadioButton3.setChecked(true);
//                RadioButton4.setChecked(false);
//                break;
//
//            case R.id.radioButton4:
//                userTypeString = "Delivery Driver";
//                TvAdd1.setVisibility(View.GONE);
//                TvAdd2.setVisibility(View.GONE);
//                TvAdd3.setVisibility(View.GONE);
//                TvAdd4.setVisibility(View.GONE);
//                TvPostCode.setVisibility(View.GONE);
//                TvPhone.setVisibility(View.GONE);
//                TvHiddenLoc.setVisibility(View.GONE);
//                TvDepot.setVisibility(View.VISIBLE);
//                country.setVisibility(View.GONE);
//                RadioButton.setChecked(false);
//                RadioButton2.setChecked(false);
//                RadioButton3.setChecked(false);
//                RadioButton4.setChecked(true);
//                break;
//        }
//    }

    public void insertNewSendCustToDb(){
        final String fName = TvfName.getText().toString().trim();
        final String lName = TvlName.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String addL1 = TvAdd1.getText().toString().trim();
        final String addL2 = TvAdd2.getText().toString().trim();
        final String addL3 = TvAdd3.getText().toString().trim();
        final String addL4 = TvAdd4.getText().toString().trim();
        final String nation = country.getSelectedItem().toString().trim();
        final String phone = TvPhone.getText().toString().trim();
        String hiddenLoc = TvHiddenLoc.getText().toString().trim();
        final String postcode = TvPostCode.getText().toString().trim();
        String depot = TvDepot.getText().toString().trim();

        StringRequest request = new StringRequest(Request.Method.POST, "https://easyshippingrh.000webhostapp.com/regSenCust.php", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Toast.makeText(RegisterActivity.this, response,Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("fName", fName);
                params.put("lName", lName);
                params.put("email", email);
                params.put("addressline1", addL1);
                params.put("addressline2", addL2);
                params.put("towncity", addL3);
                params.put("countystate", addL4);
                params.put("country", nation);
                params.put("postcode", postcode);
                params.put("phoneno", phone);

                return params;
            }

        };

        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);
        requestQueue.add(request);
    }

    public void insertNewRecCustToDb(){

        //progressBar.setVisibility(View.VISIBLE);
        final String fName = TvfName.getText().toString().trim();
        final String lName = TvlName.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String addL1 = TvAdd1.getText().toString().trim();
        final String addL2 = TvAdd2.getText().toString().trim();
        final String addL3 = TvAdd3.getText().toString().trim();
        final String addL4 = TvAdd4.getText().toString().trim();
        final String nation = country.getSelectedItem().toString().trim();
        final String phone = TvPhone.getText().toString().trim();
        final String hiddenLoc = TvHiddenLoc.getText().toString().trim();
        final String postcode = TvPostCode.getText().toString().trim();
        String depot = TvDepot.getText().toString().trim();


        StringRequest request = new StringRequest(Request.Method.POST, "https://easyshippingrh.000webhostapp.com/regRecCust.php", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Toast.makeText(RegisterActivity.this, response,Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("help", "Login Error: " + error.getMessage());
                progressBar.setVisibility(View.GONE);
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("fName", fName);
                params.put("lName", lName);
                params.put("email", email);
                params.put("addressline1", addL1);
                params.put("addressline2", addL2);
                params.put("towncity", addL3);
                params.put("countystate", addL4);
                params.put("country", nation);
                params.put("postcode", postcode);
                params.put("phoneno", phone);
                params.put("hiddenloc", hiddenLoc);

                return params;
            }

        };

        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);
        requestQueue.add(request);
    }

    public void insertNewWareEmpToDb(){

        //progressBar.setVisibility(View.VISIBLE);
        final String fName = TvfName.getText().toString().trim();
        final String lName = TvlName.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String depot = TvDepot.getText().toString().trim();


        StringRequest request = new StringRequest(Request.Method.POST, "https://easyshippingrh.000webhostapp.com/regWareEmp.php", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Toast.makeText(RegisterActivity.this, response,Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("help", "Login Error: " + error.getMessage());
                progressBar.setVisibility(View.GONE);
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("fName", fName);
                params.put("lName", lName);
                params.put("email", email);
                params.put("depot", depot);

                return params;
            }

        };

        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);
        requestQueue.add(request);
    }

    public void insertNewDriveEmpToDb(){

        //progressBar.setVisibility(View.VISIBLE);
        final String fName = TvfName.getText().toString().trim();
        final String lName = TvlName.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String depot = TvDepot.getText().toString().trim();


        StringRequest request = new StringRequest(Request.Method.POST, "https://easyshippingrh.000webhostapp.com/regDriveEmp.php", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Toast.makeText(RegisterActivity.this, response,Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("help", "Login Error: " + error.getMessage());
                progressBar.setVisibility(View.GONE);
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("fName", fName);
                params.put("lName", lName);
                params.put("email", email);
                params.put("depot", depot);

                return params;
            }

        };

        // Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);
        requestQueue.add(request);
    }

    public void onLoginClick(View view) {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class).putExtra("Mode", 2));
    }

}