package com.example.ebetmo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    String email, password;
    EditText etEmail, etPassword;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etEmail = findViewById(R.id.email);
        etPassword = findViewById(R.id.password);

        //getItem();
    }

    public void login(View view) {
        final String URL = "http://"+Final_IP.IP_ADDRESS+"/betting/login.php";
        email = etEmail.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        if(!email.equals("") && !password.equals("")){
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, response -> {
                Log.d("res", response);

                if (response.equals("success")) {
                    FormData.email = email;

                    getPF();

                    Toast.makeText(MainActivity.this, "Login Successfully.", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(getApplicationContext(), home.class));
                    finish();
                } else if (response.equals("failure")) {
                    Toast.makeText(MainActivity.this, "Invalid Username/Password", Toast.LENGTH_SHORT).show();
                }
            }, error -> Toast.makeText(MainActivity.this, error.toString().trim(), Toast.LENGTH_SHORT).show()){
                @Override
                public Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> data = new HashMap<>();
                    data.put("em", email);
                    data.put("pw", password);
                    return data;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }else{
            Toast.makeText(this, "Fields can not be empty!", Toast.LENGTH_SHORT).show();
        }
    }

    public void register(View view){
        Intent intent = new Intent(MainActivity.this, sign_up.class);
        startActivity(intent);
        finish();
    }

    private void getPF() {
        queue = Volley.newRequestQueue(this);
        String url = "http://"+Final_IP.IP_ADDRESS+"/betting/get_pf.php?em="+FormData.email;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    int i;
                    String temp;
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("list");
                    JSONObject object = jsonArray.getJSONObject(0);

                    temp = object.getString(FormData.id_nm);
                    FormData.id = temp;

                    Log.d("id",FormData.id);

                    temp = object.getString(FormData.coin_nm);
                    FormData.coin = temp;

                    for(i=0;i<FormData.profile_labels.length;i++){
                            temp = object.getString(FormData.profile_labels[i]);
                            FormData.profile_info[i] = temp;

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Invalid action.", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(request);
        super.onStart();
    }
}