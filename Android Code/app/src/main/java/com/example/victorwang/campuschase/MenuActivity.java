package com.example.victorwang.campuschase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        Button buttonCt = (Button) findViewById(R.id.btn_ct);
        buttonCt.setOnClickListener(ct);
        Button buttonJt = (Button) findViewById(R.id.btn_jt);
        buttonJt.setOnClickListener(jt);
    }

    private View.OnClickListener ct = new View.OnClickListener() {
        public void onClick(View v) { // Create team
            Intent CtIntent = new Intent(MenuActivity.this, CreateActivity.class);
            MenuActivity.this.startActivity(CtIntent);
        }
    };

    private View.OnClickListener jt = new View.OnClickListener() {
        public void onClick(View v) { // Join team
            Intent JtIntent = new Intent(MenuActivity.this, JoinActivity.class);
            MenuActivity.this.startActivity(JtIntent);
        }
    };
}
