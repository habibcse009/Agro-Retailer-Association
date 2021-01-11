package com.project.agro_retailerapp.AgroTeam;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.project.agro_retailerapp.Constant;
import com.project.agro_retailerapp.R;
import com.project.agro_retailerapp.utils.LocaleManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import es.dmoral.toasty.Toasty;

public class AgroMainActivity extends AppCompatActivity {
    CardView cardProfile, cardGovtNotice, cardFarmerOrders, cardInfo, cardAddProducts, cardProducts, cardPayments, cardLogout;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    //for double back press to exit
    private static final int TIME_DELAY = 2000;
    private static long back_pressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agro_main);


        getSupportActionBar().setTitle("Agro-Team Panel");


        cardProfile = findViewById(R.id.card_profile);
        cardProducts = findViewById(R.id.card_products);
        cardLogout = findViewById(R.id.card_logout);
        cardAddProducts = findViewById(R.id.card_add_products);
        cardFarmerOrders = findViewById(R.id.card_farmer_order);
        cardInfo = findViewById(R.id.card_info);
        cardGovtNotice = findViewById(R.id.card_govt_notice);


        sp = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();

        cardProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Intent intent=new Intent(AgroMainActivity.this, ProfileActivity.class);
                //  startActivity(intent);

            }
        });

        cardGovtNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Intent intent=new Intent( AgroMainActivity.this, ViewNoticeActivity.class);
                //  startActivity(intent);

            }
        });


        cardInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Intent intent=new Intent( AgroMainActivity.this, CropsActivity.class);
                //  startActivity(intent);

            }
        });


        cardFarmerOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //       Intent intent=new Intent( AgroMainActivity.this, FarmerOrderActivity.class);
                //       startActivity(intent);

            }
        });

        cardProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   Intent intent=new Intent( AgroMainActivity.this, AllProductsActivity.class);
                //   intent.putExtra("type","farmer");
                //    startActivity(intent);

            }
        });

        cardAddProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //     Intent intent=new Intent( AgroMainActivity.this, AddProductActivity.class);
                //     startActivity(intent);

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


   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.language_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.local_english:
                setNewLocale(this, LocaleManager.ENGLISH);
                return true;


            case R.id.local_spanish:
                setNewLocale(this, LocaleManager.BANGLA);
                return true;


        }

        return super.onOptionsItemSelected(item);
    }

    private void setNewLocale(AppCompatActivity mContext, @LocaleManager.LocaleDef String language) {
        LocaleManager.setNewLocale(this, language);
        Intent intent = mContext.getIntent();
        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
    }
*/
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
