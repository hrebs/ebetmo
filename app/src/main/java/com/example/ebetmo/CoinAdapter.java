package com.example.ebetmo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CoinAdapter extends RecyclerView.Adapter<CoinAdapter.ViewHolder>{
    Context context;
    int singleData;
    ArrayList<ModelCoin> modelArrayList;
    SQLiteDatabase sqLiteDatabase;
    DBHelper dbHelper;

    public CoinAdapter(Context context, int singleData, ArrayList<ModelCoin> modelArrayList, SQLiteDatabase sqLiteDatabase, DBHelper dbHelper) {
        this.context = context;
        this.singleData = singleData;
        this.modelArrayList = modelArrayList;
        this.sqLiteDatabase = sqLiteDatabase;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.single_topup,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ModelCoin modelCoin = modelArrayList.get(position);
        String amount = modelCoin.getAmount();
        holder.name.setText("You Top Up ₱"+amount+" to your coin.");
        holder.date.setText(modelCoin.getDate());


    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, date;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.coin_title);
            date = itemView.findViewById(R.id.coin_date);
        }
    }
}
