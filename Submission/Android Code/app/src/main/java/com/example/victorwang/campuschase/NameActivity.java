package com.example.victorwang.campuschase;


import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import static android.R.attr.data;
import android.widget.ImageView;



public class NameActivity extends AppCompatActivity {
    private String picPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.name);
        Button button1 = (Button) findViewById(R.id.btn1);
        button1.setOnClickListener(help);
        Button button2 = (Button) findViewById(R.id.btn2);
        button2.setOnClickListener(go);
        Button button3 = (Button) findViewById(R.id.button2);
        button3.setOnClickListener(picture);

    }

    private View.OnClickListener help = new View.OnClickListener() {
        public void onClick(View v) {
            new AlertDialog.Builder(NameActivity.this)
                    .setTitle("Help")
                    .setMessage("Start here by add your name")
                    .setPositiveButton("Sure", null)
                    .show();

        }
    };

    private View.OnClickListener go = new View.OnClickListener() {
        public void onClick(View v) {
            Log.d("NOTIFCATION", "Go button pressed");
            TreasureHuntAPI api = TreasureHuntAPI.getInstance();
            EditText name = (EditText) findViewById(R.id.et1);
            Log.d("NOTIFICATION", name.getText().toString());

            if (name.getText().toString().equals("")) {
                new AlertDialog.Builder(NameActivity.this)
                        .setTitle("Enter a name")
                        .setMessage("Please enter your player name.")
                        .setPositiveButton("Ok", null)
                        .show();

            } else {
                api.setPlayerName(name.getText().toString());
                api.setProfilePicLocal(picPath);
                Log.d("NOTIFICATION", api.getPlayerName());
                Intent nameIntent = new Intent(NameActivity.this, MenuActivity.class);
                NameActivity.this.startActivity(nameIntent);
                NameActivity.this.finish();
            }
        }
    };

    private View.OnClickListener picture = new View.OnClickListener() {
        public void onClick(View v) {
            Intent picIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            picIntent.setType("image/*");
            startActivityForResult(picIntent, 1);

        }
    };


@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
        return;
        }
        if (requestCode == 1 && data != null) {
        Uri picUri = data.getData();
        //image file path
        picPath = picUri.getPath();
        ImageView pic = (ImageView) findViewById(R.id.imageView2);
        pic.setImageURI(picUri);
        }

        }
        }
