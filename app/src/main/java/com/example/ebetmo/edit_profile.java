package com.example.ebetmo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class edit_profile extends AppCompatActivity {
    private final int REQUEST_CAMERA = 1, REQUEST_GALLERY = 0;
    private Bitmap itemBitmap;

    EditText name, contact, address, email, password, date;
    Button save;
    ImageView avatar;
    TextView verify;
    ImageButton close, camera;

    EditText []inputs = new EditText[6];

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        findId();

        name.setText(FormData.profile_info[0]);
        contact.setText(FormData.profile_info[1]);
        address.setText(FormData.profile_info[2]);
        email.setText(FormData.profile_info[3]);
        password.setText(FormData.profile_info[4]);
        date.setText(FormData.profile_info[7]);
        verify.setText(FormData.profile_info[6]+" Account.");

        File imgFile = new File(FormData.profile_info[5]);

        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            avatar.setImageBitmap(myBitmap);
        }

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), account.class));
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    String url1 = "";
                    for(int i=0; i<inputs.length;i++){
                        url1 = url1 + FormData.profile_labels1[i] + "=" + inputs[i].getText().toString() + "&";
                    }

                    String URL = "http://"+Final_IP.IP_ADDRESS+"/betting/update_user.php?" + url1 + "pf=" + FormData.imageLocation + "&id=" + FormData.id;

                    WebView view1 = findViewById(R.id.webView);

                    view1.loadUrl(URL);
                    view1.getSettings().setJavaScriptEnabled(true);

                    Toast.makeText(edit_profile.this, "Updated Successfully.", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(getApplicationContext(), account.class));
                    finish();
                }
                catch (Exception e){
                    Toast.makeText(edit_profile.this, "Updated Successfully.", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
            }
        });
    }

    private void findId() {
        name = inputs[0] = findViewById(R.id.fName);
        contact = inputs[1] = findViewById(R.id.user_contact);
        address = inputs[2] =findViewById(R.id.user_address);
        email = inputs[3] =findViewById(R.id.user_email);
        password = inputs[4] =findViewById(R.id.user_password);
        date = inputs[5] =findViewById(R.id.user_bDay);
        save = findViewById(R.id.save_btn);
        avatar = findViewById(R.id.profile);
        verify = findViewById(R.id.verify);
        close = findViewById(R.id.close);
        camera = findViewById(R.id.upload);
    }


    private void getPic()
    {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType(getString(R.string.galleryType));
        startActivityForResult(galleryIntent.createChooser(galleryIntent, "Select Images"), REQUEST_GALLERY);
    }

    private void takePic()
    {
        Intent takePicIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicIntent, REQUEST_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
        {
            if(requestCode == REQUEST_CAMERA)
            {

                itemBitmap = (Bitmap) data.getExtras().get("data");
                avatar.setImageBitmap(itemBitmap);

                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                Uri tempUri = getImageUri(getApplicationContext(), itemBitmap);

                // CALL THIS METHOD TO GET THE ACTUAL PATH
                File finalFile = new File(getRealPathFromURI(tempUri));

                FormData.imageLocation = finalFile.getAbsolutePath();


            }
            else if(requestCode == REQUEST_GALLERY && data != null)
            {
                try {
                    itemBitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(),
                            data.getData());
                    avatar.setImageBitmap(itemBitmap);

                    // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                    Uri tempUri = getImageUri(getApplicationContext(), itemBitmap);

                    // CALL THIS METHOD TO GET THE ACTUAL PATH
                    File finalFile = new File(getRealPathFromURI(tempUri));

                    FormData.imageLocation = finalFile.getAbsolutePath();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void insertPic(View view){
        final CharSequence[] items = {"Camera", "Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Upload Item Image");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(items[which].equals("Camera"))
                    takePic();
                else if(items[which].equals("Gallery"))
                    getPic();
                else if(items[which].equals("Cancel"))
                    dialog.dismiss();
            }
        });
        builder.show();
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }
}


