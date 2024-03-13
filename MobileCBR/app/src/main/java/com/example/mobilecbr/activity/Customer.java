package com.example.mobilecbr.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mobilecbr.R;
import com.example.mobilecbr.utils.API;
import com.example.mobilecbr.utils.ObjectGlobal;
import com.example.mobilecbr.utils.checkNetwork;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Customer extends AppCompatActivity {

    EditText edtName, edtAddress, edtPhone;
    Button btnConfirm, btnCancel;
    RequestQueue requestQueue;
    StringRequest stringRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_customer);

        linkView();
        eventBTNCancel();
        eventBTNConfirm();
    }

    private void eventBTNConfirm() {
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edtName.getText().toString();
                String address = edtAddress.getText().toString();
                String phone = edtPhone.getText().toString();

                if(name.equals("") || address.equals("") || phone.equals(""))
                    checkNetwork.showReport(Customer.this, "Vui lòng điền đầy đủ thông tin.");
                else
                {
                    if(phone.length() < 10)
                        checkNetwork.showReport(Customer.this, "Số điện thoại không hợp lệ.");
                    else
                    {
                        updateUser(name, address, phone);
                        createBill();
                    }
                }
            }
        });
    }

    private void createBill() {
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        stringRequest = new StringRequest(Request.Method.POST, API.urlInsertBill, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    int billID = jsonObject.getInt("billID");

                    for(int i = 0; i < ObjectGlobal.lstCart.size(); i++)
                        insertBillDetail(billID, ObjectGlobal.lstCart.get(i).get_proid(), ObjectGlobal.lstCart.get(i).get_quantity());

                    addNotification();
                    Intent intent = new Intent(Customer.this, MainActivity.class);
                    startActivity(intent);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
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
                    object.put("summoney", sumMoney());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                array.put(object);

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("bill", array.toString());

                return hashMap;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void insertBillDetail(int billID, int proid, int quan){
        RequestQueue requestQueueD = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequestD = new StringRequest(Request.Method.POST, API.urlInsertBillDetail, new Response.Listener<String>() {
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
                    object.put("proid", proid);
                    object.put("billid", billID);
                    object.put("quantity", quan);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                array.put(object);

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("billdetail", array.toString());

                return hashMap;
            }
        };
        requestQueueD.add(stringRequestD);
    }

    public int sumMoney() {
        int sum = 0;
        for(int i = 0; i < ObjectGlobal.lstCart.size(); i++)
            sum +=(ObjectGlobal.lstCart.get(i).get_price() * ObjectGlobal.lstCart.get(i).get_quantity());

        return sum;
    }

    private void updateUser(String name, String address, String phone){
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        stringRequest = new StringRequest(Request.Method.POST, API.urlUpdateUser, new Response.Listener<String>() {
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
                    object.put("fullname", name);
                    object.put("address", address);
                    object.put("phone", phone);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                array.put(object);

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("user", array.toString());

                return hashMap;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void eventBTNCancel() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void addNotification() {
        NotificationCompat.Builder notificationBuilder ;
        NotificationManager notificationManager;

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {

            NotificationChannel nc = notificationManager.getNotificationChannel(getString(R.string.app_name));

            notificationManager = getSystemService(NotificationManager.class);

            notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), getString(R.string.app_name));

            notificationBuilder.setChannelId(getString(R.string.app_name));
            if (nc == null)
            {
                nc = new NotificationChannel(getString(R.string.app_name), getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH);

                nc.setDescription("Badge Notifications");
                nc.enableLights(true);
                nc.setShowBadge(true);
                nc.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

                notificationManager.createNotificationChannel(nc);

            }
        }
        else
        {
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationBuilder = new NotificationCompat.Builder(getApplicationContext());
        }

        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Thông báo")
                .setContentText("Đặt hàng thành công")
                .setAutoCancel(true);

        notificationManager.notify(0, notificationBuilder.build());
    }

    private void linkView() {
        edtName = (EditText) findViewById(R.id.edtCusName);
        edtAddress = (EditText) findViewById(R.id.edtCusAddress);
        edtPhone = (EditText) findViewById(R.id.edtCusPhone);

        btnConfirm = (Button) findViewById(R.id.btnConfim);
        btnCancel = (Button) findViewById(R.id.btnCancel);
    }
}