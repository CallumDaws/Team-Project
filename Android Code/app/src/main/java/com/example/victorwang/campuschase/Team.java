package com.example.victorwang.campuschase;

import java.util.Comparator;

public class Team implements Comparable<Team> {
    private String teamName;
    private String teamid;
    private String teamColour;
    private String teamLeader;
    private int score;

    public Team(String teamName, String teamid, String teamColour, String teamLeader, int score) {
        setTeamName(teamName);
        setTeamid(teamid);
        setTeamColour(teamColour);
        setTeamLeader(teamLeader);
        setScore(score);
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamid() {
        return teamid;
    }

    public void setTeamid(String teamid) {
        this.teamid = teamid;
    }

    public String getTeamColour() {
        return teamColour;
    }

    public void setTeamColour(String teamColour) {
        this.teamColour = teamColour;
    }

    public String getTeamLeader() {
        return teamLeader;
    }

    public void setTeamLeader(String teamLeader) {
        this.teamLeader = teamLeader;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int compareTo(Team other) {
        if ((getScore() == other.getScore())) {
            return 0;
        } else if (other.getScore() < getScore()) {
            return -1;
        } else {
            return 1;
        }
    }

    public static Comparator<Team> compare() {
        Comparator<Team> comp = new Comparator<Team>() {
            @Override
            public int compare(Team t1, Team t2) {
                return t1.compareTo(t2);
            }
        };
        return comp;
    }


}
