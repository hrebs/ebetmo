package com.example.ebetmo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class terms extends AppCompatActivity {
    Button accept, decline;
    CheckBox check_accept, check_not;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.terms);
        accept = findViewById(R.id.accept_btn);
        decline = findViewById(R.id.decline_btn);
        check_accept = findViewById(R.id.check_accept);
        check_not = findViewById(R.id.check_not);

        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check_accept.isChecked()||check_not.isChecked()){
                    finish();
                }else Toast.makeText(terms.this, "Accept the agreement by checking the boxes.", Toast.LENGTH_SHORT).show();

            }
        });
    }
}