package com.example.ebetmo;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class add_item extends AppCompatActivity {
    private final int REQUEST_CAMERA = 1, REQUEST_GALLERY = 0;
    private Bitmap itemBitmap;
    EditText name, des, price,type, time, slots;
    ImageView image;
    Button submit_item, upload, save;
    ImageButton back;
    private RequestQueue queue;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        getID();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FormData.clearItem();
                Intent intent = new Intent(add_item.this, home.class);
                startActivity(intent);
                finish();
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

    public void addItem(View view){

        String name1 = FormData.item_info[0] = name.getText().toString().trim();
        String des1 = FormData.item_info[1] = des.getText().toString().trim();
        String image1 = FormData.item_info[2] = FormData.imageLocation;
        String type1 = FormData.item_info[3] = type.getText().toString().trim();
        String time1 = FormData.item_info[4] = time.getText().toString().trim();
        String price1 = FormData.item_info[5] = price.getText().toString().trim();
        String slots1 = FormData.item_info[6] = slots.getText().toString().trim();
        String win1 = FormData.item_info[7] = "0";
        String stat1 = FormData.item_info[8] = "Open";

        if(name1.equals("") || des1.equals("") || type1.equals("") || time1.equals("") || price1.equals("")){
            Toast.makeText(add_item.this, "Please fill up the form.", Toast.LENGTH_SHORT).show();
        }

        else {
            final String URL = "http://" + Final_IP.IP_ADDRESS + "/betting/add_item.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, response -> {
                Log.d("res", response);
                if (response.equals("success")) {
                    Toast.makeText(add_item.this, "Added item Successfully.", Toast.LENGTH_SHORT).show();

                    FormData.clearItem();

                    FormData.ifAddedItem = true;
                    Intent intent = new Intent(add_item.this, account.class);
                    startActivity(intent);
                    finish();
                } else if (response.equals("failure")) {
                    Toast.makeText(add_item.this, "Something wrong, try again.", Toast.LENGTH_SHORT).show();
                }
            }, error -> Toast.makeText(add_item.this, error.toString().trim(), Toast.LENGTH_SHORT).show()) {
                @Override
                public Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> data = new HashMap<>();

                    for(int i=0;i<FormData.item_info.length;i++){
                        data.put(FormData.item_labels[i], FormData.item_info[i]);
                    }

                    return data;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
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

    private void getPic()
    {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType(getString(R.string.galleryType));
        startActivityForResult(galleryIntent.createChooser(galleryIntent, "Select Images"), REQUEST_GALLERY);
    }

    private void takePic()
    {
        Intent takePicIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicIntent, REQUEST_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
        {
            if(requestCode == REQUEST_CAMERA)
            {

                itemBitmap = (Bitmap) data.getExtras().get("data");
                image.setImageBitmap(itemBitmap);

                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                Uri tempUri = getImageUri(getApplicationContext(), itemBitmap);

                // CALL THIS METHOD TO GET THE ACTUAL PATH
                File finalFile = new File(getRealPathFromURI(tempUri));

                FormData.imageLocation = finalFile.getAbsolutePath();


            }
            else if(requestCode == REQUEST_GALLERY && data != null)
            {
                try {
                    itemBitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(),
                            data.getData());
                    image.setImageBitmap(itemBitmap);

                    // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                    Uri tempUri = getImageUri(getApplicationContext(), itemBitmap);

                    // CALL THIS METHOD TO GET THE ACTUAL PATH
                    File finalFile = new File(getRealPathFromURI(tempUri));

                    FormData.imageLocation = finalFile.getAbsolutePath();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public void insertPic1(View view){
        final CharSequence[] items = {"Camera", "Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Upload Item Image");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(items[which].equals("Camera"))
                    takePic();
                else if(items[which].equals("Gallery"))
                    getPic();
                else if(items[which].equals("Cancel"))
                    dialog.dismiss();
            }
        });
        builder.show();
    }
}