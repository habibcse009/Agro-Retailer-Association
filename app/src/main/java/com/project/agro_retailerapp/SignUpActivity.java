package com.project.agro_retailerapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;

public class SignUpActivity extends AppCompatActivity {

    private ProgressDialog loading;
    private final String PREF_OTP = "com.app.otp";
    private ProgressDialog progressDialog;

    SharedPreferences.Editor editor;
    SharedPreferences sp;

    TextView txtSignup;
    String name, accountType, cell, gender, email, location, password;


    EditText etxtName, etxtAccountType, etxtCell, etxtGender, etxtLocation, etxtEmail, etxtPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button


        txtSignup = findViewById(R.id.signup);
        // signupback = (ImageView)findViewById(R.id.signupback);

        etxtName = findViewById(R.id.fullname);
        etxtAccountType = findViewById(R.id.ac_type);
        etxtCell = findViewById(R.id.cell);
        etxtLocation = findViewById(R.id.location);

        etxtPassword = findViewById(R.id.password);
        etxtGender = findViewById(R.id.gender);

        sp = getSharedPreferences(PREF_OTP, MODE_PRIVATE);
        editor = sp.edit();


        //For choosing account type and open alert dialog
        etxtGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String[] genderList = {"Male", "Female"};

                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                builder.setTitle("SELECT GENDER");
                //builder.setIcon(R.drawable.ic_gender);


                builder.setCancelable(false);
                builder.setItems(genderList, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        switch (position) {
                            case 0:
                                etxtGender.setText(genderList[position]);
                                break;

                            case 1:
                                etxtGender.setText(genderList[position]);
                                break;


                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        dialog.dismiss();
                    }
                });


                AlertDialog accountTypeDialog = builder.create();

                accountTypeDialog.show();
            }

        });


        //For choosing account type and open alert dialog
        etxtAccountType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String[] typeList = {"Agro", "Retailers"};

                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                builder.setTitle("SELECT ACCOUNT TYPE");
                //builder.setIcon(R.drawable.ic_gender);


                builder.setCancelable(false);
                builder.setItems(typeList, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        switch (position) {
                            case 0:
                                etxtAccountType.setText(typeList[position]);
                                break;

                            case 1:
                                etxtAccountType.setText(typeList[position]);
                                break;


                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        dialog.dismiss();
                    }
                });


                AlertDialog accountTypeDialog = builder.create();

                accountTypeDialog.show();
            }

        });


        //For choosing gender and open alert dialog
        etxtLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String[] cityList = {"Dhaka", "Chittagong", "Sylhet", "Rajshahi", "Barishal", "Khulna", "Rangpur", "Mymensingh"};

                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                builder.setTitle("SELECT DIVISION");
                //builder.setIcon(R.drawable.ic_location);


                builder.setCancelable(false);
                builder.setItems(cityList, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        switch (position) {
                            case 0:

                                etxtLocation.setText("Dhaka");
                                break;

                            case 1:

                                etxtLocation.setText("Chittagong");
                                break;

                            case 2:

                                etxtLocation.setText("Sylhet");
                                break;

                            case 3:

                                etxtLocation.setText("Rajshahi");
                                break;

                            case 4:

                                etxtLocation.setText("Barishal");
                                break;

                            case 5:

                                etxtLocation.setText("Khulna");
                                break;

                            case 6:

                                etxtLocation.setText("Rangpur");
                                break;

                            case 7:

                                etxtLocation.setText("Mymensingh");
                                break;
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        dialog.dismiss();
                    }
                });


                AlertDialog locationTypeDialog = builder.create();

                locationTypeDialog.show();
            }

        });


        txtSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Getting values from edit texts
                name = etxtName.getText().toString().trim();
                accountType = etxtAccountType.getText().toString();
                cell = etxtCell.getText().toString().trim();
                location = etxtLocation.getText().toString().trim();
                password = etxtPassword.getText().toString().trim();
                gender = etxtGender.getText().toString().trim();


                //Checking  field/validation


                //Checking  field/validation
                if (accountType.isEmpty()) {
                    etxtAccountType.setError("Please select account type!");
                    requestFocus(etxtName);
                } else if (name.isEmpty()) {
                    etxtName.setError("Please enter your name !");
                    requestFocus(etxtName);
                }


                //Checking username field/validation
                else if (cell.length() != 11 || cell.contains(" ") || cell.charAt(0) != '0' || cell.charAt(1) != '1') {
                    etxtCell.setError("Please enter correct cell !");
                    requestFocus(etxtCell);
                }

                //Checking  field/validation

                //Checking username field/validation
                else if (location.isEmpty()) {
                    etxtLocation.setError("Please enter your location!");
                    requestFocus(etxtLocation);
                }


                //Checking password field/validation
                else if (password.length() < 4) {

                    etxtPassword.setError("Password at least 4 character long !");
                    requestFocus(etxtPassword);

                } else {
                    MyAsyncTasks myAsyncTasks = new MyAsyncTasks();
                    myAsyncTasks.execute();


                  //  signup();


                }

            }
        });

    }


    private void signup() {


        //showing progress dialog
//
        loading = new ProgressDialog(SignUpActivity.this);
        loading.setMessage("Please wait....");
        loading.show();

        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.SIGNUP_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        String myResponse = response.trim();


                        //for logcat
                        Log.d("RESPONSE", response);


                        //If we are getting success from server
                        if (myResponse.equals("success")) {


                            loading.dismiss();
                            //Starting profile activity
                            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                            Toasty.success(SignUpActivity.this, "Sign up successful", Toast.LENGTH_SHORT).show();
                            startActivity(intent);

                        } else if (myResponse.equalsIgnoreCase(Constant.USER_EXISTS)) {

                            Toasty.error(SignUpActivity.this, "User Already exists!", Toast.LENGTH_SHORT).show();
                            loading.dismiss();

                        }

                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want

                        Toasty.error(SignUpActivity.this, "Error in connection!", Toast.LENGTH_LONG).show();
                        // loading.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request

                params.put(Constant.KEY_TYPE, accountType);
                params.put(Constant.KEY_NAME, name);
                params.put(Constant.KEY_CELL, cell);
                params.put(Constant.KEY_GENDER, gender);
                params.put(Constant.KEY_LOCATION, location);
                params.put(Constant.KEY_PASSWORD, password);

                //returning parameter
                return params;
            }
        };


        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }


    //for request focus
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    //for back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

// work on OTP

    public class MyAsyncTasks extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // display a progress dialog for good user experience
            progressDialog = new ProgressDialog(SignUpActivity.this);
            progressDialog.setMessage("Please Wait. Sending otp code...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            // implement API in background and store the response in current variable

            try {

                //sms api token
                // String token = "9965fd5c3678c7a06cb718f2f3d3564b";
                String token = "2a30df0df1edf4a4a8fecce9dc01ba79";

                //Single or Multiple mobiles numbers separated by comma

                String to = cell;


                //generate 4 digit random code
                Random r = new Random();
                String code = String.format(Locale.ENGLISH, "%04d", r.nextInt(9000));

                editor.putString("otp_code", code);
                editor.commit();


                //Your message to send, Add URL encoding here.
                String textmessage = "Your OTP code is " + code + ". Enter for verification.\nStay with Agro-Retailer App. Thanks.";

                URLConnection myURLConnection = null;
                URL myURL = null;
                BufferedReader reader = null;

                //encode the message content
                String encoded_message = URLEncoder.encode(textmessage);
                String apiUrl = "http://api.greenweb.com.bd/api.php?";

                StringBuilder sgcPostContent = new StringBuilder(apiUrl);
                sgcPostContent.append("token=" + token);
                sgcPostContent.append("&to=" + to);
                sgcPostContent.append("&message=" + encoded_message);

                apiUrl = sgcPostContent.toString();
                try {

                    int SDK_INT = android.os.Build.VERSION.SDK_INT;
                    if (SDK_INT > 8) {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                .permitAll().build();
                        StrictMode.setThreadPolicy(policy);
                        //your codes here

                    }
                    //prepare connection
                    myURL = new URL(apiUrl);
                    myURLConnection = myURL.openConnection();
                    myURLConnection.connect();
                    reader = new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));

                    //read the output
                    String output;
                    while ((output = reader.readLine()) != null)
                        //print output
                        Log.d("OUTPUT", "" + output);

                    //Close connection
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            } catch (Exception e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
            return "ok";
        }

        @Override
        protected void onPostExecute(String s) {

            // dismiss the progress dialog after receiving data from API
            progressDialog.dismiss();
            try {
                // JSON Parsing of data
                dialog();

            } catch (Exception e) {
                e.printStackTrace();
            }


        }

    }


    //dialog for taking otp code
    public void dialog() {

        android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(SignUpActivity.this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_otp_input, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);

        final Button dialog_button = (Button) dialogView.findViewById(R.id.dialog_button);
        final EditText dialog_input = (EditText) dialogView.findViewById(R.id.dialog_input);
        final TextView dialog_title = (TextView) dialogView.findViewById(R.id.dialog_title);

        dialog_button.setText(getString(R.string.otp_submit));
        dialog_title.setText(getString(R.string.otp_verification));


        final android.app.AlertDialog alertDialog = dialog.create();
        alertDialog.show();

        dialog_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code = dialog_input.getText().toString().trim();
                if (code.length() < 4) {


                    dialog_input.setError("Wrong Input");
                    dialog_input.requestFocus();


                } else {

                    alertDialog.dismiss();
                    verifyOTP(code);


                }


            }
        });


    }


    //verify otp
    public void verifyOTP(String code) {
        String getSavedOtp = sp.getString("otp_code", "");

        if (code.equals(getSavedOtp)) {


            signup();

        } else {

            Toasty.error(this, "Wrong OTP Code!", Toast.LENGTH_SHORT).show();
        }


    }

}