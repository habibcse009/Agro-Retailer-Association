package com.project.agro_retailerapp.AgroTeam.Products;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.project.agro_retailerapp.AgroTeam.Orders.OrderActivity;
import com.project.agro_retailerapp.Constant;
import com.project.agro_retailerapp.R;

import androidx.appcompat.app.AppCompatActivity;

public class ProductDescriptionActivity extends AppCompatActivity {

    String name, price, image, description, agro_cell, id;
    ImageView imgProduct;
    TextView txtName, txtPrice, txtDescription, txtOrder, txtViewReview;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_description);

        imgProduct = findViewById(R.id.img_product);
        txtName = findViewById(R.id.txt_product_name);
        txtPrice = findViewById(R.id.txt_price);
        txtDescription = findViewById(R.id.txt_description);
        txtOrder = findViewById(R.id.txt_order);
        txtViewReview = findViewById(R.id.txt_view_review);
        txtViewReview.setVisibility(View.INVISIBLE);


        //Fetching cell from shared preferences
        sharedPreferences = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String account_type = sharedPreferences.getString(Constant.AC_TYPE_SHARED_PREF, "Not Available");

        if (account_type.equals("Agro")) {
            txtOrder.setVisibility(View.GONE);
        }

        name = getIntent().getExtras().getString("name");
        price = getIntent().getExtras().getString("price");
        description = getIntent().getExtras().getString("description");
        image = getIntent().getExtras().getString("image");
        agro_cell = getIntent().getExtras().getString("agro_cell");
        id = getIntent().getExtras().getString("id");

        String url = Constant.MAIN_URL + "/product_image/" + image;

        txtName.setText(name);
        txtPrice.setText(Constant.KEY_CURRENCY + price + "/KG");
        txtDescription.setText(description);

        Glide.with(ProductDescriptionActivity.this)
                .load(url)
                .placeholder(R.drawable.loading)
                .error(R.drawable.not_found)
                .into(imgProduct);


        txtOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDescriptionActivity.this, OrderActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("price", price);
                intent.putExtra("agro_cell", agro_cell);
                intent.putExtra("id", id);

                startActivity(intent);

            }
        });


        txtViewReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //   Intent intent=new Intent(ProductDescriptionActivity.this,ViewReviewActivity.class);
                //   intent.putExtra("agro_cell",agro_cell);
                //    startActivity(intent);


            }
        });

    }
}
