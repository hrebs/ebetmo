package com.example.ebetmo;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.Arrays;

public class BetsAdapter1 extends RecyclerView.Adapter<BetsAdapter1.ViewHolder> {

    private String[] itemName;
    private String[] win;
    private String[] image1;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    Context context;
    //private RequestQueue queue;
    Dialog dialog;

    // data is passed into the constructor
    BetsAdapter1(Context context, String[] itemName, String[] image1, String[] win) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.itemName = itemName;
        this.image1 = image1;
        this.win = win;
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.single_bettors, parent, false);
        dialog = new Dialog(context);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.itemName.setText(itemName[position]);
        holder.win.setText(win[position]);
        File imgFile = new File(image1[position]);

        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            holder.image.setImageBitmap(myBitmap);
        }

        //int c=0;

    }

    // total number of cells
    @Override
    public int getItemCount() {
        return itemName.length;
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView itemName, win, id;
        ImageView image;

        ViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.bettor_name);
            win = itemView.findViewById(R.id.bettor_slot);
            image = itemView.findViewById(R.id.bettor_image);
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
