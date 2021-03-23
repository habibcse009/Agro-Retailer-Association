package com.project.agro_retailerapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.PermissionRequest;
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
import com.project.agro_retailerapp.Admin.AdminMainActivity;
import com.project.agro_retailerapp.AgroTeam.AgroMainActivity;
import com.project.agro_retailerapp.Retailers.RetailerMainActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity {
    ProgressDialog progressDialog;

    TextView txtSignin;
    TextView txtLogin, txtSignUp;
    EditText etxtCell,etxtAccountType, etxtPassword;

    private ProgressDialog loading;

    String UserCell, UserPassword,getMobile;
    SharedPreferences sharedPreferences;
    private final String PREF_OTP = "com.project.agro_retailerapp.forgot_password";
    SharedPreferences.Editor editor2;
    SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtLogin =  findViewById(R.id.signin);
        txtSignUp = findViewById(R.id.signup);
        etxtCell =  findViewById(R.id.cell);
        etxtPassword =  findViewById(R.id.password);
        etxtAccountType=findViewById(R.id.ac_type);

        //Fetching cell from shared preferences
        sharedPreferences = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String cell = sharedPreferences.getString(Constant.CELL_SHARED_PREF, "Not Available");
        String get_password = sharedPreferences.getString(Constant.PASSWORD_SHARED_PREF, "0");
        String get_ac_type = sharedPreferences.getString(Constant.AC_TYPE_SHARED_PREF, "Not Available");
        UserCell = cell;
        UserPassword = get_password;

        sp = getSharedPreferences(PREF_OTP, MODE_PRIVATE);
        editor2 = sp.edit();

//
//        if (Build.VERSION.SDK_INT>=23) //Android MarshMellow Version or above
//        {
//            requestAllPermission();
//        }
//


        Log.d("Value", UserCell + "  " + UserPassword + "  " + get_ac_type);

        if (!UserCell.equals("Not Available") && !UserPassword.equals("0") ) {
            etxtCell.setText(UserCell);
            etxtPassword.setText(UserPassword);
            etxtAccountType.setText(get_ac_type);

         //   login();

        }


        //For choosing account type and open alert dialog
        etxtAccountType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String[] typeList = {"Agro", "Retailers","Admin"};

                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("SELECT TYPE");
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
                            case 2:
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




        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });


        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                login(); //calling login method

            }
        });
    }

    private void login() {
        //Getting values from edit texts
        final String userCell = etxtCell.getText().toString().trim();
        final String password = etxtPassword.getText().toString().trim();
        final String account_type = etxtAccountType.getText().toString();


        if ( account_type.isEmpty()) {

            etxtAccountType.setError("Please Select Type !");
            etxtAccountType.requestFocus();
        }

        if ( userCell.length() != 11 || userCell.contains(" ") || userCell.charAt(0) != '0' || userCell.charAt(1) != '1') {

            etxtCell.setError("Please enter cell !");
            etxtCell.requestFocus();
        }

        //Checking password field/validation
        else if (password.length() < 4) {

            etxtPassword.setError("Password at least 4 character long !");
            etxtPassword.requestFocus();

        } else {
            //showing progress dialog
            loading = new ProgressDialog(this);
            loading.setMessage("Please wait....");
            loading.show();

            //Creating a string request
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.LOGIN_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            String myResponse=response.trim();


                            Log.d("Response",response);
                            //If we are getting success from server
                            if (myResponse.equals("admin")) {
                                //Creating a shared preference
                                sharedPreferences = LoginActivity.this.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                                //Creating editor to store values to shared preferences
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                //Adding values to editor
                                editor.putBoolean(Constant.LOGGEDIN_SHARED_PREF, true);

                                //Adding values to editor
                                editor.putString(Constant.AC_TYPE_SHARED_PREF, account_type);
                                editor.putString(Constant.CELL_SHARED_PREF, userCell);
                                editor.putString(Constant.PASSWORD_SHARED_PREF, password);


                                //editor.putString(Config.NAME_SHARED_PREF, name);

                                //Saving values to editor
                                editor.apply();

                                loading.dismiss();
                                //Starting profile activity

                                Toasty.success(LoginActivity.this, "Login Success!", Toast.LENGTH_SHORT, true).show();


                                //warningDialog();

                                Intent intent = new Intent(LoginActivity.this, AdminMainActivity.class);
                                startActivity(intent);


                            }


                            else  if (myResponse.equals("agro")) {
                                //Creating a shared preference
                                sharedPreferences = LoginActivity.this.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                                //Creating editor to store values to shared preferences
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                //Adding values to editor
                                editor.putBoolean(Constant.LOGGEDIN_SHARED_PREF, true);

                                //Adding values to editor
                                editor.putString(Constant.AC_TYPE_SHARED_PREF, account_type);
                                editor.putString(Constant.CELL_SHARED_PREF, userCell);
                                editor.putString(Constant.PASSWORD_SHARED_PREF, password);


                                //editor.putString(Config.NAME_SHARED_PREF, name);

                                //Saving values to editor
                                editor.apply();

                                loading.dismiss();
                                //Starting profile activity

                                Toasty.success(LoginActivity.this, "Login Success!", Toast.LENGTH_SHORT, true).show();


                                //warningDialog();

                                Intent intent = new Intent(LoginActivity.this, AgroMainActivity.class);
                                startActivity(intent);


                            }


                            else  if (myResponse.equalsIgnoreCase("retailers")) {
                                //Creating a shared preference
                                sharedPreferences = LoginActivity.this.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                                //Creating editor to store values to shared preferences
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                //Adding values to editor
                                editor.putBoolean(Constant.LOGGEDIN_SHARED_PREF, true);

                                //Adding values to editor
                                editor.putString(Constant.AC_TYPE_SHARED_PREF, account_type);
                                editor.putString(Constant.CELL_SHARED_PREF, userCell);
                                editor.putString(Constant.PASSWORD_SHARED_PREF, password);


                                //editor.putString(Config.NAME_SHARED_PREF, name);

                                //Saving values to editor
                                editor.apply();

                                loading.dismiss();
                                //Starting profile activity

                                Toasty.success(LoginActivity.this, "Login Success!", Toast.LENGTH_SHORT, true).show();


                                //warningDialog();

                                Intent intent = new Intent(LoginActivity.this, RetailerMainActivity.class);
                                startActivity(intent);


                            }


                            else {
                                //If the server response is not success
                                //Displaying an error message on toast

                                Toasty.error(LoginActivity.this, "Invalid Cell or password", Toast.LENGTH_SHORT, true).show();
                                //Toast.makeText(Activity_Signin.this, "Invalid Cell or password", Toast.LENGTH_LONG).show();
                                loading.dismiss();
                            }
                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //You can handle error here if you want

                            Toasty.error(LoginActivity.this, "Error in connection!!", Toast.LENGTH_SHORT, true).show();
                            // Toast.makeText(Activity_Signin.this, "Error in connection!!!", Toast.LENGTH_LONG).show();
                            loading.dismiss();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    //Adding parameters to request
                    params.put(Constant.KEY_TYPE, account_type);
                    params.put(Constant.KEY_CELL, userCell);
                    params.put(Constant.KEY_PASSWORD, password);

                    Log.d("Values",account_type+" "+userCell+ " "+password);


                    //returning parameter
                    return params;
                }
            };

            //Adding the string request to the queue
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }

    }






    //for request focus

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    @SuppressLint("StaticFieldLeak")
    public class MyAsyncTasks extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // display a progress dialog for good user experience
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("Please Wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            // implement API in background and store the response in current variable

            try {

                //  String token = "967c5bf770a47eb1731dec1e5690a7c4";
                String token = "2a30df0df1edf4a4a8fecce9dc01ba79";

                //Single or Multiple mobiles numbers separated by comma
                String to = getMobile;


                //generate 4 digit random code
                Random r = new Random();
                String code = String.format(Locale.ENGLISH, "%04d", r.nextInt(9000));

                editor2.putString("otp_code", code);
                editor2.commit();


                //Your message to send, Add URL encoding here.
                String textmessage = "Your OTP code is " + code + ". Enter for verification. Stay with Agro-Retailer App";

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

            Log.d("data", s.toString());
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


        AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivity.this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_otp_input, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);

        final Button dialog_button = (Button) dialogView.findViewById(R.id.dialog_button);
        final EditText dialog_input = (EditText) dialogView.findViewById(R.id.dialog_input);
        final TextView dialog_title = (TextView) dialogView.findViewById(R.id.dialog_title);

        dialog_button.setText(getString(R.string.otp_submit));
        dialog_title.setText(getString(R.string.otp_verification));


        final AlertDialog alertDialog = dialog.create();
        alertDialog.show();

        dialog_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code = dialog_input.getText().toString().trim();
                if (code.isEmpty()) {

                    dialog_input.setError("Wrong Input");
                    dialog_input.requestFocus();


                } else {
                    verifyOTP(code);
                }

                alertDialog.dismiss();
            }
        });


    }


    //verify otp
    public void verifyOTP(String code) {
        String getSavedOtp = sp.getString("otp_code", "");

        if (code.equals(getSavedOtp)) {
            //Toast.makeText(this, "Success! Verified", Toast.LENGTH_SHORT).show();
            // Snackbar.make(parentView, getString(R.string.otp_success), Snackbar.LENGTH_LONG).show();


            // Proceed User login

            //   passwordReset(getMobile, getPassword);   eta comment krsi


        } else {

            // Snackbar.make(parentView, getString(R.string.otp_wrong), Snackbar.LENGTH_LONG).show();
            Toasty.error(this, "Wrong OTP Code!", Toast.LENGTH_SHORT).show();
        }


    }

/*

    private void passwordReset(final String phone, final String password) {


        //showing progress dialog
        loading = new ProgressDialog(this);
        loading.setMessage("Please wait....");
        loading.show();

        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.PASSWORD_RESET_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("response", response);


                        //If we are getting success from server
                        if (response.equalsIgnoreCase("success")) {

                            loading.dismiss();
                            //Starting profile activity

                            Toasty.success(LoginActivity.this, "Password Reset Successful!!!", Toast.LENGTH_LONG).show();


                        } else {
                            //If the server response is not success
                            //Displaying an error message on toast

                            Toasty.error(LoginActivity.this, "Invalid Request", Toast.LENGTH_LONG).show();
                            loading.dismiss();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want

//                            Toasty.error(LoginActivity.this, "Error in connection!!", Toast.LENGTH_SHORT, true).show();
                        Toasty.error(LoginActivity.this, "Error in connection!!", Toast.LENGTH_LONG).show();
                        loading.dismiss();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
                params.put(Constant.KEY_MOBILE, phone);
                params.put(Constant.KEY_PASSWORD, password);


                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

*/

/*    private void requestAllPermission() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            // Toast.makeText(getApplicationContext(), "All permissions are granted!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            //showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        //Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }*/

}
