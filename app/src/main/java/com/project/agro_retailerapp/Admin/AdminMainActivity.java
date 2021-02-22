package com.project.agro_retailerapp.Admin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.project.agro_retailerapp.Admin.Profile.ProfileAdminActivity;
import com.project.agro_retailerapp.Admin.UserList.AgroListActivity;
import com.project.agro_retailerapp.Admin.UserList.RetailerListActivity;
import com.project.agro_retailerapp.Constant;
import com.project.agro_retailerapp.LoginActivity;
import com.project.agro_retailerapp.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import es.dmoral.toasty.Toasty;

public class AdminMainActivity extends AppCompatActivity {

    CardView cardProfileAdmin, cardServiceProviderAdmin, cardAllUsersAdmin, cardDashboadAdmin, cardLogoutAdmin;

    SharedPreferences sp;
    SharedPreferences.Editor editor;


    String UserCell;

    //for double back press to exit
    private static final int TIME_DELAY = 2000;
    private static long back_pressed;
    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        getSupportActionBar().setTitle("Admin Panel");
        //Fetching mobile from shared preferences
        sp = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String mobile = sp.getString(Constant.CELL_SHARED_PREF, "Not Available");
        UserCell = mobile;

        cardProfileAdmin = findViewById(R.id.card_profileAdmn);
        cardServiceProviderAdmin = findViewById(R.id.card_viewSpAdmn);
        cardAllUsersAdmin = findViewById(R.id.card_allUserAdmn);
        cardLogoutAdmin = findViewById(R.id.card_logoutAdmn);


        sp = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();

        cardProfileAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainActivity.this, ProfileAdminActivity.class);
                startActivity(intent);

            }
        });

        cardServiceProviderAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainActivity.this, AgroListActivity.class);
                startActivity(intent);

            }
        });


        cardAllUsersAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainActivity.this, RetailerListActivity.class);
                startActivity(intent);

            }
        });


        cardLogoutAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                editor.clear();
                editor.apply();
                // finishAffinity();
                Toasty.info(AdminMainActivity.this, "Log out from admin panel!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AdminMainActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        });


    }

    //double backpress to exit
    @Override
    public void onBackPressed() {
        if (back_pressed + TIME_DELAY > System.currentTimeMillis()) {

            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            //finish();
            finishAffinity();

        } else {
            Toasty.info(this, "Press once again to exit!",
                    Toast.LENGTH_SHORT).show();
        }
        back_pressed = System.currentTimeMillis();
    }
}