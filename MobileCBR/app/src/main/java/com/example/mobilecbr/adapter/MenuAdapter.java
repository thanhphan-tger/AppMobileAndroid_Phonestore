package com.example.mobilecbr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.mobilecbr.R;
import com.example.mobilecbr.dto.Category;

import java.util.ArrayList;

public class MenuAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<Category> lstCat;

    public MenuAdapter(Context mContext, ArrayList<Category> lstCat) {
        this.mContext = mContext;
        this.lstCat = lstCat;
    }

    @Override
    public int getCount() {
        return lstCat.size();
    }

    @Override
    public Object getItem(int i) {
        return lstCat.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null)
            view = LayoutInflater.from(mContext).inflate(R.layout.list_menu, viewGroup, false);

        ImageView imageView = view.findViewById(R.id.img_IconMenu);
        TextView textView = view.findViewById(R.id.textviewItem);

        Category category = (Category) lstCat.get(i);

        Glide.with(mContext).load(category.get_image()).into(imageView);
        textView.setText(category.get_name());

        return view;
    }
}
