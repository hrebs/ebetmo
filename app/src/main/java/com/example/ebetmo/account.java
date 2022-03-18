package com.example.ebetmo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class account extends AppCompatActivity {
    TextView coin, name;
    ImageView user_image;
    ImageButton close;
    Button verification_btn, logout_btn, terms_btn, to_profile;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        dialog = new Dialog(this);
        findId();

        coin.setText("P"+FormData.coin);
        name.setText(FormData.profile_info[0]);

        File imgFile = new File(FormData.profile_info[5]);

        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            user_image.setImageBitmap(myBitmap);
        }

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), home.class));
                finish();
            }
        });

        to_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), edit_profile.class));
            }
        });

        terms_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), terms.class));
            }
        });
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });
        verification_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(FormData.profile_info[6].equals("Verified")){
                    Toast.makeText(account.this, "You are already verified!", Toast.LENGTH_SHORT).show();
                }
                else {
                    dialog.setContentView(R.layout.verification_dialog);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                    dialog.setCancelable(true);
                    Button ok = dialog.findViewById(R.id.verify_btn);

                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            dialog.dismiss();

                            Intent refresh = new Intent(getApplicationContext(), verification.class);
                            startActivity(refresh);
                            //finish();
                        }
                    });

                    dialog.show();
                }

            }
        });
    }

    private void findId() {
        coin = findViewById(R.id.coin);
        name = findViewById(R.id.user_name);
        user_image = findViewById(R.id.user_image);
        close = findViewById(R.id.close);
        verification_btn = findViewById(R.id.verification_btn);
        logout_btn = findViewById(R.id.logout_btn);
        terms_btn = findViewById(R.id.terms_btn);
        to_profile = findViewById(R.id.profile_btn);
    }

}


