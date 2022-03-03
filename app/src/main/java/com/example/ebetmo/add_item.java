package com.example.ebetmo;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class add_item extends AppCompatActivity {
    SessionManager sessionManager;
    DBHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    EditText name, des, price,type, time, slots;
    ImageView image;
    String owner_id;
    Button submit_item, upload, save;
    ImageButton back;
    LoadingDialog loadingDialog;
    String item_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        getID();
        try{


            dbHelper = new DBHelper(this);
            sqLiteDatabase = dbHelper.getWritableDatabase();
            sessionManager = new SessionManager(this);
            owner_id = sessionManager.getId();
            loadingDialog = new LoadingDialog(this);
            slots.setText("0");

        }catch (Exception e){

            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error. Reload App!", Toast.LENGTH_SHORT).show();
            finish();

        }

        if(getIntent().getBundleExtra("item_data")!=null){
            Bundle bundle = getIntent().getBundleExtra("item_data");
            item_id = String.valueOf(bundle.getInt("item_id"));
            save.setVisibility(View.VISIBLE);
            submit_item.setVisibility(View.GONE);



            Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM items WHERE id=?", new String[]{item_id});
            if (c.getCount()>0){
                while(c.moveToNext()){
                    name.setText(c.getString(2));
                    des.setText(c.getString(3));
                    price.setText(c.getString(7));
                    type.setText(c.getString(5));
                    time.setText(c.getString(6));
                    slots.setText(c.getString(8));

                    byte[] image1 = c.getBlob(4);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(image1, 0,image1.length);
                    image.setImageBitmap(bitmap);


                }

            }else {
                Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
                finish();
            }



        }

        upload.setOnClickListener(v -> mGetContent .launch("image/*"));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        submit_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingDialog.showLoading("Please Wait...");

                Bitmap bitmap=((BitmapDrawable)image.getDrawable()).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[]item_image = stream.toByteArray();

                String named = name.getText().toString().trim();
                String des1 = des.getText().toString().trim();
                String price1 = price.getText().toString().trim();
                String type1 = type.getText().toString().trim();
                String time1 = time.getText().toString().trim();
                String slots1= slots.getText().toString().trim();
                String status = "open";
                String winner = "0";

                ContentValues contentValues = new ContentValues();
                contentValues.put("owner_id", owner_id);
                contentValues.put("item_name", named);
                contentValues.put("item_description", des1);
                contentValues.put("item_image", item_image);
                contentValues.put("type", type1);
                contentValues.put("time", time1);
                contentValues.put("price", price1);
                contentValues.put("slots", slots1);
                contentValues.put("status", status);
                contentValues.put("winner", winner);

                if(named.equals("")||des1.equals("")||type1.equals("")||time1.equals("")||price1.equals("")||slots1.equals("")){
                    Toast.makeText(add_item.this, "Please Complete Details!", Toast.LENGTH_SHORT).show();
                    loadingDialog.hideLoading();
                }

                double getSlot = Double.parseDouble(slots1);

                    if (getSlot > 100){
                        loadingDialog.hideLoading();
                        Toast.makeText(add_item.this, "Maximum slots is up to 100 only", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        if (named.equals("")||des1.equals("")||price1.equals("")||type1.equals("")||time1.equals("")||slots1.equals("")){
                            Toast.makeText(add_item.this, "Please complete details!", Toast.LENGTH_SHORT).show();
                        }else{
                            long result = sqLiteDatabase.insert("items",null,contentValues);
                            if(result==-1){
                                Toast.makeText(getApplicationContext(), "Something problem in Inserting Data!", Toast.LENGTH_SHORT).show();
                                loadingDialog.hideLoading();
                            }else{
                                Toast.makeText(getApplicationContext(), "Item Posted.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(add_item.this, home.class));
                                finish();
                            }
                        }
                    }










            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingDialog.showLoading("Please Wait...");

                Bitmap bitmap=((BitmapDrawable)image.getDrawable()).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[]item_image = stream.toByteArray();

                String named = name.getText().toString().trim();
                String des1 = des.getText().toString().trim();
                String price1 = price.getText().toString().trim();
                String type1 = type.getText().toString().trim();
                String time1 = time.getText().toString().trim();
                String slots1= slots.getText().toString().trim();

                ContentValues contentValues = new ContentValues();
                contentValues.put("item_name", named);
                contentValues.put("item_description", des1);
                contentValues.put("item_image", item_image);
                contentValues.put("type", type1);
                contentValues.put("time", time1);
                contentValues.put("price", price1);
                contentValues.put("slots", slots1);

                if(named.equals("")||des1.equals("")||type1.equals("")||time1.equals("")||price1.equals("")||slots1.equals("")){
                    Toast.makeText(add_item.this, "Please Complete Details!", Toast.LENGTH_SHORT).show();
                    loadingDialog.hideLoading();
                }else{
                    double getSlot = Double.parseDouble(slots1);
                    if (getSlot > 100){
                        loadingDialog.hideLoading();
                        Toast.makeText(add_item.this, "Maximum slots is up to 100 only", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        if (named.equals("")||des1.equals("")||price1.equals("")||type1.equals("")||time1.equals("")||slots1.equals("")){
                            Toast.makeText(add_item.this, "Please complete details!", Toast.LENGTH_SHORT).show();
                        }else{
                            long result = sqLiteDatabase.update("items",contentValues,"id="+item_id,null);
                            if(result==-1){
                                Toast.makeText(getApplicationContext(), "Something problem in Updating Data!", Toast.LENGTH_SHORT).show();
                                loadingDialog.hideLoading();
                            }else{
                                Toast.makeText(getApplicationContext(), "Item is Updated.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(add_item.this, home.class));
                                finish();
                            }
                        }
                    }

                }




            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateTimeDialog(time);
            }
        });


    }

    private void showDateTimeDialog(EditText time) {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        calendar.set(Calendar.MINUTE,minute);

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yy HH:mm");

                        time.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                };

                new TimePickerDialog(add_item.this, timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE), false).show();
            }
        };
        new DatePickerDialog(add_item.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    private void getID() {

        name = findViewById(R.id.item_name);
        des = findViewById(R.id.item_des);
        price = findViewById(R.id.item_price);
        type = findViewById(R.id.item_type);
        time = findViewById(R.id.item_date);
        slots = findViewById(R.id.item_slots);
        image = findViewById(R.id.item_image);
        submit_item = findViewById(R.id.submit_btn);
        back = findViewById(R.id.back);
        upload = findViewById(R.id.upload_image);
        save = findViewById(R.id.save_btn);
    }

    ActivityResultLauncher<String> mGetContent =registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    image.setImageURI(result);

                }
            }
    );
}