package com.example.ebetmo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText email, password;
    TextView signup;
    Button login;
    SessionManager sessionManager;
    DBHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getID();

        try{

            dbHelper = new DBHelper(this);
            sqLiteDatabase = dbHelper.getReadableDatabase();
            sessionManager = new SessionManager(this);
            loadingDialog = new LoadingDialog(this);




        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error. Reload App!", Toast.LENGTH_SHORT).show();

        }

        loadingDialog.showLoading("Please Wait...");
        boolean log = sessionManager.getLogin();
        if(log){startActivity(new Intent(MainActivity.this, home.class));
        }else loadingDialog.hideLoading();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email1 = email.getText().toString().trim();
                String pass = password.getText().toString().trim();
                loadingDialog.showLoading("Loading...");

                if(email1.equals("") || pass.equals("")){
                    Toast.makeText(MainActivity.this, "Insert Email and Password!", Toast.LENGTH_SHORT).show();
                    loadingDialog.hideLoading();
                }else{
                    boolean check_user = dbHelper.check_emails_pass(email1, pass);
                    if(check_user){
                        //Store session
                        sessionManager.setLogin(true);
                        //Store info
                        sessionManager.setEmail(email1);
                        startActivity(new Intent(MainActivity.this, home.class));
                        password.setText("");
                        session(email1);
                        finish();
                    }else {
                        Toast.makeText(MainActivity.this, "Account Doesn't Exist!", Toast.LENGTH_SHORT).show();
                        loadingDialog.hideLoading();
                    }

                }
            }
        });



        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, sign_up.class));
            }
        });
    }

    private void session(String email1) {
        sqLiteDatabase = dbHelper.getWritableDatabase();
        Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM users WHERE email = ?", new String[]{email1});
        try{
            if (c.getCount()>0){
                while(c.moveToNext()){
                    sessionManager.setId(String.valueOf(c.getInt(0)));
                    sessionManager.setName(c.getString(1));
                    sessionManager.setCoin(c.getString(9));
                    sessionManager.setValid(c.getString(7));
                }
                c.close();
            }else{
                Toast.makeText(this, "Failed to store session", Toast.LENGTH_SHORT).show();
            }



        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error: Fetching Session!", Toast.LENGTH_SHORT).show();

        }



    }

    private void getID() {
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login_btn);
        signup = findViewById(R.id.to_signUp);
    }
}