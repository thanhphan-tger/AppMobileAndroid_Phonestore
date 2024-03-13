package com.example.mobilecbr.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.mobilecbr.R;
import com.example.mobilecbr.dto.Cart;
import com.example.mobilecbr.dto.Category;
import com.example.mobilecbr.dto.Product;
import com.example.mobilecbr.utils.API;
import com.example.mobilecbr.utils.ObjectGlobal;
import com.example.mobilecbr.utils.checkNetwork;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProductDetail extends AppCompatActivity {

    RequestQueue requestQueue;
    JsonArrayRequest arrayRequest;
    Toolbar toolbar;
    ImageView imageView;
    TextView tvProName;
    TextView tvProPrice;
    TextView tvProDesc;
    MaterialButton button;
    Product pro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_product_detail);

        linkView();

        if(checkNetwork.haveNetwork(this))
        {
            actionBar();
            getData();
            addProToCart();
        }
        else
            checkNetwork.showReport(this, "No internet.");
    }

    private void addProToCart() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestQueue requestQueueA = Volley.newRequestQueue(getApplicationContext());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, API.urlInsertCart, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
                    protected Map<String, String> getParams() throws AuthFailureError {
                        JSONArray array = new JSONArray();
                        JSONObject object = new JSONObject();
                        try {
                            object.put("userid", ObjectGlobal.user.getUserID());
                            object.put("proid", pro.get_id());
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        array.put(object);
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("cart", array.toString());
                        return hashMap;
                    }
                };
                requestQueueA.add(stringRequest);

                ObjectGlobal.lstCart = new ArrayList<>();
                loadProCart();
                new AlertDialog.Builder(view.getContext())
                        .setMessage("Thêm vào giỏ hàng thành công.")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                loadProCart();
                                Intent intent = new Intent(getApplicationContext(), CartActivity.class);
                                startActivity(intent);
                            }
                        })
                        .show();
            }
        });
    }

    public void loadProCart(){
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        arrayRequest = new JsonArrayRequest(API.urlAllProCart + ObjectGlobal.user.getUserID(), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if(response != null)
                {
                    try {
                        for(int i = 0; i < response.length(); i++)
                        {
                            JSONObject jsonObject = response.getJSONObject(i);
                            int id = Integer.parseInt(jsonObject.getString("proid"));
                            String name = jsonObject.getString("name");
                            int price = Integer.parseInt(jsonObject.getString("price"));
                            String image = jsonObject.getString("image");
                            int quan = Integer.parseInt(jsonObject.getString("quantity"));

                            if(ObjectGlobal.lstCart.size() <= 0)
                                ObjectGlobal.lstCart.add(new Cart(id, name, price, image, quan));
                            else
                            {
                                int count = 0;
                                for(int j = 0; j < ObjectGlobal.lstCart.size(); j++)
                                {
                                    if(ObjectGlobal.lstCart.get(j).get_proid() == id)
                                        count++;
                                }
                                if(count == 0)
                                    ObjectGlobal.lstCart.add(new Cart(id, name, price, image, quan));
                            }
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(arrayRequest);
    }

    private void getData() {
        pro = (Product) this.getIntent().getSerializableExtra("product");

        tvProName.setText(pro.get_name());

        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tvProPrice.setText("Giá : " + decimalFormat.format(pro.get_price()) + "Đ");

        tvProDesc.setText(pro.get_desc());

        Glide.with(this).load(pro.get_image()).into(imageView);
    }

    private void actionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.baseline_keyboard_backspace_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void linkView() {
        toolbar = (Toolbar) findViewById(R.id.toolbarDetail);
        imageView = (ImageView) findViewById(R.id.imgPro_Detail);
        tvProName = (TextView) findViewById(R.id.tvProName_Detail);
        tvProPrice = (TextView) findViewById(R.id.tvProPrice_Detail);
        tvProDesc = (TextView) findViewById(R.id.tvProDesc_Detail);
        button = (MaterialButton) findViewById(R.id.btnAddCart);
    }
}