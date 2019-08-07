/*
Author: Ben Gerard
Date: 17/3/17
Purpose: Provides a java interface for the server-side.
*/
package com.example.victorwang.campuschase;

import java.io.*;
import java.net.*;
import java.util.*;

import com.google.gson.*;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TreasureHuntAPI {
    private String token;
    private String playerName;
    private String teamid;
    private String serverIP;
    private String questionsURL;
    private String profilePicturesURL;
    private String profilePicLocal;

    // Create singleton
    private static TreasureHuntAPI instance = null;

    protected TreasureHuntAPI() {
        // Get serverip and questions from config file
        HttpURLConnection request;
        try {
            request = (HttpURLConnection) new URL("http://bengerard.me/campuschase/config.json").openConnection();
            request.connect();
            JsonParser jp = new JsonParser();
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
            JsonObject rootobj = root.getAsJsonObject();
            serverIP = rootobj.getAsJsonObject().get("serverip").getAsString();
            questionsURL = rootobj.getAsJsonObject().get("questions").getAsString();
            profilePicturesURL = rootobj.getAsJsonObject().get("profilepictures").getAsString();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static TreasureHuntAPI getInstance() {
        if (instance == null) {
            instance = new TreasureHuntAPI();
        }
        return instance;
    }

    public String uploadProfilePicture(String filename, String playername, String teamid) throws MalformedURLException, IOException {
        MediaType MEDIA_TYPE_PNG = MediaType.parse("image/jpg");
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("playername", playername)
                .addFormDataPart("teamid", teamid)
                .addFormDataPart("profilepic", filename, RequestBody.create(MEDIA_TYPE_PNG, new File(filename)))
                .build();

        Request request = new Request.Builder()
                .url(serverIP + "/treasurehunt/api/uploadpicture")
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        return response.body().string();
    }

    public JsonArray downloadQuestions() throws JsonIOException, JsonSyntaxException, IOException {
        HttpURLConnection request = (HttpURLConnection) new URL(
                questionsURL).openConnection();
        request.connect();

        // Parse json
        JsonParser jp = new JsonParser();
        JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
        JsonObject rootobj = root.getAsJsonObject();
        return rootobj.getAsJsonObject().get("questions").getAsJsonArray(); // Return questions as a json array
    }

    public String createTeam(String teamName) throws MalformedURLException, IOException {
        String json = "{ \"teamname\":\"" + teamName + "\"}";
        System.out.println(json);
        HttpURLConnection conn = sendJsonRequest(serverIP + "/treasurehunt/api/createteam", json); // Send request
        InputStream in = new BufferedInputStream(conn.getInputStream());
        String result = org.apache.commons.io.IOUtils.toString(in, "UTF-8"); // Get response

        System.out.println("Team ID from the server: " + result);

        in.close();
        conn.disconnect();
        setTeamid(result);
        String token = assignTeamLeader(getPlayerName(), result);
        // Assign team leader
        if (!token.equals("error")) {
            System.out.println("Token from server: " + token);
            setToken(token); // Store authentication token
            return result;
        } else {
            return "error";
        }

    }

    private String assignTeamLeader(String playerName, String teamid) throws MalformedURLException, IOException {
        String json = "{ \"playername\":\"" + playerName + "\", \"teamid\":\"" + teamid + "\"}"; // JSON request body
        HttpURLConnection conn = sendJsonRequest(serverIP + "/treasurehunt/api/assignteamleader", json);
        InputStream in = new BufferedInputStream(conn.getInputStream());
        String result = org.apache.commons.io.IOUtils.toString(in, "UTF-8");

        in.close();
        conn.disconnect();
        return result;
    }

    public String joinTeam(String playerName, String teamid) throws MalformedURLException, IOException {
        String json = "{ \"playername\":\"" + playerName + "\", \"teamid\":\"" + teamid + "\"}";
        HttpURLConnection conn = sendJsonRequest(serverIP + "/treasurehunt/api/jointeam", json);
        InputStream in = new BufferedInputStream(conn.getInputStream());
        String result = org.apache.commons.io.IOUtils.toString(in, "UTF-8");

        in.close();
        conn.disconnect();
        if (!result.equals("player name already taken")) {
            setTeamid(teamid);
            setPlayerName(playerName);
            setToken(result);
            return result;
        } else {
            return "player name already taken";
        }
    }

    public String updatePlayerLocation(String location) throws MalformedURLException, IOException {
        String json = "{ \"playername\":\"" + getPlayerName() + "\", \"location\":\"" + location + "\", \"token\":\""
                + getToken() + "\"}";
        HttpURLConnection conn = sendJsonRequest(serverIP + "/treasurehunt/api/updateplayerlocation", json);
        InputStream in = new BufferedInputStream(conn.getInputStream());
        String result = org.apache.commons.io.IOUtils.toString(in, "UTF-8");

        in.close();
        conn.disconnect();

        if (result.equals("error") || result.equals("Forbidden")) {
            return "error";
        } else {
            return "location updated";
        }

    }

    public String updateTeamScore(int score) throws MalformedURLException, IOException {

        String json = "{ \"playername\":\"" + getPlayerName() + "\", \"teamid\":\"" + getTeamid() + "\", \"score\":\"" + score + "\",\"token\":\""
                + getToken() + "\"}";
        HttpURLConnection conn = sendJsonRequest(serverIP + "/treasurehunt/api/updateteamscore", json);
        InputStream in = new BufferedInputStream(conn.getInputStream());
        String result = org.apache.commons.io.IOUtils.toString(in, "UTF-8");

        in.close();
        conn.disconnect();

        if (result.equals("error") || result.equals("Forbidden")) {
            return "error";
        } else {
            return "score updated";
        }


    }

    public void updateTeamQuestionNumber(int questionNumber) throws IOException {
        String json = "{ \"teamid\":\"" + getTeamid() + "\", \"questionNumber\":\"" + questionNumber + "\"}";
        HttpURLConnection conn = sendJsonRequest(serverIP + "/treasurehunt/api/updateteamquestion", json);
        InputStream in = new BufferedInputStream(conn.getInputStream());

        in.close();
        conn.disconnect();

    }

    public void updatePlayerName(String name) throws IOException {
        String json = "{ \"playername\":\"" + name + "\", \"teamid\":\"" + getTeamid() + "\"}";
        HttpURLConnection conn = sendJsonRequest(serverIP + "/treasurehunt/api/updateplayername", json);
        InputStream in = new BufferedInputStream(conn.getInputStream());

        in.close();
        conn.disconnect();

    }

    public int getTeamScore() throws MalformedURLException, IOException {
        Map<String, Integer> scores = getAllTeamScores();
        System.out.println(scores);
        return scores.get(getTeamid());
    }

    public String removePlayer(String playerName) throws MalformedURLException, IOException {
        String json = "{ \"playername\":\"" + playerName + "\", \"token\":\"" + getToken() + "\"}";
        HttpURLConnection conn = sendRemoveRequest(serverIP + "/treasurehunt/api/removeplayer", json);
        InputStream in = new BufferedInputStream(conn.getInputStream());
        String result = org.apache.commons.io.IOUtils.toString(in, "UTF-8");

        if (result.equals("error") || result.equals("Forbidden")) {
            return "error";
        } else {
            return result;
        }
    }

    public String removeTeam(String playerName) throws MalformedURLException, IOException {
        String json = "{ \"playername\":\"" + getPlayerName() + "\", \"teamid\":\"" + getTeamid() + "\", \"token\":\""
                + getToken() + "\"}";
        HttpURLConnection conn = sendRemoveRequest(serverIP + "/treasurehunt/api/removeteam", json);
        InputStream in = new BufferedInputStream(conn.getInputStream());
        String result = org.apache.commons.io.IOUtils.toString(in, "UTF-8");

        if (result.equals("error") || result.equals("Forbidden")) {
            return "error";
        } else {
            return result;
        }
    }

    public int numberOfPlayersInTeam() throws MalformedURLException, IOException {
        System.out.println(getTeamid());
        String json = "{\"teamid\":\"" + getTeamid() + "\"}";
        HttpURLConnection conn = sendJsonRequest(serverIP + "/treasurehunt/api/playersinteam", json);
        InputStream in = new BufferedInputStream(conn.getInputStream());
        String result = org.apache.commons.io.IOUtils.toString(in, "UTF-8");
        System.out.println(result);

        if (result.equals("error") || result.equals("Forbidden")) {
            throw new IllegalArgumentException("Error");
        }
        return Integer.parseInt(result);
    }


    public JsonObject getTeamDetails(String teamid) throws MalformedURLException, IOException {
        String json = "{\"teamid\":\"" + teamid + "\"}";
        HttpURLConnection conn = sendJsonRequest(serverIP + "/treasurehunt/api/getteamdetails", json);
        InputStream in = new BufferedInputStream(conn.getInputStream());
        String result = org.apache.commons.io.IOUtils.toString(in, "UTF-8");


        JsonObject resultJson = new JsonParser().parse(result).getAsJsonObject();

        return resultJson;
    }

    public Map<String, Integer> getAllTeamScores() throws MalformedURLException, IOException {
        String json = "{ }";
        HttpURLConnection conn = sendJsonRequest(serverIP + "/treasurehunt/api/getallteamscores", json);
        InputStream in = new BufferedInputStream(conn.getInputStream());
        String result = org.apache.commons.io.IOUtils.toString(in, "UTF-8");

        if (result.equals("error")) {
            throw new IllegalArgumentException("Error");
        }

        JsonArray resultJson = new JsonParser().parse(result).getAsJsonArray();
        Map<String, Integer> scores = new HashMap<String, Integer>();

        for (int i = 0; i < resultJson.size(); i++) {
            JsonObject tempObject = (JsonObject) resultJson.get(i);
            if (!tempObject.get("score").toString().equals("null")) {
                scores.put(tempObject.get("teamid").toString(), Integer.parseInt(tempObject.get("score").toString()));
            }
        }

        return scores;
    }

    public boolean isTeamIDValid(String teamid) throws IOException {
        String json = "{\"teamid\":\"" + teamid + "\"}";
        HttpURLConnection conn = sendJsonRequest(serverIP + "/treasurehunt/api/checkteamid", json);
        InputStream in = new BufferedInputStream(conn.getInputStream());
        String result = org.apache.commons.io.IOUtils.toString(in, "UTF-8");
        JsonObject resultJson = new JsonParser().parse(result).getAsJsonObject();
        System.out.println(resultJson.get("COUNT(*)").getAsInt());
        if (resultJson.get("COUNT(*)").getAsInt() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public String getTeamColour() throws MalformedURLException, IOException {
        JsonObject teamDetails = getTeamDetails(getTeamid());
        System.out.println(teamDetails.toString());
        String hexColour = teamDetails.get("colour").toString();
        return hexColour;
    }

    private HttpURLConnection sendJsonRequest(String url, String json) throws MalformedURLException, IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestMethod("POST");

        OutputStream os = conn.getOutputStream();
        os.write(json.getBytes("UTF-8"));
        os.close();
        return conn;
    }

    private HttpURLConnection sendRemoveRequest(String url, String json) throws MalformedURLException, IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestMethod("DELETE");

        OutputStream os = conn.getOutputStream();
        os.write(json.getBytes("UTF-8"));
        os.close();
        return conn;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }


    public String getTeamid() {
        return teamid;
    }

    public void setTeamid(String teamid) {
        this.teamid = teamid;
    }

    public void setProfilePicLocal(String url) {
        profilePicLocal = url;
    }

    public String getProfilePicLocal() {
        return profilePicLocal;
    }



}
