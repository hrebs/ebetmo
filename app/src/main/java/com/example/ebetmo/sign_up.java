package com.example.ebetmo;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class sign_up extends AppCompatActivity {
    EditText name, contact, email, password, confirm, bDate, address;
    Button SignUp;
    ImageView profile;
    String Verification = "none";
    DBHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    TextView to_login;
    ImageButton upload;
    LoadingDialog loadingDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        findId();

        try{
            dbHelper = new DBHelper(this);
            sqLiteDatabase = dbHelper.getReadableDatabase();
            loadingDialog = new LoadingDialog(this);


        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error. Reload App!", Toast.LENGTH_SHORT).show();

        }

        to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(sign_up.this, MainActivity.class));
                finish();
            }
        });

        upload.setOnClickListener(v -> mGetContent .launch("image/*"));

        bDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateTimeDialog(bDate);

            }
        });






        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingDialog.showLoading("Please Wait...");

                Bitmap bitmap=((BitmapDrawable)profile.getDrawable()).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[]image = stream.toByteArray();
                String name1 = name.getText().toString().trim();
                String email1 = email.getText().toString().trim();
                String contact1 = contact.getText().toString().trim();
                String password1 = password.getText().toString().trim();
                String bDate1 = bDate.getText().toString().trim();
                String confirm1 = confirm.getText().toString().trim();
                String address1 = address.getText().toString().trim();
                String coin = "0";

                ContentValues contentValues = new ContentValues();
                contentValues.put("fullName", name1);
                contentValues.put("contact", contact1);
                contentValues.put("email", email1);
                contentValues.put("password", password1);
                contentValues.put("profile", image);
                contentValues.put("b_day", bDate1);
                contentValues.put("address", address1);
                contentValues.put("coin", coin);
                contentValues.put("verified", Verification);

                if(name1.equals("") || email1.equals("") || contact1.equals("") || password1.equals("") || bDate1.equals("")){
                    Toast.makeText(getApplicationContext(), "Please Complete Details", Toast.LENGTH_SHORT).show();
                    loadingDialog.hideLoading();
                }else{
                    if(confirm1.equals(password1)){
                        boolean check = dbHelper.check_email(email1);
                        if(!check){
                            long result = sqLiteDatabase.insert("users",null,contentValues);
                            if(result==-1){
                                loadingDialog.hideLoading();
                                Toast.makeText(getApplicationContext(), "Something problem in Inserting Data", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getApplicationContext(), "Registered Successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(sign_up.this, MainActivity.class));
                                finish();
                            }
                        }else{
                            loadingDialog.hideLoading();
                            Toast.makeText(getApplicationContext(), "Email Already Exist!", Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        loadingDialog.hideLoading();
                        Toast.makeText(getApplicationContext(), "Password not match!", Toast.LENGTH_SHORT).show();
                    }

                }






            }
        });


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


    private void findId() {
        name = findViewById(R.id.fName);
        contact = findViewById(R.id.contact);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirm = findViewById(R.id.confirm);
        bDate = findViewById(R.id.bDay);
        SignUp = findViewById(R.id.signUp_btn);
        profile = findViewById(R.id.profile);
        to_login = findViewById(R.id.to_login);
        address = findViewById(R.id.address);
        upload = findViewById(R.id.upload);

    }

    ActivityResultLauncher<String> mGetContent =registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    profile.setImageURI(result);

                }
            }
    );
}