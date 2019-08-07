package com.example.victorwang.campuschase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {
    private TextView about;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        about = (TextView) findViewById(R.id.tx_aboutd);
        about.setMovementMethod(new ScrollingMovementMethod());
        about.setText("Welcome to Campus Chase, a treasure hunt app designed to help familiarise you and your team with Newcastle university campus." +
                " Solve the riddles, find the correct QR codes and beat the rest of the teams.\n" +
                "Campus Chase was designed and created by Team 16, second year computer science students.\n" +
                "Creators:\nCallum Daws - Team Leader\nBen Gerard - Head of programming\n" +
                "Emma Wilson - Head of design\nJacky Li - Head of research\n" +
                "Daniel Carroll - Programmer\nVictor Li - Programmer\n" +
                "Marawan Alwaraki - Designer\nGavin Loughrey - Researcher");
    }
}
