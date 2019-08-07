package treasurehuntapi;

import java.util.Comparator;

public class Team implements Comparable<Team>{
	private String teamid;
	private String teamColour;
	private String teamLeader;
	private int score;
	
	public Team (String teamid, String teamColour, String teamLeader, int score) {
		setTeamid(teamid);
		setTeamColour(teamColour);
		setTeamLeader(teamLeader);
		setScore(score);
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
	
	public static Comparator<Team> compare()
	{   
	 Comparator<Team> comp = new Comparator<Team>(){
	     @Override
	     public int compare(Team t1, Team t2)
	     {
	         return t1.compareTo(t2);
	     }        
	 };
	 return comp;
	}  
	
	

}
