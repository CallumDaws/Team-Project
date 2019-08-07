package com.example.victorwang.campuschase;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.util.Log;

import java.io.IOException;

public class CreateActivity extends AppCompatActivity {

    private TreasureHuntAPI api = TreasureHuntAPI.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.create);
        Button buttonmd = (Button) findViewById(R.id.btn_md);
        buttonmd.setOnClickListener(md);
        Button buttonjts = (Button) findViewById(R.id.btn_jts);
        buttonjts.setOnClickListener(jts);
        Button buttonCreateTeam = (Button) findViewById(R.id.btn_createteam);
        buttonCreateTeam.setOnClickListener(createTeam);
    }

    private View.OnClickListener jts = new View.OnClickListener() {
        public void onClick(View v) {
            try {
                int result = api.numberOfPlayersInTeam();
                new AlertDialog.Builder(CreateActivity.this)
                        .setTitle("Team Status")
                        .setMessage("Your team currently has " + result + " members.")
                        .setPositiveButton("Sure", null)
                        .show();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("IO", "IO" + e);
            }
        }
    };

    private View.OnClickListener md = new View.OnClickListener() {
        public void onClick(View v) {
            Intent mdIntent = new Intent(CreateActivity.this, MapsActivity.class);
            CreateActivity.this.startActivity(mdIntent);
            CreateActivity.this.finish();
        }
    };

    private View.OnClickListener createTeam = new View.OnClickListener() {
        public void onClick(View v) {
            Log.d("NOTIFICATION", "Create team pressed");
            try {
                EditText name = (EditText) findViewById(R.id.editText2);
                String result = api.createTeam(name.getText().toString());
                if (result.equals("error")) {
                    Log.e("CREATE", "Error creating team");
                } else {
                    Log.d("CREATE", "Team created successfully");
                    TextView teamidView = (TextView) findViewById(R.id.textView4);
                    teamidView.setText(result);
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("IO", "IO" + e);
            }
        }
    };

}


