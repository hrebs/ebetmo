package com.example.ebetmo;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private String[] itemName;
    private String[] des;
    private String[] price;
    private String[] image1;
    private String[] slots1;
    private String[] id1;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    Context context;
    private RequestQueue queue;
    Dialog dialog;

    // data is passed into the constructor
    MyRecyclerViewAdapter(Context context, String[] itemName, String[] des, String[] price, String[] image, String[] slots, String[] id1) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.itemName = itemName;
        this.des = des;
        this.price = price;
        this.image1 = image;
        this.slots1 = slots;
        this.id1 = id1;
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.single_item, parent, false);
        dialog = new Dialog(context);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemName.setText(itemName[position]);
        holder.des.setText(des[position]);
        holder.price.setText(price[position]);
        File imgFile = new File(image1[position]);
        holder.slots.setText(slots1[position] +" slot/s");
        holder.id.setText(id1[position]);

        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            holder.image.setImageBitmap(myBitmap);
        }

        int c=0;

        holder.image.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"NonConstantResourceId", "SetTextI18n", "ResourceAsColor"})
            @Override
            public void onClick(View view) {
                //Context context = Home.getApplicationContext();

                switch(view.getId()) {
                    case R.id.image_item:
                        String id1 = holder.id.getText().toString();
                        FormData.getItemId = id1;
                        break;
                }

                Log.d("res", FormData.getItemId);

                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetDialogTheme);
                View v = bottomSheetDialog.getLayoutInflater().inflate(R.layout.bottom_sheet_layout,null);
                ImageView itemImage = v.findViewById(R.id.item_image);
                TextView itemName, itemDes, itemPrice,itemSlots, itemDate, close_txt, id;
                itemName = v.findViewById(R.id.item_name);
                itemDes = v.findViewById(R.id.item_des);
                itemPrice = v.findViewById(R.id.item_price);
                itemSlots = v.findViewById(R.id.item_slots);
                itemDate = v.findViewById(R.id.item_date);
                Button bet = v.findViewById(R.id.bet);
                EditText choose = v.findViewById(R.id.choose_input);
                WebView view1 = v.findViewById(R.id.webView1);
                TextView more = v.findViewById(R.id.more);
                LinearLayout bet_panel = v.findViewById(R.id.bet_panel);
                close_txt = v.findViewById(R.id.close_betting);
                ImageButton info = v.findViewById(R.id.view_info);
                //id = findViewById(R.id.item_id);

                getOneItem(FormData.getItemId);

                //Toast.makeText(context, FormData.getItemId, Toast.LENGTH_SHORT).show();

                File imgFile1 = new File(FormData.item_info[2]);
                if(imgFile1.exists()){
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile1.getAbsolutePath());
                    itemImage.setImageBitmap(myBitmap);
                }

                itemName.setText(FormData.item_info[0]);
                itemDes.setText(FormData.item_info[1]);
                itemDate.setText(FormData.item_info[4]);
                itemPrice.setText(FormData.item_info[5]);
                itemSlots.setText(FormData.item_info[6] + " slot/s");

                //FormData.coinsofItem = FormData.item_info[5];
                //FormData.coinsofUser = FormData.coin;

                FormData.getItemPrice = FormData.item_info[5];

                int in = Arrays.asList(FormData.max_bet).indexOf(FormData.getItemId);

                try{
                    FormData.getTempId = FormData.max_bet[in];
                    Log.d("yes", FormData.max_bet[in] + " " + FormData.getItemId);
                }catch (Exception e){
                    Log.d("err", e.toString());
                }


                if(FormData.getTempId == FormData.getItemId){
                    close_txt.setVisibility(View.VISIBLE);
                    close_txt.setText("Bet Successfully.");
                    close_txt.setBackgroundColor(R.color.green);
                    bet.setEnabled(false);
                }
                else {
                    Log.d("no", FormData.getTempId+ " " +FormData.getItemId);
                    close_txt.setVisibility(View.INVISIBLE);
                }

                bottomSheetDialog.setContentView(v);
                bottomSheetDialog.show();

                info.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.setContentView(R.layout.bet_info);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                        dialog.setCancelable(true);
                        Button ok = dialog.findViewById(R.id.ok_btn);

                        ok.setOnClickListener(new View.OnClickListener() {
                            @SuppressLint("ResourceAsColor")
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                });

                bet.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @SuppressLint("SetJavaScriptEnabled")
                    @Override
                    public void onClick(View view) {
                        String choose1 = choose.getText().toString().trim();

                        boolean ifSlot = true;

                        int count = Integer.parseInt(FormData.item_info[6]);

                        if(choose1.equals("")){
                            Toast.makeText(context, "Please fill up the bet.", Toast.LENGTH_SHORT).show();
                        }

                        else {
                            try{
                                for(int i =0;i<FormData.slot_number.length;i++){
                                    if(choose1.equals(FormData.slot_number[i])){
                                        Toast.makeText(context, "Slot already taken.", Toast.LENGTH_SHORT).show();
                                        ifSlot = false;
                                        break;
                                    }

                                }

                                if(ifSlot){
                                    int sl = Integer.parseInt(FormData.item_info[6]);
                                    int ch = Integer.parseInt(choose1);

                                    double userCoin = Double.parseDouble(FormData.coin);
                                    double itemCoin = Double.parseDouble(FormData.item_info[5]);



                                    if(userCoin>=itemCoin){
                                        if(ch == 0){
                                            Toast.makeText(context, "Please select Range from 1 to " + FormData.item_info[6], Toast.LENGTH_SHORT).show();
                                        }
                                        else if(ch>sl || ch<0){
                                            Toast.makeText(context, "Invalid slot!", Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            dialog.setContentView(R.layout.bet_dialog);
                                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                                            dialog.setCancelable(true);
                                            Button ok = dialog.findViewById(R.id.ok_btn);

                                            ok.setOnClickListener(new View.OnClickListener() {
                                                @SuppressLint("ResourceAsColor")
                                                @Override
                                                public void onClick(View view) {
                                                    for(int i=0;i<FormData.max_bet.length;i++){
                                                        if(FormData.max_bet[i].isEmpty()){
                                                            FormData.max_bet[i] = FormData.getItemId;
                                                            break;
                                                        }
                                                    }

                                                    //FormData.ifBet = true;

                                                    close_txt.setVisibility(View.VISIBLE);
                                                    close_txt.setText("Bet Successfully.");
                                                    close_txt.setBackgroundColor(R.color.green);
                                                    bet.setEnabled(false);
                                                    dialog.dismiss();

                                                    Intent refresh = new Intent(context, home.class);
                                                    context.startActivity(refresh);
                                                }
                                            });

                                            int in = Arrays.asList(FormData.itemName).indexOf(FormData.item_info[0]);

                                            Log.d("stat",FormData.stats[in]);
                                            String stat = FormData.stats[in];
                                            String img = FormData.image1[in];

                                            FormData.bet_info[0] = FormData.getItemId;
                                            FormData.bet_info[1] = FormData.id;
                                            FormData.bet_info[2] = img;
                                            FormData.bet_info[3] = stat;
                                            FormData.bet_info[4] = choose1;
                                            FormData.bet_info[5] = getCurrentLocalDateTimeStamp();
                                            FormData.bet_info[6] = FormData.profile_info[0];
                                            FormData.bet_info[7] = FormData.profile_info[5];

                                            final String URL = "http://" + Final_IP.IP_ADDRESS + "/betting/add_bet.php";
                                            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, response -> {
                                                Log.d("res", response);
                                                if (response.equals("success")) {
                                                    Toast.makeText(context, "Bet Successfully.", Toast.LENGTH_SHORT).show();

                                                } else if (response.equals("failure")) {
                                                    Toast.makeText(context, "Something wrong, try again.", Toast.LENGTH_SHORT).show();
                                                }
                                            }, error -> Toast.makeText(context, error.toString().trim(), Toast.LENGTH_SHORT).show()) {
                                                @Override
                                                public Map<String, String> getParams() throws AuthFailureError {
                                                    Map<String, String> data = new HashMap<>();

                                                    for(int i=0;i<FormData.bet_info.length;i++){
                                                        data.put(FormData.bet_labels[i], FormData.bet_info[i]);
                                                    }

                                                    return data;
                                                }
                                            };
                                            RequestQueue requestQueue = Volley.newRequestQueue(context);
                                            requestQueue.add(stringRequest);

                                            double coinPrice = Double.parseDouble(FormData.getItemPrice);
                                            double coinUser = Double.parseDouble(FormData.coin);

                                            double total = coinUser - coinPrice;

                                            String sTotal = Double.toString(total);

                                            FormData.coin = sTotal;

                                            String URL1 = "http://"+Final_IP.IP_ADDRESS+"/betting/upd_coin.php?em="+FormData.email+"&cin="+sTotal;

                                            view1.loadUrl(URL1);
                                            view1.getSettings().setJavaScriptEnabled(true);

                                            dialog.show();
                                        }
                                    }
                                    else{
                                        Toast.makeText(context, "Out of coin!", Toast.LENGTH_SHORT).show();
                                    }
                                }





                            }
                            catch (Exception e){
                                Toast.makeText(context, "No decimal points please!", Toast.LENGTH_SHORT).show();
                            }


                        }

                    }
                });


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
        TextView itemName, des, price, slots, id;
        ImageView image;

        ViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.item_name);
            des = itemView.findViewById(R.id.item_des);
            price = itemView.findViewById(R.id.item_price);
            slots = itemView.findViewById(R.id.slots);
            image = itemView.findViewById(R.id.image_item);
            id = itemView.findViewById(R.id.item_id);
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

    public void getOneItem(String getId){
        int id = Arrays.asList(FormData.id1).indexOf(getId);

        String temp;

                    temp = FormData.itemName[id];
                    FormData.item_info[0] = temp;

                    temp = FormData.des[id];
                    FormData.item_info[1] = temp;

                    temp = FormData.image1[id];
                    FormData.item_info[2] = temp;

                    temp = FormData.type1[id];
                    FormData.item_info[3] = temp;

                    temp = FormData.date[id];
                    FormData.item_info[4] = temp;

                    temp = FormData.price1[id];
                    FormData.item_info[5] = temp;

                    temp = FormData.slots1[id];
                    FormData.item_info[6] = temp;

                    temp = FormData.win[id];
                    FormData.item_info[7] = temp;

                    temp = FormData.stats[id];
                    FormData.item_info[8] = temp;
    }

    @SuppressLint("SimpleDateFormat")
    public String getCurrentTimeStamp() {
        return new SimpleDateFormat("MM-dd-yyyy HH:mm").format(new Date());
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getCurrentLocalDateTimeStamp() {
        //ZoneId z = ZoneId.of("Asia/Singapore");
        return LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm"));
    }
}
