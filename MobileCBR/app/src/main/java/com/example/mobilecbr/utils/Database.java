package com.example.mobilecbr.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.mobilecbr.dto.Product;

import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {
    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "Product_Manager";

    public Database(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    private String CREATE_TABLE = "CREATE TABLE Product (proid INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "name TEXT, price INTEGER, image TEXT, desc TEXT, catid INTEGER)";

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Product");
    }

    public void insertProduct(Product product)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", product.get_name());
        values.put("price", product.get_price());
        values.put("image", product.get_image());
        values.put("desc", product.get_desc());
        values.put("catid", product.get_catid());

        sqLiteDatabase.insert("Product", null, values);
        sqLiteDatabase.close();
    }

    public ArrayList<Product> getAllProduct()
    {
        ArrayList<Product> lstProduct = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM Product", null);
        while (cursor.moveToNext())
        {
            Product product = new Product();
            product.set_id(cursor.getInt(0));
            product.set_name(cursor.getString(1));
            product.set_price(cursor.getInt(2));
            product.set_image(cursor.getString(3));
            product.set_desc(cursor.getString(4));
            product.set_catid(cursor.getInt(5));

            lstProduct.add(product);
        }
        return lstProduct;
    }
}
