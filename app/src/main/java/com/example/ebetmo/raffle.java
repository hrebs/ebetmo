package com.example.ebetmo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class raffle extends AppCompatActivity {
    ImageButton close;
    String item_id, owner_id,winnerNum, current_user_id;
    TextView item_name, item_des, item_price, win_num, slot_total, win_name;
    ImageView item_image;
    RecyclerView recyclerView;

    String win;

    int TotalSlots , bettorCount;
    Button draw, view_winner;
    LoadingDialog loadingDialog;
    Dialog dialog;
    BetsAdapter1 itemAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raffle);

        //getName(FormData.profile_info[0]);



        close = findViewById(R.id.close);
        item_name = findViewById(R.id.item_name);
        item_des = findViewById(R.id.item_des);
        item_price = findViewById(R.id.item_price);
        item_image = findViewById(R.id.item_image);
        win_num = findViewById(R.id.win_num);
        slot_total = findViewById(R.id.slot_total);
        win_name = findViewById(R.id.win_name);
        recyclerView = findViewById(R.id.bettors_rv);
        draw = findViewById(R.id.btn_draw);
        view_winner = findViewById(R.id.btn_view_winner);

        dialog = new Dialog(this);
        view_winner.setVisibility(View.INVISIBLE);

        item_name.setText(FormData.item_info[0]);
        item_des.setText(FormData.item_info[1]);
        File imgFile = new File(FormData.item_info[2]);
        item_price.setText(FormData.item_info[5]);
        win_num.setText(FormData.item_info[7]);

        if(win_num.getText().toString().equals("0")){
            win_num.setText("Undecided");
        }
        else{
            draw.setVisibility(View.GONE);
            view_winner.setVisibility(View.VISIBLE);
            view_winner.setText("Back to Home");
        }

        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            item_image.setImageBitmap(myBitmap);
        }

        displayItems();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        draw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    dialog.setContentView(R.layout.draw_dialog);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                    dialog.setCancelable(true);
                    Button yes, no;
                    yes = dialog.findViewById(R.id.yes_btn);
                    no = dialog.findViewById(R.id.no_btn);

                    yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try{
                                if(FormData.owner_name1[0].isEmpty()){
                                    Toast.makeText(raffle.this, "No bettors. please set bettors first.", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }

                                else {
                                    int min = 1;
                                    int max = Integer.parseInt(FormData.item_info[6]);
                                    int b = (int)(Math.random()*(max-min+1)+min);

                                    win = Integer.toString(b);

                                    win_num.setText(win);
                                    dialog.dismiss();
                                    draw.setVisibility(View.GONE);

                                    int findWinner = Arrays.asList(FormData.slot_number).indexOf(win);

                                    try{
                                        win_name.setText("Winner Name: " + FormData.owner_name1[findWinner]);
                                        close.setVisibility(View.INVISIBLE);

                                        view_winner.setVisibility(View.VISIBLE);
                                        view_winner.setText("Back to Home");

                                        FormData.winners = win;

                                        Toast.makeText(raffle.this, "Congratulations to: " +FormData.owner_name1[findWinner], Toast.LENGTH_SHORT).show();
                                    }
                                    catch (Exception e){
                                        Toast.makeText(raffle.this, "No winner.", Toast.LENGTH_SHORT).show();
                                        FormData.winners = "0";

                                        view_winner.setVisibility(View.VISIBLE);
                                        view_winner.setText("Back to Home");
                                    }
                                }
                            }
                            catch (Exception e){
                                Log.d("err", e.getMessage());


                            }

                        }
                    });

                    no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                }
                catch (Exception e){

                }


            }
        });

        view_winner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FormData.ifRaffle = true;

                startActivity(new Intent(getApplicationContext(), home.class));
                finish();
            }
        });
    }

    private void displayItems() {
        recyclerView.setVisibility(View.VISIBLE);
        //itemAdapter = new BetsAdapter1(this, FormData.changeName, FormData.changeImage, FormData.slot_number);
        itemAdapter = new BetsAdapter1(this, FormData.owner_name1, FormData.owner_image1, FormData.slot_number);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(itemAdapter);
    }
}