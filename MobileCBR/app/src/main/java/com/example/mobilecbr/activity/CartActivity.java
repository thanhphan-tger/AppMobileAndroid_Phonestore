package com.example.mobilecbr.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mobilecbr.R;
import com.example.mobilecbr.adapter.CartAdapter;
import com.example.mobilecbr.dto.Cart;
import com.example.mobilecbr.utils.API;
import com.example.mobilecbr.utils.ObjectGlobal;
import com.example.mobilecbr.utils.checkNetwork;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CartActivity extends AppCompatActivity {

    Toolbar toolbar;
    ListView lvCart;
    ArrayList<Cart> lstCart;
    TextView tvReport;
    static TextView tvSumMoney;
    Button btnPayBill, btnHome;
    CartAdapter adapter;
    static ArrayList<Cart> lstCartTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_cart);

        linkView();
        if(checkNetwork.haveNetwork(this))
        {
            actionBar();

            loadData();
            checkData();
            sumMoney();
            eventButton();
            catchOnItemListView();
        }
        else
            checkNetwork.showReport(this, "No internet.");
    }

    private void catchOnItemListView() {
        lvCart.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                new AlertDialog.Builder(CartActivity.this)
                        .setTitle("Thông báo")
                        .setMessage("Bạn có chắc là muốn xóa sản phẩm này khỏi giỏ hàng ?")
                        .setPositiveButton("Không", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setNegativeButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteProduct(ObjectGlobal.lstCart.get(pos).get_proid());
                                ObjectGlobal.lstCart.remove(pos);
                                lstCart = ObjectGlobal.lstCart;
                                adapter.notifyDataSetChanged();
                                sumMoney();
                                checkData();
                            }
                        })
                        .show();
            }
        });
    }

    private void deleteProduct(int proid)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.urlDeleteProCart, new Response.Listener<String>() {
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
                    object.put("proid", proid);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                array.put(object);
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("cart", array.toString());
                return hashMap;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void eventButton() {
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        btnPayBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lstCart.size() <= 0)
                    checkNetwork.showReport(CartActivity.this, "Không có sản phẩm nào trong giỏ hàng");
                else
                {
                    Intent intent = new Intent(getApplicationContext(), Customer.class);
                    startActivity(intent);
                }
            }
        });
    }

    public static void sumMoney() {
        int sum = 0;
        for(int i = 0; i < lstCartTemp.size(); i++)
            sum +=(lstCartTemp.get(i).get_price() * lstCartTemp.get(i).get_quantity());

        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tvSumMoney.setText(decimalFormat.format(sum) + "Đ");
    }

    private void loadData() {
        lstCart = ObjectGlobal.lstCart;
        lstCartTemp = ObjectGlobal.lstCart;
        adapter = new CartAdapter(getApplicationContext(), lstCart);
        lvCart.setAdapter(adapter);
    }


    private void checkData() {
        if(lstCart.size() <= 0)
        {
            tvReport.setVisibility(View.VISIBLE);
            lvCart.setVisibility(View.INVISIBLE);
        }
        else
        {
            tvReport.setVisibility(View.INVISIBLE);
            lvCart.setVisibility(View.VISIBLE);
        }
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
        toolbar = (Toolbar) findViewById(R.id.toolbarCart);
        lvCart = (ListView) findViewById(R.id.listviewProCart);
        tvReport = (TextView) findViewById(R.id.tvReport);
        tvSumMoney = (TextView) findViewById(R.id.tvSumMoney);
        btnPayBill = (Button) findViewById(R.id.btnPayBill);
        btnHome = (Button) findViewById(R.id.btnHome);

        lstCart = new ArrayList<>();
    }
}