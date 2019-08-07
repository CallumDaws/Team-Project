package treasurehuntapi;

import java.io.IOException;
import static org.junit.Assert.*;
import org.junit.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.net.MalformedURLException;
import java.util.Map;

public class TreasureHuntAPITest {
	private TreasureHuntAPI api = new TreasureHuntAPI();

	
	@Test
	public void createTeam() {
		try {
			String result = api.createTeam("newteamuser10","teamname");
		
			if (result.equals("error")) {
				fail("An error occured. Maybe this user already exists or the server is down?");
			} else {
				System.out.println(result);
				assertTrue(true);
			}
		} catch (MalformedURLException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	@Ignore
	@Test
	public void joinTeam() {
		try {
			String result = api.joinTeam("tempusernew", "4540");
			if (result.equals("player name already taken")) {
				assertTrue(true);
				System.out.println("Player name has already been taken");
			} else {
				System.out.println(result);
				assertTrue(true);
			}
		} catch (MalformedURLException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	@Ignore
	@Test
	public void updateLocation() {
		try {
			api.setPlayerName("tempuser11");
			api.setTeamid("4540");
			api.setToken(
					"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0ZWFtX2lkIjoiNDU0MCIsImlhdCI6MTQ5MjE4MjE0MH0.AvX3oQnSxphaqCjwFsNKNxpfsiL8OVIhS2aJyghWok0");
			String result = api.updatePlayerLocation("location2");
			if (result.equals("error")) {
				fail("An error occured. is the server down?");
			} else {
				assertTrue(true);
			}
		} catch (MalformedURLException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	@Ignore
	@Test
	public void updateTeamScore() {
		api.setPlayerName("tempuser11");
		api.setTeamid("4540");
		api.setToken(
				"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0ZWFtX2lkIjoiNDU0MCIsImlhdCI6MTQ5MjE4MjE0MH0.AvX3oQnSxphaqCjwFsNKNxpfsiL8OVIhS2aJyghWok0");
		try {
			String result = api.updateTeamScore(60);
			if (result.equals("error")) {
				fail("An error occured. is the server down?");
			} else {
				assertTrue(true);
			}
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	@Ignore
	@Test
	public void getTeamScore() {
		api.setPlayerName("tempuser11");
		api.setTeamid("4540");
		api.setToken(
				"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0ZWFtX2lkIjoiNDU0MCIsImlhdCI6MTQ5MjE4MjE0MH0.AvX3oQnSxphaqCjwFsNKNxpfsiL8OVIhS2aJyghWok0");
		try {
			int result = api.getTeamScore();
			if (result == 60) {
				assertTrue(true);
			} else {
				fail("getTeamScore failed");
			}
		} catch (MalformedURLException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	@Ignore
	@Test
	public void removePlayer() {
		api.setPlayerName("tempuser12");
		api.setTeamid("4540");
		api.setToken(
				"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0ZWFtX2lkIjoiNDU0MCIsImlhdCI6MTQ5MjE4MjE0MH0.AvX3oQnSxphaqCjwFsNKNxpfsiL8OVIhS2aJyghWok0");
		try {
			String result = api.removePlayer(api.getPlayerName());
			if (result.equals("error")) {
				fail("there was an error removing the player");
			} else {
				assertTrue(true);
			}
		} catch (MalformedURLException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	@Ignore
	@Test
	public void removeTeam() {
		api.setPlayerName("joineduser1");
		api.setTeamid("5364");
		api.setToken(
				"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0ZWFtX2lkIjoiNTM2NCIsImlhdCI6MTQ5Mjg1NDg0M30.Qfu8_Co-By__nPBRm3dagSh6A2BbUoz5ReeGZaI03Kg");
		try {
			String result = api.removeTeam(api.getPlayerName());
			if (result.equals("error")) {
				fail("there was an error removing the team");
			} else {
				assertTrue(true);
			}
		} catch (MalformedURLException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	@Ignore
	@Test
	public void numberOfPlayersInTeam() {
		api.setPlayerName("tempuser1836466887");
		api.setTeamid("8347");
		api.setToken(
				"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0ZWFtX2lkIjoiODM0NyIsImlhdCI6MTQ5Mjg1NTE2OH0.GPhXYb6gvFyx4jkmZnlo9ixZnn3Mdp1e9RM6D8AkOFo");
		try {
			int result = api.numberOfPlayersInTeam();
			if (result > 0) {
				System.out.println(result);
				assertTrue(true);
			} else {
				fail("an error occured, is the server down?");
			}
		} catch (MalformedURLException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	@Ignore
	@Test
	public void getTeamDetails() {
		api.setPlayerName("bgerard");
		api.setTeamid("8724");
		api.setToken(
				"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0ZWFtX2lkIjoiODcyNCIsImlhdCI6MTQ5Mjg1NzkyOX0._uJIHK7Cmov4ExioMqUttga0YRarBV5yuuxCNUt6s9E");
		try {
			JsonObject result = api.getTeamDetails(api.getTeamid());
			System.out.println(result.toString());
			String teamid = result.get("teamid").toString();
			String score = result.get("score").toString();
			JsonArray players = result.get("players").getAsJsonArray();

			if (teamid.equals(api.getTeamid()) && score.equals("1") && players.size() > 0) {
				assertTrue(true);
			} else {
				fail("error");
			}

		} catch (MalformedURLException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}
	
	@Ignore
	@Test
	public void getTeamColour() {
		api.setPlayerName("newteamuser4");
		api.setTeamid("4718");
		api.setToken(
				"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0ZWFtX2lkIjoiNDcxOCIsImlhdCI6MTQ5Mjg2NzM4NH0.80X-xNimVtfkko8m4F1f5gBsgnFba7xi4_mMNbKZUw8");
		try {
			String result = api.getTeamColour();
			System.out.println(result.toString());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Ignore
	@Test
	public void getAllTeamScores() {
		try {
			Map<String, Integer> scores = api.getAllTeamScores();
			System.out.println(scores);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Ignore
	@Test 
	public void uploadProfilePicture() {
		api.setPlayerName("newteamuser4");
		api.setTeamid("4740");
		api.setToken(
				"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0ZWFtX2lkIjoiNDcxOCIsImlhdCI6MTQ5Mjg2NzM4NH0.80X-xNimVtfkko8m4F1f5gBsgnFba7xi4_mMNbKZUw8");
		try {
			String result = api.uploadProfilePicture("/home/ben/Team-Programming-Project/Backend/testimage.jpg", api.getPlayerName(), api.getTeamid());
			System.out.println(result);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
