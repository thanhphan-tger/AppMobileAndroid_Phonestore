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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ViewFlipper;

import com.android.volley.RequestQueue;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.mobilecbr.R;
import com.example.mobilecbr.adapter.MenuAdapter;
import com.example.mobilecbr.adapter.NewProductAdapter;
import com.example.mobilecbr.dto.Cart;
import com.example.mobilecbr.dto.Category;
import com.example.mobilecbr.dto.Product;
import com.example.mobilecbr.dto.User;
import com.example.mobilecbr.utils.API;
import com.example.mobilecbr.utils.Database;
import com.example.mobilecbr.utils.ObjectGlobal;
import com.example.mobilecbr.utils.checkNetwork;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RequestQueue requestQueue;
    JsonArrayRequest arrayRequest;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ArrayList<Category> lstCat;
    MenuAdapter adapterMenu;
    ListView lvMenu, lvNewPro;
    ViewFlipper viewFlipper;
    Database db;
    ArrayList<Product> lstPro;
    NewProductAdapter adapterNewPro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        linkView();

        if(checkNetwork.haveNetwork(this))
        {
            if(ObjectGlobal.user == null)
                ObjectGlobal.user = (User) this.getIntent().getSerializableExtra("user");

            actionBar();
            loadMenu();
            loadViewFlipper();
            loadNewProduct();
            showProductDetail();
            loadProCart();
            catchOnItemListView();
        }
        else
            checkNetwork.showReport(this, "No internet.");
    }

    @Override
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
                loadProCart();
                //ObjectGlobal.lstCart = lstCart;
                Intent intent = new Intent(MainActivity.this, CartActivity.class);
                startActivity(intent);
            }
        }
        return super.onOptionsItemSelected(item);
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

    private void catchOnItemListView() {
        lvMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i)
                {
                    case 0:
                    {
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent);
                        drawerLayout.closeDrawer(GravityCompat.START);
                    }break;
                    case 1:
                    {
                        Intent intent = new Intent(MainActivity.this, Contact.class);
                        startActivity(intent);
                        drawerLayout.closeDrawer(GravityCompat.START);
                    }break;
                    case 2:
                    {
                        Intent intent = new Intent(MainActivity.this, Infomation.class);
                        startActivity(intent);
                        drawerLayout.closeDrawer(GravityCompat.START);
                    }break;
                    case 3: case 4:
                    {
                        loadProCart();
                        //ObjectGlobal.lstCart = lstCart;
                        Intent intent = new Intent(MainActivity.this, ProductActivity.class);
                        intent.putExtra("catID", lstCat.get(i).get_id());
                        startActivity(intent);
                        drawerLayout.closeDrawer(GravityCompat.START);
                    }break;
                    case 5:
                    {
                        ObjectGlobal.user = null;
                        ObjectGlobal.lstCart = new ArrayList<>();
                        Intent intent = new Intent(MainActivity.this, Login.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        drawerLayout.closeDrawer(GravityCompat.START);
                    }break;
                }
            }
        });
    }

    private void showProductDetail() {
        lvNewPro.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), ProductDetail.class);
                intent.putExtra("product", lstPro.get(i));
                startActivity(intent);
            }
        });
    }

    private void loadNewProduct() {
        lstPro = db.getAllProduct();
        adapterNewPro = new NewProductAdapter(this, lstPro);
        lvNewPro.setAdapter(adapterNewPro);
    }

    private void loadViewFlipper() {
        ArrayList<String> lstImg = new ArrayList<>();
        lstImg.add("https://cdn.tgdd.vn/2023/04/banner/SMARTPAY-x-MWGWEBBANNER380x200-380x200.jpg");
        lstImg.add("https://images.fpt.shop/unsafe/fit-in/384x180/filters:quality(90):fill(white)/fptshop.com.vn/Uploads/Originals/2023/4/11/638168181753256156_H3_384x180.png");
        lstImg.add("https://cdn.tgdd.vn/2023/04/banner/380x200px-380x200-5.png");
        lstImg.add("https://cdn.tgdd.vn/2023/03/banner/380x200px-380x200-8.png");

        for(int i = 0; i < lstImg.size(); i++)
        {
            ImageView imageView = new ImageView(getApplicationContext());
            Glide.with(getApplicationContext()).load(lstImg.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setAutoStart(true);
        viewFlipper.setFlipInterval(5000);

        Animation anim_slide_in = AnimationUtils.loadAnimation(this, R.anim.slide_in);
        Animation anim_slide_out = AnimationUtils.loadAnimation(this, R.anim.slide_out);

        viewFlipper.setInAnimation(anim_slide_in);
        viewFlipper.setOutAnimation(anim_slide_out);
    }

    private void loadMenu() {
        lstCat.add(new Category(3, "Trang chủ", "https://icons.iconarchive.com/icons/fps.hu/free-christmas-flat-circle/512/home-icon.png"));
        lstCat.add(new Category(4, "Liên hệ", "https://cdn1.iconfinder.com/data/icons/mix-color-3/502/Untitled-12-512.png"));
        lstCat.add(new Category(5, "Thông tin", "https://cdn2.iconfinder.com/data/icons/perfect-flat-icons-2/512/User_info_man_male_profile_information.png"));


        requestQueue = Volley.newRequestQueue(getApplicationContext());
        arrayRequest = new JsonArrayRequest(API.urlCategory, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if(response != null)
                {
                    for(int i = 0; i < response.length(); i++)
                    {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            int id = jsonObject.getInt("catID");
                            String name = jsonObject.getString("catName");
                            String image = jsonObject.getString("catImg");
                            lstCat.add(new Category(id, name, image));
                            adapterMenu.notifyDataSetChanged();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    lstCat.add(new Category(6, "Đăng xuất", "https://icons.iconarchive.com/icons/hopstarter/soft-scraps/128/Button-Log-Off-icon.png"));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(arrayRequest);
        adapterMenu = new MenuAdapter(this, lstCat);
        lvMenu.setAdapter(adapterMenu);
    }

    private void actionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void linkView() {
        toolbar = (Toolbar) findViewById(R.id.toolbarHome);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        viewFlipper = (ViewFlipper) findViewById(R.id.viewflipper);

        db = new Database(this);
        //insertData();

        lstCat = new ArrayList<>();
        lvMenu = (ListView) findViewById(R.id.listviewMenu);

        lstPro = new ArrayList<>();
        lvNewPro = (ListView) findViewById(R.id.listviewPro_Home);
    }

    private void insertData() {
        db.insertProduct(new Product("Điện thoại iPhone 14 512GB", 27990000, "https://cdn.tgdd.vn/Products/Images/42/289670/iphone-14-den-glr-4.jpg", "Tại sự kiện giới thiệu sản phẩm mới hàng năm đến từ nhà Apple thì chiếc iPhone 14 512GB cũng đã chính thức được cho ra mắt, đây được xem là thời điểm kết thúc mọi lời đồn đoán bấy lâu nay khi toàn bộ thông số đã đều lộ diện. Hứa hẹn sẽ trở thành một trong những sản phẩm thu hút nhiều sự quan tâm nhất trong năm 2022.", 1));
        db.insertProduct(new Product("Điện thoại Samsung Galaxy S23 5G", 16690000, "https://cdn.tgdd.vn/Products/Images/42/264060/samsung-galaxy-s23-xanh-4.jpg", "Samsung Galaxy S23 5G 128GB chắc hẳn không còn là cái tên quá xa lạ đối với các tín độ công nghệ hiện nay, được xem là một trong những gương mặt chủ chốt đến từ nhà Samsung với cấu hình mạnh mẽ bậc nhất, camera trứ danh hàng đầu cùng lối hoàn thiện vô cùng sang trọng và hiện đại.", 2));
        db.insertProduct(new Product("Điện thoại Samsung Galaxy Z Fold4", 34990000, "https://cdn.tgdd.vn/Products/Images/42/250625/samsung-galaxy-z-flod-4-den-7.jpg", "Galaxy Z Fold4 (2022) ra mắt với ngoại hình gần như là không đổi khi so với Galaxy Z Fold3, nếu bạn chỉ nhìn bề ngoài thì bạn sẽ khó lòng phân biệt được 2 sản phẩm này. Máy vẫn sử dụng khung viền Armor Aluminum bền bỉ, mặt kính màn hình phụ là Corning Gorilla Glass Victus+, mặt kính màn hình chính là Ultra Thin Glass.", 2));
        db.insertProduct(new Product("Điện thoại iPhone 14 Pro Max 128GB", 27090000, "https://cdn.tgdd.vn/Products/Images/42/251192/iphone-14-pro-bac-1-2.jpg", "iPhone 14 Pro Max một siêu phẩm trong giới smartphone được nhà Táo tung ra thị trường vào tháng 09/2022. Máy trang bị con chip Apple A16 Bionic vô cùng mạnh mẽ, đi kèm theo đó là thiết kế hình màn hình mới, hứa hẹn mang lại những trải nghiệm đầy mới mẻ cho người dùng iPhone.", 1));
    }
}