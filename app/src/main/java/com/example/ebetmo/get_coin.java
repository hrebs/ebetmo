package com.example.ebetmo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class get_coin extends AppCompatActivity {
    Button five, ten, twenty, fifty, one_hundred, two_hundred, five_hundred, one_thousand, two_thousand, three_thousand,four_thousand,five_thousand, proceed;

    Button setCoin[]= new Button[12];
    EditText my_coin;
    ImageButton close;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_coin);

        getCoinUser();

        setCoin[1] = findViewById(R.id.ten);
        setCoin[2] = findViewById(R.id.twenty);
        setCoin[3] = findViewById(R.id.fifty);
        setCoin[4] = findViewById(R.id.two_hundred);
        setCoin[5] = findViewById(R.id.one_hundred);
        setCoin[6] = findViewById(R.id.five_hundred);
        setCoin[7] = findViewById(R.id.one_thousand);
        setCoin[8] = findViewById(R.id.two_thousand);
        setCoin[9] = findViewById(R.id.three_thousand);
        setCoin[10] = findViewById(R.id.five_thousand);
        my_coin = findViewById(R.id.add_coin);
        close = findViewById(R.id.close);
        setCoin[11] = findViewById(R.id.four_thousand);
        setCoin[0] = findViewById(R.id.five);
        proceed = findViewById(R.id.proceed);

        for (int i = 0; i<setCoin.length;i++){
            int finalI = i;
            setCoin[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    my_coin.setText(setCoin[finalI].getText().toString().replace("₱",""));
                }
            });
        }


        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double total, prev, add;
                String sTotal;

                prev = Double.parseDouble(FormData.coin);
                add = Double.parseDouble(my_coin.getText().toString());

                total = prev + add;

                sTotal = String.valueOf(total);
                FormData.coin = sTotal;

                String URL = "http://"+Final_IP.IP_ADDRESS+"/betting/upd_coin.php?em="+FormData.email+"&cin="+sTotal;

                WebView view1 = findViewById(R.id.webView);

                view1.loadUrl(URL);
                view1.getSettings().setJavaScriptEnabled(true);
                Toast.makeText(get_coin.this, "Top up successfully.", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(getApplicationContext(), home.class));
                    finish();

            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), home.class));
                finish();
            }
        });

    }

    private void getCoinUser() {
        queue = Volley.newRequestQueue(this);
        String url = "http://"+Final_IP.IP_ADDRESS+"/betting/get_coin.php?em="+FormData.email;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    String temp;
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("list");
                    JSONObject object = jsonArray.getJSONObject(0);

                    temp = object.getString("cin");
                    FormData.coin = temp;

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(get_coin.this, "Invalid action.", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(request);
        super.onStart();
    }

}