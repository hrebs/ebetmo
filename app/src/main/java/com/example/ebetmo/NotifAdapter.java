package com.example.ebetmo;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class NotifAdapter extends RecyclerView.Adapter<NotifAdapter.ViewHolder> {

    private String[] itemName;
    private String[] win;
    private String[] date;
    private String[] image1;
    private String[] id1;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    Context context;
    //private RequestQueue queue;
    Dialog dialog;

    // data is passed into the constructor
    NotifAdapter(Context context, String[] itemName, String[] image1, String[] win, String[] date, String[] id1) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.itemName = itemName;
        this.image1 = image1;
        this.win = win;
        this.date = date;
        this.id1 = id1;
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.single_notify, parent, false);
        dialog = new Dialog(context);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.itemName.setText(itemName[position]);
        holder.win.setText(win[position]);
        holder.date.setText(date[position]);
        File imgFile = new File(image1[position]);
        holder.id.setText(id1[position]);

        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            holder.image.setImageBitmap(myBitmap);
        }

        if(holder.win.getText().toString().equals("0")){
            holder.win.setText("Undecided");
        }

        holder.image.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"NonConstantResourceId", "SetTextI18n", "ResourceAsColor"})
            @Override
            public void onClick(View view) {
                //Context context = Home.getApplicationContext();

                switch(view.getId()) {
                    case R.id.notify_image:
                        String id1 = holder.id.getText().toString();
                        FormData.getItemId = id1;
                        break;
                }

                context.startActivity(new Intent(context, view_item.class));
            }
        });
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return itemName.length;
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView itemName, win, date, id;
        ImageView image;

        ViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.item_name);
            win = itemView.findViewById(R.id.item_winner);
            date = itemView.findViewById(R.id.item_date);
            image = itemView.findViewById(R.id.notify_image);
            id = itemView.findViewById(R.id.item_id1);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null){
                mClickListener.onItemClick(view, getAdapterPosition());
            }
            //FormData.getItemId = id.getText().toString();

        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return itemName[id];
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public void addBet(View view){

    }

}
