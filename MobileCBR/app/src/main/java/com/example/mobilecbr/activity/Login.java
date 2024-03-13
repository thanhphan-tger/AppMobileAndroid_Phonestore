package com.example.mobilecbr.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mobilecbr.R;
import com.example.mobilecbr.dto.User;
import com.example.mobilecbr.utils.API;
import com.example.mobilecbr.utils.ObjectGlobal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    VideoView v;
    EditText email,password;
    Button login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        linkView();

        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.login_bg);
        v.setVideoURI(uri);
        v.start();
        v.setOnPreparedListener(mediaPlayer -> mediaPlayer.setLooping(true));

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = email.getText().toString();
                String pass = password.getText().toString();
                if(user.equals("") || pass.equals(""))
                    Toast.makeText(Login.this,"Please enter all the required fields",Toast.LENGTH_SHORT).show();
                else
                {
                    if (!isEmailValid(user))
                        Toast.makeText(Login.this,"Your email is not valid!",Toast.LENGTH_SHORT).show();
                    else
                        checkUserLogin(user, pass);
                }
            }
        });
    }

    private void checkUserLogin(String username, String pass) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method. POST, API.urlCheckUser, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject;
                JSONArray jsonArray;
                String firstChar = String.valueOf(response.charAt(0));
                try {
                    if (firstChar.equalsIgnoreCase("["))
                    {
                        jsonArray = new JSONArray(response);
                        jsonObject = jsonArray.getJSONObject(0);
                        String id = jsonObject.getString("userID");
                        String name = jsonObject.getString("username");
                        String email = jsonObject.getString("email");
                        String pass = jsonObject.getString("password");

                        Toast.makeText(Login.this,"Login successfully!",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        intent.putExtra("user", new User(Integer.parseInt(id), name, email, pass));
                        startActivity(intent);
                    }else
                    {
                        jsonObject = new JSONObject(response);
                        if(jsonObject.getString("username").equals("null"))
                            Toast.makeText(Login.this, "Your email or password is incorrect!", Toast.LENGTH_SHORT).show();
                    }
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
                    object.put("username", username);
                    object.put("pass", pass);
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

    private void linkView() {
        email = (EditText) findViewById(R.id.email_input_login);
        password = (EditText) findViewById(R.id.password_input_login);
        login = (Button) findViewById(R.id.login_button);
        v = findViewById(R.id.videoView);
    }

    public void redirect_register(View v)
    {
        Intent i = new Intent(this, Register.class);
        startActivity(i);
    }

    @Override
    protected void onPostResume()
    {
        if(v != null){
            v.start();
        }
        super.onPostResume();
    }

    public boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}