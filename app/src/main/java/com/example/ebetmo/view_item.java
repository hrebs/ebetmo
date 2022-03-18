package com.example.ebetmo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;

public class view_item extends AppCompatActivity {
    TextView name, des, price, status, slots, date, winner,type,slotNumber,betDate, item_owner;
    ImageButton back;
    ImageView item_image;
    String item_id;
    LinearLayout manager_button, entry_panel;
    String current_user_id, owner_id;
    Button edit, start;
    private RequestQueue queue;

    //TextView labels[] = new TextView[10];

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item);
        name = findViewById(R.id.item_name);
        des = findViewById(R.id.item_des);
        price = findViewById(R.id.item_price);
        status = findViewById(R.id.item_status);
        slots = findViewById(R.id.item_slots);
        date = findViewById(R.id.item_date);
        winner = findViewById(R.id.item_winner);
        type = findViewById(R.id.item_type);
        back = findViewById(R.id.back);
        item_image = findViewById(R.id.item_image);
        manager_button = findViewById(R.id.manager_button);
        edit = findViewById(R.id.edit_btn);
        entry_panel = findViewById(R.id.entry_panel);
        slotNumber = findViewById(R.id.slot_num);
        betDate = findViewById(R.id.bet_date);
        item_owner = findViewById(R.id.item_owner);
        start = findViewById(R.id.start_btn);

        getOneItem(FormData.getItemId);

        name.setText(FormData.item_info[0]);
        des.setText(FormData.item_info[1]);
        File imgFile = new File(FormData.item_info[2]);
        type.setText(FormData.item_info[3]);
        date.setText(FormData.item_info[4]);
        price.setText(FormData.item_info[5]);
        slots.setText(FormData.item_info[6]);
        winner.setText(FormData.item_info[7]);
        status.setText(FormData.item_info[8]);
        item_owner.setText(FormData.profile_info[0]);
        slotNumber.setText(FormData.bet_info[4]);
        betDate.setText(getCurrentLocalDateTimeStamp());

        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            item_image.setImageBitmap(myBitmap);
        }

        if(winner.getText().toString().equals("0")){
            winner.setText("Undecided");
        }

        if(slotNumber.getText().toString().equals("")){
            slotNumber.setText("In raffle.");
        }


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Intent intent=new Intent(getApplicationContext(),raffle.class);
                    startActivity(intent);

            }
        });

        item_owner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        winner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void getOneItem(String getId){
        try{
            int id = Arrays.asList(FormData.id1).indexOf(getId);

            String temp;

            temp = FormData.itemName[id];
            FormData.item_info[0] = temp;

            temp = FormData.des[id];
            FormData.item_info[1] = temp;

            temp = FormData.image1[id];
            FormData.item_info[2] = temp;

            temp = FormData.type1[id];
            FormData.item_info[3] = temp;

            temp = FormData.date[id];
            FormData.item_info[4] = temp;

            temp = FormData.price1[id];
            FormData.item_info[5] = temp;

            temp = FormData.slots1[id];
            FormData.item_info[6] = temp;

            temp = FormData.win[id];
            FormData.item_info[7] = temp;

            temp = FormData.stats[id];
            FormData.item_info[8] = temp;
        }
        catch (Exception e){

        }
    }

    private void getBet() {
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
                Toast.makeText(view_item.this, "Invalid action.", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(request);
        super.onStart();
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getCurrentLocalDateTimeStamp() {
        //ZoneId z = ZoneId.of("Asia/Singapore");
        return LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm"));
    }
}