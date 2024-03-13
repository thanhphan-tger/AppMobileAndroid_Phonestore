package com.example.mobilecbr.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    EditText username,email,password, confirmpassword;
    Button register;
    VideoView v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);

        linkView();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                String mail = email.getText().toString();
                String confirm = confirmpassword.getText().toString();

                if(user.equals("") || pass.equals("") || mail.equals("") || confirm.equals(""))
                    Toast.makeText(Register.this,"Please enter all the required fields",Toast.LENGTH_SHORT).show();
                else
                {
                    if(pass.equals(confirm))
                    {
                        if (!isEmailValid(mail))
                            Toast.makeText(Register.this,"Your email is not valid!",Toast.LENGTH_SHORT).show();
                        else
                        {
                            checkData(user, pass, mail);
                        }
                    }
                    else
                        Toast.makeText(Register.this,"Your password confirmation is incorrect!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.register_bg);
        v.setVideoURI(uri);
        v.start();
        v.setOnPreparedListener(mediaPlayer -> mediaPlayer.setLooping(true));
    }

    private void checkData(String username, String pass, String mail){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, API.urlCheckEmail + mail, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String firstChar = String.valueOf(response.charAt(0));
                if (firstChar.equalsIgnoreCase("["))
                    Toast.makeText(Register.this,"Your email has already taken!",Toast.LENGTH_SHORT).show();
                else
                    insertUser(username, pass, mail);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(stringRequest);
    }

    private void insertUser(String username, String pass, String mail){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.urlInsertUser, new Response.Listener<String>() {
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

                        Toast.makeText(Register.this,"Register successfully!",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Register.this, MainActivity.class);
                        intent.putExtra("user", new User(Integer.parseInt(id), name, email, pass));
                        startActivity(intent);
                    }
                    else
                        Toast.makeText(Register.this,"Oopsie! Something went wrong",Toast.LENGTH_SHORT).show();
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
                    object.put("email", mail);
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
        username = (EditText) findViewById(R.id.username_input);
        password = (EditText) findViewById(R.id.password_input);
        email = (EditText)findViewById(R.id.email_input);
        confirmpassword = (EditText)findViewById(R.id.confirmpassword_input);
        register = (Button) findViewById(R.id.register_button);
        v = findViewById(R.id.videoView);
    }

    public void redirect_login(View v)
    {
        Intent i = new Intent(this, Login.class);
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