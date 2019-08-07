package com.example.victorwang.campuschase;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

public class JoinActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.join);
        Button buttonJ = (Button) findViewById(R.id.btn_j);
        buttonJ.setOnClickListener(j);
    }

    private View.OnClickListener j = new View.OnClickListener() {
        public void onClick(View v) {


            TreasureHuntAPI api = TreasureHuntAPI.getInstance();
            try {
                EditText teamid = (EditText) findViewById(R.id.editText);
                String result = api.joinTeam(api.getPlayerName(), teamid.getText().toString());
                if (result.equals("player name already taken")) {
                    Log.e("CREATE", "Error joining team");
                } else {
                    Log.d("CREATE", "Team created successfully");
                    Intent JIntent = new Intent(JoinActivity.this, MapsActivity.class);
                    JoinActivity.this.startActivity(JIntent);
                    JoinActivity.this.finish();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("IO", "IO" + e);
            }

        }
    };
}
