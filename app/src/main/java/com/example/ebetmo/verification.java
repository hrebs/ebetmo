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
import android.util.Log;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class verification extends AppCompatActivity {
    EditText name, age, house, street, brgy, city, province, zip;
    Button driver, sss, passport, voters, postal, HDMF, GOCC, PRC, barangay, national, senior, submit;
    ImageButton back;
    //String ID_Type ,owner_id;
    CardView id_panel;
    Dialog dialog;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        findId();
        dialog = new Dialog(this);

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
        dialog.setCancelable(true);
        Button ok = dialog.findViewById(R.id.done_btn);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Toast.makeText(verification.this, FormData.ID_Type, Toast.LENGTH_SHORT).show();
                submit.setEnabled(true);
            }
        });

        dialog.show();
        id_panel.setVisibility(View.GONE);
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

    public void submit(View view){
        String name1, age1, house1, street1, brgy1, city1, province1, zip1;
        name1 = FormData.verify_info[0] = name.getText().toString().trim();
        age1 = FormData.verify_info[1] = age.getText().toString().trim();
        house1 = FormData.verify_info[2] = house.getText().toString().trim();
        street1 = FormData.verify_info[3] = street.getText().toString().trim();
        brgy1 = FormData.verify_info[4] = brgy.getText().toString().trim();
        city1 = FormData.verify_info[5] = city.getText().toString().trim();
        province1 = FormData.verify_info[6] = province.getText().toString().trim();
        zip1 = FormData.verify_info[7] = zip.getText().toString().trim();
        FormData.verify_info[8] = FormData.ID_Type;
        FormData.verify_info[9] = FormData.imageLocation;

        queue = Volley.newRequestQueue(this);
        if(name1.equals("") || age1.equals("") || house1.equals("") || street1.equals("") || brgy.equals("")){
            Toast.makeText(verification.this, "Please fill up the form.", Toast.LENGTH_SHORT).show();
        }

        else {
            final String URL = "http://" + Final_IP.IP_ADDRESS + "/betting/add_verify.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, response -> {
                Log.d("res", response);
                if (response.equals("success")) {
                    Toast.makeText(verification.this, "You are now verified!", Toast.LENGTH_SHORT).show();

                    String URL1 = "http://"+Final_IP.IP_ADDRESS+"/betting/upd_verify.php?em="+FormData.email+"&vr=Verified";

                    WebView view1 = findViewById(R.id.webView2);
                    FormData.profile_info[6] = "Verified";

                    view1.loadUrl(URL1);
                    view1.getSettings().setJavaScriptEnabled(true);

                    //FormData.clearItem();
                    Intent intent = new Intent(verification.this, home.class);
                    startActivity(intent);
                    finish();
                } else if (response.equals("failure")) {
                    Toast.makeText(verification.this, "Something wrong, try again.", Toast.LENGTH_SHORT).show();
                }
            }, error -> Toast.makeText(verification.this, error.toString().trim(), Toast.LENGTH_SHORT).show()) {
                @Override
                public Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> data = new HashMap<>();

                    for(int i=0;i<FormData.verify_info.length;i++){
                        data.put(FormData.verify_labels[i], FormData.verify_info[i]);
                    }

                    return data;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }
}


