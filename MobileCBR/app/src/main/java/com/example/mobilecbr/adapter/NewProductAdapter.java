package com.example.mobilecbr.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mobilecbr.R;
import com.example.mobilecbr.dto.Product;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class NewProductAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<Product> lstProduct;

    public NewProductAdapter(Context mContext, ArrayList<Product> lstProduct) {
        this.mContext = mContext;
        this.lstProduct = lstProduct;
    }

    @Override
    public int getCount() {
        return lstProduct.size();
    }

    @Override
    public Object getItem(int i) {
        return lstProduct.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null)
            view = LayoutInflater.from(mContext).inflate(R.layout.list_pronew, viewGroup, false);

        TextView tvName = view.findViewById(R.id.tvProNameHome);
        TextView tvPrice = view.findViewById(R.id.tvProPriceHome);
        TextView tvDesc = view.findViewById(R.id.tvProDescHome);
        ImageView imageView = view.findViewById(R.id.imgProImgHome);
        Product pro = (Product) lstProduct.get(i);

        tvName.setText(pro.get_name());

        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tvPrice.setText("Giá : " + decimalFormat.format(pro.get_price()) + "Đ");

        tvDesc.setMaxLines(2);
        tvDesc.setEllipsize(TextUtils.TruncateAt.END);
        tvDesc.setText(pro.get_desc());

        Glide.with(mContext).load(pro.get_image()).into(imageView);

        return view;
    }
}
