package com.example.victorwang.campuschase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class ScoresActivity extends AppCompatActivity {
    private ScoreManager scoreManager = ScoreManager.getInstance();
    private TreasureHuntAPI api = TreasureHuntAPI.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.scores);

        List<Team> teams = scoreManager.getTeamsAndScores();
        TextView scoresView = (TextView) findViewById(R.id.tx_sbd);

        for (int i = 0; i < teams.size(); i++) {
            Team tempTeam = teams.get(i);
            scoresView.setText(scoresView.getText().toString() + "\n" + "Team Name: " + tempTeam.getTeamName() + "Score: " + tempTeam.getScore());
        }

    }
}