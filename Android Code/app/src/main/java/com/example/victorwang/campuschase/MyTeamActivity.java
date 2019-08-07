package com.example.victorwang.campuschase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyTeamActivity extends AppCompatActivity {
    private TreasureHuntAPI api = TreasureHuntAPI.getInstance();
    JsonObject teamDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myteam);
        try {
            teamDetails = api.getTeamDetails(api.getTeamid());
            JsonArray players = teamDetails.get("players").getAsJsonArray();
            TextView teamMembers = (TextView) findViewById(R.id.tx_teamm);

            for (int i = 0; i < players.size(); i++) {
                JsonObject tempPlayer = players.get(i).getAsJsonObject();
                teamMembers.setText(teamMembers.getText().toString() + tempPlayer.get("playername").getAsString() + "\n");
            }


        } catch (IOException e) {
            e.printStackTrace();
            Log.e("IO", "IO" + e);
        }

    }
}
