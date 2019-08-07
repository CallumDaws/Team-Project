package com.example.victorwang.campuschase;

import java.util.*;
import java.io.IOException;
import java.net.MalformedURLException;

import com.google.gson.JsonObject;


public class ScoreManager {
    private static ScoreManager instance = null;
    private List<Team> teamArray = new ArrayList<Team>();
    private TreasureHuntAPI api = new TreasureHuntAPI();

    protected ScoreManager() {
        updateScores();
    }

    public static ScoreManager getInstance() {
        if (instance == null) {
            instance = new ScoreManager();
        }
        return instance;
    }

    public void updateScores() {
        //System.out.println("updating scores");
        try {
            Map<String, Integer> scores = api.getAllTeamScores();
            for (String key : scores.keySet()) {
                JsonObject teamDetails = api.getTeamDetails(key);
                Team tempTeam = new Team(teamDetails.get("teamname").getAsString(), teamDetails.get("teamid").getAsString(), teamDetails.get("colour").getAsString(), teamDetails.get("teamleader").getAsString(), teamDetails.get("score").getAsInt());
                teamArray.add(tempTeam);
            }
            Collections.sort(teamArray, Team.compare());

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public List<Team> getTeamsAndScores() {
        return teamArray;
    }

    public int getHighscore() {
        return teamArray.get(0).getScore();
    }

}
