package com.project.agro_retailerapp.Retailers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project.agro_retailerapp.Constant;
import com.project.agro_retailerapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;

public class OrderListRetailerActivity extends AppCompatActivity {

    ListView CustomList;
    Button btnSearch;
    EditText etxtSearch;
    private ProgressDialog loading;

    String cell;
    SharedPreferences sharedPreferences;

    public String mStatus[] = new String[999];
    public String mId[] = new String[999];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list_retailer);


        CustomList = findViewById(R.id.listViewCusOrder);
        btnSearch = findViewById(R.id.btnSearchOrderCus);
        etxtSearch = findViewById(R.id.etxt_searchOrderCus);


        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button
        getSupportActionBar().setTitle("My Order List");


        //Fetching cell from shared preferences
        sharedPreferences = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        cell = sharedPreferences.getString(Constant.CELL_SHARED_PREF, "Not Available");


        //call function
        getData("");


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchText = etxtSearch.getText().toString().trim();

                if (searchText.isEmpty()) {
                    Toasty.error(OrderListRetailerActivity.this, "Please input text!", Toast.LENGTH_SHORT).show();
                } else {
                    getData(searchText);
                }
            }
        });
    }


    private void getData(String s) {

        String getSearchText = s;
        //showing progress dialog
        loading = new ProgressDialog(this);
        loading.setMessage("Please wait....");
        loading.show();

        if (!s.isEmpty()) {
            getSearchText = s;
        }


        String url = Constant.ORDER_LIST_URL + cell + "&text=" + getSearchText;

        Log.d("URL", url);
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("Response", response);
                loading.dismiss();
                showJSON(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        loading.dismiss();
                        Toasty.error(OrderListRetailerActivity.this, "Network Error!", Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showJSON(String response) {


        Log.d("Response 2", response);

        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Constant.JSON_ARRAY);

            if (result.length() == 0) {
                Toasty.error(OrderListRetailerActivity.this, "No Order Found!", Toast.LENGTH_SHORT).show();
                finish();


            } else {
                for (int i = 0; i < result.length(); i++) {
                    JSONObject jo = result.getJSONObject(i);

                    String id = jo.getString(Constant.KEY_ID);
                    String name = jo.getString(Constant.KEY_NAME);
                    String price = jo.getString(Constant.KEY_PRICE);


                    String quantity = jo.getString(Constant.KEY_QUANTITY);
                    String status = jo.getString(Constant.KEY_STATUS);

                    mStatus[i] = status;
                    mId[i] = id;


                    if (status.equals("0")) {
                        status = "Pending";
                    } else if (status.equals("1")) {
                        status = "Confirmed";
                    } else if (status.equals("2")) {
                        status = "Cancel";
                    } else {
                        status = "Delivered";
                    }
                    HashMap<String, String> user_msg = new HashMap<>();

                    user_msg.put(Constant.KEY_NAME, name);
                    user_msg.put(Constant.KEY_PRICE, "Price: " + price);
                    user_msg.put(Constant.KEY_QUANTITY, "Quantity: " + quantity);
                    user_msg.put(Constant.KEY_STATUS, "Status: " + status);


                    list.add(user_msg);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        ListAdapter adapter = new SimpleAdapter(
                OrderListRetailerActivity.this, list, R.layout.order_list_items,
                new String[]{Constant.KEY_NAME, Constant.KEY_PRICE, Constant.KEY_QUANTITY, Constant.KEY_STATUS},
                new int[]{R.id.txt_name, R.id.txt_price, R.id.txt_quantity, R.id.txt_status});
        CustomList.setAdapter(adapter);

        CustomList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {


                /*new AlertDialog.Builder(OrderListRetailerActivity.this)
                        //.setMessage("Want to Call this Shop Now?")
                        .setMessage("Choose about this shop")
                        .setCancelable(false)
                        .setPositiveButton("Write Review", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (mStatus[position].equals("1")) {
                                    Intent intent = new Intent(OrderListRetailerActivity.this, ReviewActivity.class);
                                    intent.putExtra("id", mId[position]);
                                    startActivity(intent);

                                } else {
                                    Toasty.info(OrderListRetailerActivity.this, "You can provide review if the order is confirmed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("Order Details", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {


                                Intent intent = new Intent(OrderListRetailerActivity.this, OrderDetailsSpActivity.class);

                                intent.putExtra("type", "Customer");

                                //intent.putExtra("type", "Shop");
                                startActivity(intent);

                            }
                        })

                        .show();*/

             /*   if (mStatus[position].equals("1")) {
                    Intent intent = new Intent(OrderListRetailerActivity.this, ReviewActivity.class);
                    intent.putExtra("id", mId[position]);
                    startActivity(intent);

                } else {
                    Toasty.info(OrderListRetailerActivity.this, "You can provide review if the order is confirmed", Toast.LENGTH_SHORT).show();
                }*/
                if (mStatus[position].equals("1")) {
                  /*  Intent intent = new Intent(CusOrderListChActivity.this, ReviewActivity.class);
                    intent.putExtra("id", mId[position]);
                    startActivity(intent);
                  */
                    Toasty.info(OrderListRetailerActivity.this, "Your order is confirmed", Toast.LENGTH_SHORT).show();


                } else if (mStatus[position].equals("2")) {
                    Toasty.info(OrderListRetailerActivity.this, "Your order is canceled", Toast.LENGTH_SHORT).show();
                } else {
                    Toasty.info(OrderListRetailerActivity.this, "Your order is pending", Toast.LENGTH_SHORT).show();
                }
            }


        });

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

}