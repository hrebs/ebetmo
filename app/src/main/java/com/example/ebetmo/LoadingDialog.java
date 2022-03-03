package com.example.ebetmo;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.TextView;

public class LoadingDialog {
    Context context;
    Dialog dialog;

    public LoadingDialog(Context context){
this.context = context;
    }

    public void showLoading(String Title){
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.loading);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        TextView title = dialog.findViewById(R.id.textView);

        title.setText(Title);
        dialog.create();
        dialog.show();

    }

    public void hideLoading(){
        dialog.dismiss();
    }

}
