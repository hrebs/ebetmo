package com.example.ebetmo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class view_item extends AppCompatActivity {
    TextView name, des, price, status, slots, date, winner,type,slotNumber,betDate, item_owner;
    ImageButton back;
    ImageView item_image;
    String item_id;
    SQLiteDatabase sqLiteDatabase;
    DBHelper dbHelper;
    LinearLayout manager_button, entry_panel;
    SessionManager sessionManager;
    String current_user_id, owner_id;
    Button edit, start;

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

        if(getIntent().getBundleExtra("item_data")!=null){
            Bundle bundle = getIntent().getBundleExtra("item_data");
            item_id = String.valueOf(bundle.getInt("item_id"));


        }

        try{
            dbHelper = new DBHelper(getApplicationContext());
            sqLiteDatabase = dbHelper.getWritableDatabase();
            sessionManager = new SessionManager(this);
            current_user_id = sessionManager.getId();
            displayItemsInfo(item_id);
            checkManager(item_id);
            checkBet(item_id,current_user_id);
            displaySeller(owner_id);


        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error. Reload App!", Toast.LENGTH_SHORT).show();

        }

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Bundle bundle = new Bundle();
                    bundle.putInt("item_id",Integer.parseInt(item_id));
                    bundle.putInt("owner_id",Integer.parseInt(owner_id));
                    Intent intent=new Intent(getApplicationContext(),raffle.class);
                    intent.putExtra("item_data",bundle);
                    startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                    finish();
                    Toast.makeText(getApplicationContext(), "Error: Try Again!", Toast.LENGTH_SHORT).show();

                }

            }
        });

        item_owner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Bundle bundle = new Bundle();
                    bundle.putInt("user_id",Integer.parseInt(owner_id));
                    Intent intent=new Intent(getApplicationContext(),view_user.class);
                    intent.putExtra("user_data",bundle);
                    startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                    finish();
                    Toast.makeText(getApplicationContext(), "Error: Try Again!", Toast.LENGTH_SHORT).show();

                }
            }
        });

        winner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Bundle bundle = new Bundle();
                    bundle.putInt("item_id",Integer.parseInt(item_id));
                    bundle.putInt("owner_id",Integer.parseInt(owner_id));
                    Intent intent=new Intent(getApplicationContext(),raffle.class);
                    intent.putExtra("item_data",bundle);
                    startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                    finish();
                    Toast.makeText(getApplicationContext(), "Error: Try Again!", Toast.LENGTH_SHORT).show();

                }

            }
        });



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Bundle bundle = new Bundle();
                    bundle.putInt("item_id",Integer.parseInt(item_id));
                    bundle.putString("item_name", name.getText().toString().trim());
                    Intent intent=new Intent(getApplicationContext(),add_item.class);
                    intent.putExtra("item_data",bundle);
                    startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                    finish();
                    Toast.makeText(getApplicationContext(), "Error: Try Again!", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    private void displaySeller(String owner_id) {
        Cursor c = sqLiteDatabase.rawQuery("Select fullName FROM users WHERE id = ?", new String[]{owner_id});
        while(c.moveToNext()) item_owner.setText(c.getString(0));
    }

    private void checkBet(String item_id, String current_user_id) {
        Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM bets WHERE owner_id =? and item_id=?", new String[]{current_user_id,item_id});
        if (c.getCount()>0){
            entry_panel.setVisibility(View.VISIBLE);
            while(c.moveToNext()){
                slotNumber.setText("Slot number: "+c.getString(6));
                betDate.setText(c.getString(7));
            }
            c.close();

        }else{
            entry_panel.setVisibility(View.GONE);
        }
    }

    private void checkManager(String item_id) {
        String item_owner_id = "0";
        Cursor c = sqLiteDatabase.rawQuery("SELECT owner_id FROM items where id=?", new String[]{item_id});
        if (c.getCount()>0){
            while (c.moveToNext()){
                item_owner_id = c.getString(0);
            }
            if (item_owner_id.equals(current_user_id)){
                manager_button.setVisibility(View.VISIBLE);
            }else manager_button.setVisibility(View.GONE);


        }else{
            Toast.makeText(this, "Failed to match ID of manager", Toast.LENGTH_SHORT).show();
            manager_button.setVisibility(View.GONE);
        }
        c.close();
    }

    private void displayItemsInfo(String item_id) {
        Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM items WHERE id =?", new String[]{item_id});
        if (c.getCount()>0){
            while(c.moveToNext()){
                name.setText(c.getString(2));
                des.setText(c.getString(3));
                double getPrice = Double.parseDouble(c.getString(7));
                price.setText("₱"+getPrice);

                String getStatus = c.getString(10);
                if (getStatus.equals("open")) status.setText("Open for Betting");
                else status.setText("Betting is Closed.");

                slots.setText(c.getString(8)+" Slots");
                date.setText(c.getString(6));
                String getWinner = c.getString(9);
                if(getWinner.equals("0")||getWinner.equals("")) winner.setText("Unknown");
                else{
                    winner.setText(getWinner);
                }

                type.setText(c.getString(5));

                byte[]image = c.getBlob(4);
                Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0,image.length);
                item_image.setImageBitmap(bitmap);

                owner_id = c.getString(1);
            }
        }else{
            Toast.makeText(this, "Failed to fetch info.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), home.class));
            finish();
        }
        c.close();
    }
}