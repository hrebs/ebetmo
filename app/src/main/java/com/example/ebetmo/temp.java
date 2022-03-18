package com.example.ebetmo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class temp extends AppCompatActivity {
    EditText name, age, house, street, brgy, city, province, zip;
    Button driver, sss, passport, voters, postal, HDMF, GOCC, PRC, barangay, national, senior, submit;
    ImageButton back;
    //String ID_Type ,owner_id;
    CardView id_panel;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);
        findId();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FormData.ID_Type = "Driver's License";
                getIdDialog();
            }
        });
        sss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FormData.ID_Type = "SSS";
                getIdDialog();
            }
        });
        passport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FormData.ID_Type = "Passport ID";
                getIdDialog();
            }
        });
        voters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FormData.ID_Type = "Voter's ID";
                getIdDialog();
            }
        });
        postal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FormData.ID_Type="Postal ID";
                getIdDialog();
            }
        });
        HDMF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FormData.ID_Type = "HDMF(Pagibig)";
                getIdDialog();
            }
        });
        GOCC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FormData.ID_Type = "GOCC ID";
                getIdDialog();
            }
        });
        PRC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FormData.ID_Type = "PRC ID";
                getIdDialog();
            }
        });
        barangay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FormData.ID_Type = "Barangay ID";
                getIdDialog();
            }
        });
        national.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FormData.ID_Type = "National ID";
                getIdDialog();
            }
        });
        senior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FormData.ID_Type = "Senior Citizen ID";
                getIdDialog();
            }
        });

    }

    private void getIdDialog(){
        dialog.setContentView(R.layout.pick_id_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        ImageButton close = dialog.findViewById(R.id.close);
        ImageButton pick_image = dialog.findViewById(R.id.pick_image);
        //id_picture = dialog.findViewById(R.id.id_picture);
        Button done = dialog.findViewById(R.id.done_btn);



        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id_panel.setVisibility(View.GONE);
                dialog.dismiss();
                //Toast.makeText(verification.this, FormData.ID_Type, Toast.LENGTH_SHORT).show();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        dialog.show();
    }

    private void findId() {
        id_panel = findViewById(R.id.ID_Panel);
        driver = findViewById(R.id.driver);
        sss = findViewById(R.id.sss);
        passport = findViewById(R.id.passport);
        voters = findViewById(R.id.voters);
        postal = findViewById(R.id.postal);
        HDMF = findViewById(R.id.HDMF);
        GOCC = findViewById(R.id.GOCC);
        PRC = findViewById(R.id.PRC);
        barangay = findViewById(R.id.barangay);
        national = findViewById(R.id.national);
        senior = findViewById(R.id.senior);
        back = findViewById(R.id.back);
        name = findViewById(R.id.name_text);
        age = findViewById(R.id.age_text);
        house = findViewById(R.id.house_text);
        street = findViewById(R.id.street_text);
        brgy = findViewById(R.id.barangay_text);
        city = findViewById(R.id.city_text);
        province = findViewById(R.id.province_text);
        zip = findViewById(R.id.zip_text);
        submit = findViewById(R.id.submit_verification);
    }
}


