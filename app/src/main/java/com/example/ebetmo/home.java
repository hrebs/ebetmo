package com.example.ebetmo;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class home extends AppCompatActivity {
    TextView coin;
    Button account, home, notification;
    RecyclerView recyclerView;
    LinearLayout empty;
    ImageButton popMenu;
    Dialog dialog;

    MyRecyclerViewAdapter itemAdapter;
    private RequestQueue queue;

    @SuppressLint({"SetTextI18n", "SetJavaScriptEnabled"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        queue = Volley.newRequestQueue(this);

        getItem();
        getUsers();
        getBets();
        findId();

        coin.setText("P"+FormData.coin);
        if(coin.getText().toString().equals("P") || coin.getText().toString().equals("P")){
            Intent refresh = new Intent(this, home.class);
            startActivity(refresh);
            finish();
        }

        if(FormData.itemName[0].isEmpty()){
            empty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            Intent refresh = new Intent(this, home.class);
            startActivity(refresh);
            finish();
        }
        else {
            displayItems();
        }

        if(FormData.ifAddedItem){
            FormData.ifAddedItem = false;
            Intent refresh = new Intent(this, home.class);
            startActivity(refresh);
            finish();
        }

        if(FormData.ifRaffle){
            String URL = "http://"+Final_IP.IP_ADDRESS+"/betting/upd_winner.php?win="+FormData.winners+"&id="+FormData.getItemId;
            WebView view1 = findViewById(R.id.webView3);

            view1.loadUrl(URL);
            view1.getSettings().setJavaScriptEnabled(true);

            FormData.ifRaffle = false;
        }

        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), account.class));
                finish();
            }
        });
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), notification.class));
            }
        });

        popMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainMenu(view);
            }
        });
    }

    private void mainMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(home.this,v);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.add_item){
                    if (FormData.profile_info[6].equals("Verified")){
                        startActivity(new Intent(getApplicationContext(), add_item.class));
                    finish();
                    }
                    else Toast.makeText(home.this, "You are not Verified yet.", Toast.LENGTH_SHORT).show();
                }
                if (menuItem.getItemId() == R.id.add_coin){
                    startActivity(new Intent(getApplicationContext(), get_coin.class));
                    finish();
                }
                return false;
            }
        });
        popupMenu.show();

    }


    @SuppressLint("SetTextI18n")
    private void findId() {
        recyclerView = findViewById(R.id.item_rv);
        coin = findViewById(R.id.coin);
        account = findViewById(R.id.account);
        home = findViewById(R.id.home);
        empty = findViewById(R.id.empty);
        popMenu = findViewById(R.id.main_menu);
        notification = findViewById(R.id.notification);

        coin.setText("P"+FormData.coin);
    }

    private void getItem() {
        queue = Volley.newRequestQueue(this);
        String url = "http://"+Final_IP.IP_ADDRESS+"/betting/get_item.php?em=temp";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("list");
                    FormData.itemName = new String[jsonArray.length()];
                    FormData.des = new String[jsonArray.length()];
                    FormData.price1 = new String[jsonArray.length()];
                    FormData.image1 = new String[jsonArray.length()];
                    FormData.slots1 = new String[jsonArray.length()];
                    FormData.id1 = new String[jsonArray.length()];
                    FormData.date = new String[jsonArray.length()];
                    FormData.stats = new String[jsonArray.length()];
                    FormData.win = new String[jsonArray.length()];
                    FormData.type1 = new String[jsonArray.length()];

                    for (int i = 0; i <jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String itn = object.getString("itn");
                        FormData.itemName[i] =itn;
                        String itd = object.getString("itd");
                        FormData.des[i] =itd;
                        String pr = object.getString("pr");
                        FormData.price1[i] =pr;
                        String iim = object.getString("iim");
                        FormData.image1[i] =iim;
                        String sl = object.getString("sl");
                        FormData.slots1[i] =sl;
                        String id = object.getString("id");
                        FormData.id1[i] =id;
                        String dt = object.getString("tm");
                        FormData.date[i] =dt;
                        String st = object.getString("st");
                        FormData.stats[i] =st;
                        String tp = object.getString("tp");
                        FormData.type1[i] =tp;
                        String wn = object.getString("wn");
                        FormData.win[i] =wn;
                        //Log.d("stat",Integer.toString(i)+FormData.stats[i]);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(home.this, "Invalid action.", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(request);
        super.onStart();
    }

    private void displayItems() {


        empty.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

            itemAdapter = new MyRecyclerViewAdapter(this, FormData.itemName, FormData.des, FormData.price1, FormData.image1, FormData.slots1, FormData.id1);

            GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setAdapter(itemAdapter);

    }

    private void getUsers() {
        queue = Volley.newRequestQueue(this);
        String url = "http://"+Final_IP.IP_ADDRESS+"/betting/get_users.php?em=temp";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("list");
                    FormData.user_id = new String[jsonArray.length()];
                    FormData.user_name = new String[jsonArray.length()];
                    FormData.user_image = new String[jsonArray.length()];

                    for (int i = 0; i <jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String id = object.getString("id");
                        FormData.user_id[i] =id;
                        String nm = object.getString("fn");
                        FormData.user_name[i] =nm;
                        String im = object.getString("pf");
                        FormData.user_image[i] =im;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(home.this, "Invalid action.", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(request);
        super.onStart();
    }

    private void getBets() {
        queue = Volley.newRequestQueue(this);
        String url = "http://"+Final_IP.IP_ADDRESS+"/betting/get_bets.php?em=temp";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("list");
                    FormData.slot_number = new String[jsonArray.length()];
                    FormData.owner_name1 = new String[jsonArray.length()];
                    FormData.owner_image1 = new String[jsonArray.length()];
                    FormData.item_id1 = new String[jsonArray.length()];

                    for (int i = 0; i <jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String sl = object.getString("cn");
                        FormData.slot_number[i] =sl;
                        String nm = object.getString("unm");
                        FormData.owner_name1[i] =nm;
                        String im = object.getString("uim");
                        FormData.owner_image1[i] =im;
                        String iid = object.getString("itid");
                        FormData.item_id1[i] =iid;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(home.this, "Invalid action.", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(request);
        super.onStart();
    }



}


