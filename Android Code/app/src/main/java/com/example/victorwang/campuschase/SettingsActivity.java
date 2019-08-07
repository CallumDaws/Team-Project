package com.example.victorwang.campuschase;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class SettingsActivity extends AppCompatActivity {
    private TreasureHuntAPI api = TreasureHuntAPI.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        Button button1 = (Button) findViewById(R.id.button4);
        button1.setOnClickListener(changeName);

    }

    private View.OnClickListener changeName = new View.OnClickListener() {
        public void onClick(View v) {
            EditText name = (EditText) findViewById(R.id.editText2);
            try {
                api.updatePlayerName(name.getText().toString());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

}
