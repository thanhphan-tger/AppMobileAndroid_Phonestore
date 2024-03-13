package com.example.mobilecbr.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.mobilecbr.R;
import com.example.mobilecbr.adapter.MenuAdapter;
import com.example.mobilecbr.adapter.ProductAdapter;
import com.example.mobilecbr.dto.Category;
import com.example.mobilecbr.dto.Product;
import com.example.mobilecbr.utils.API;
import com.example.mobilecbr.utils.checkNetwork;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProductActivity extends AppCompatActivity {

    Toolbar toolbar;
    ListView lvPro;
    ArrayList<Product> lstPro;
    int catID;
    ProductAdapter adapterPro;
    RequestQueue requestQueue;
    JsonArrayRequest arrayRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_product);

        linkView();
        if(checkNetwork.haveNetwork(this))
        {
            catID = this.getIntent().getIntExtra("catID", -1);
            actionBar();
            loadData();
            showProductDetail();
        }
        else
            checkNetwork.showReport(this, "No internet.");
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.btnViewCart:
            {
                Intent intent = new Intent(this, CartActivity.class);
                startActivity(intent);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void showProductDetail() {
        lvPro.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ProductActivity.this, ProductDetail.class);
                intent.putExtra("product", lstPro.get(i));
                startActivity(intent);
            }
        });
    }

    private void loadData() {
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        arrayRequest = new JsonArrayRequest(API.urlAllProduct + catID, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if(response != null)
                {
                    for(int i = 0; i < response.length(); i++)
                    {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            Product pro = new Product();
                            pro.set_id(jsonObject.getInt("proid"));
                            pro.set_name(jsonObject.getString("name"));
                            pro.set_price(jsonObject.getInt("price"));
                            pro.set_image(jsonObject.getString("image"));
                            pro.set_desc(jsonObject.getString("description"));
                            pro.set_catid(jsonObject.getInt("catID"));
                            lstPro.add(pro);
                            adapterPro.notifyDataSetChanged();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
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
        toolbar = (Toolbar) findViewById(R.id.toolbarProduct);
        lvPro = (ListView) findViewById(R.id.listviewProduct);

        lstPro = new ArrayList<>();
        adapterPro = new ProductAdapter(getApplicationContext(), lstPro);
        lvPro.setAdapter(adapterPro);
    }
}