package com.example.mobilecbr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.mobilecbr.R;
import com.example.mobilecbr.activity.CartActivity;
import com.example.mobilecbr.dto.Cart;
import com.example.mobilecbr.utils.API;
import com.example.mobilecbr.utils.ObjectGlobal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CartAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<Cart> lstCart;

    public CartAdapter(Context mContext, ArrayList<Cart> lstCart) {
        this.mContext = mContext;
        this.lstCart = lstCart;
    }

    @Override
    public int getCount() {
        return lstCart.size();
    }

    @Override
    public Object getItem(int i) {
        return lstCart.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null)
            view = LayoutInflater.from(mContext).inflate(R.layout.list_cart, viewGroup, false);

        TextView tvName = view.findViewById(R.id.tvProNameCart);
        TextView tvPrice = view.findViewById(R.id.tvProPriceCart);
        TextView tvQuantity = view.findViewById(R.id.tvQuantity);
        ImageView imageView = view.findViewById(R.id.imgProImgCart);

        Cart cart = (Cart) lstCart.get(i);

        tvName.setText(cart.get_name());

        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tvPrice.setText(decimalFormat.format(cart.get_price()) + "Đ");

        tvQuantity.setText(String.valueOf(cart.get_quantity()));
        Glide.with(mContext).load(cart.get_image()).into(imageView);

        Button btnMinus = view.findViewById(R.id.btnMinus);
        Button btnAdd = view.findViewById(R.id.btnAdd);
        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int oldQuan = cart.get_quantity();
                if(oldQuan - 1 >= 1)
                {
                    ObjectGlobal.lstCart.get(i).set_quantity(oldQuan - 1);
                    tvQuantity.setText(String.valueOf(ObjectGlobal.lstCart.get(i).get_quantity()));
                    CartActivity.sumMoney();
                    updateQuantity(cart.get_proid(), ObjectGlobal.lstCart.get(i).get_quantity());
                }
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int oldQuan = cart.get_quantity();
                if(oldQuan + 1 <= 10)
                {
                    ObjectGlobal.lstCart.get(i).set_quantity(oldQuan + 1);
                    tvQuantity.setText(String.valueOf(ObjectGlobal.lstCart.get(i).get_quantity()));
                    CartActivity.sumMoney();
                    updateQuantity(cart.get_proid(), ObjectGlobal.lstCart.get(i).get_quantity());
                }
            }
        });
        return view;
    }

    private void updateQuantity(int proid, int quan){
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.urlUpdateCart, new Response.Listener<String>() {
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
                    object.put("quan", quan);
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
}
