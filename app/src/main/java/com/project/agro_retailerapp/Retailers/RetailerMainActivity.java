package com.project.agro_retailerapp.Retailers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.project.agro_retailerapp.AgroTeam.Products.AllProductsActivity;
import com.project.agro_retailerapp.Constant;
import com.project.agro_retailerapp.R;
import com.project.agro_retailerapp.Retailers.Profile.ProfileRetailerActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import es.dmoral.toasty.Toasty;

public class RetailerMainActivity extends AppCompatActivity {

    CardView cardProfile, cardProducts, cardMyOrder, cardLogout;

    SharedPreferences sp;
    SharedPreferences.Editor editor;

    //for double back press to exit
    private static final int TIME_DELAY = 2000;
    private static long back_pressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_main);

        getSupportActionBar().setTitle("Retailers Panel");


        cardProfile = findViewById(R.id.card_profile);
        cardLogout = findViewById(R.id.card_logout);
        cardProducts = findViewById(R.id.card_products);
        cardMyOrder = findViewById(R.id.card_my_order);


        sp = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();


        cardProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RetailerMainActivity.this, ProfileRetailerActivity.class);
                startActivity(intent);
            }
        });

        cardMyOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RetailerMainActivity.this, OrderListRetailerActivity.class);
                startActivity(intent);
            }
        });


        cardProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RetailerMainActivity.this, AllProductsActivity.class);
                intent.putExtra("type", "retailer");
                startActivity(intent);
            }
        });
        cardLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.clear();
                editor.apply();
                finishAffinity();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.guide_menu, menu);
        return true;
    }


    //double backpress to exit
    @Override
    public void onBackPressed() {
        if (back_pressed + TIME_DELAY > System.currentTimeMillis()) {

            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            finish();

        } else {
            Toasty.info(this, "Press once again to exit!",
                    Toast.LENGTH_SHORT).show();
        }
        back_pressed = System.currentTimeMillis();
    }

}
