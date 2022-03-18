package com.example.ebetmo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class sign_up extends AppCompatActivity {

    private final int REQUEST_CAMERA = 1, REQUEST_GALLERY = 0;
    private Bitmap itemBitmap;

    EditText name, contact, email, password, confirm, bDate, address;
    ImageView profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name = findViewById(R.id.fName);
        contact = findViewById(R.id.contact);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirm = findViewById(R.id.confirm);
        bDate = findViewById(R.id.bDay);
        address = findViewById(R.id.address);
        profile = findViewById(R.id.profile);

        bDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateTimeDialog(bDate);
            }
        });
    }

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
                profile.setImageBitmap(itemBitmap);

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
                    profile.setImageBitmap(itemBitmap);

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

    public void insertPic(View view){
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

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
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

    private void showDateTimeDialog(EditText bDate) {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yy");

                bDate.setText(simpleDateFormat.format(calendar.getTime()));

            }
        };
        new DatePickerDialog(sign_up.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }



    public void register1(View view){
        String name1 = FormData.profile_info[0] = name.getText().toString().trim();
        String email1 = FormData.profile_info[3] = email.getText().toString().trim();
        String contact1 = FormData.profile_info[1] = contact.getText().toString().trim();
        String password1 = FormData.profile_info[4] = password.getText().toString().trim();
        String bDate1 = FormData.profile_info[7] = bDate.getText().toString().trim();
        String confirm1 = confirm.getText().toString().trim();
        String address1 = FormData.profile_info[2] = address.getText().toString().trim();
        String coin = FormData.coin = "0";
        String verify = FormData.profile_info[6] = "Unverified";
        String image = FormData.profile_info[5] = FormData.imageLocation;

        if(name1.equals("") || email1.equals("") || contact1.equals("") || password1.equals("") || bDate1.equals("")){
            Toast.makeText(sign_up.this, "Please fill up the form.", Toast.LENGTH_SHORT).show();
        }

        else {
            final String URL = "http://" + Final_IP.IP_ADDRESS + "/betting/signup.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, response -> {
                Log.d("res", response);
                if (response.equals("success")) {
                    Toast.makeText(sign_up.this, "Register Successfully.", Toast.LENGTH_SHORT).show();

                    FormData.clearPF();
                    Intent intent = new Intent(sign_up.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else if (response.equals("failure")) {
                    Toast.makeText(sign_up.this, "Something wrong, try again.", Toast.LENGTH_SHORT).show();
                }
            }, error -> Toast.makeText(sign_up.this, error.toString().trim(), Toast.LENGTH_SHORT).show()) {
                @Override
                public Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> data = new HashMap<>();

                    for(int i=0;i<FormData.profile_info.length;i++){
                        data.put(FormData.profile_labels[i], FormData.profile_info[i]);
                    }
                    data.put(FormData.coin_nm, coin);

                    return data;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }
    public void login1(View view){
        Intent intent = new Intent(sign_up.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}