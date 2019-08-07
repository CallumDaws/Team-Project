package com.example.victorwang.campuschase;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.test.mock.MockPackageManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;


import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final int REQUEST_SCAN = 1;
    private TreasureHuntAPI api = TreasureHuntAPI.getInstance();
    private QuestionManager questionManager;
    private ScoreManager scoreManager = ScoreManager.getInstance();
    private int questionNumber = 0;
    private Question currentQuestion;
    private JsonObject teamDetails;
    private List<String> clues;
    private int clueNumber = -1;
    private int questionScore = 3;
    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;
    private Map<String, Marker> markers = new HashMap<String, Marker>();
    private Timer updateTimer;
    private TextView questionView;
    private Marker playerMarker;
    private LatLng playerLocation;
    private MarkerOptions markerpos;

    // GPSTracker class
    GPSTracker gps;
    public double latitude;
    public double longitude;
    public double latitudeS;
    public double longitudeS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        questionManager = QuestionManager.getInstance();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Button buttonmc = (Button) findViewById(R.id.btn_mc);
        buttonmc.setOnClickListener(mc);
        Button buttonms = (Button) findViewById(R.id.btn_ms);
        buttonms.setOnClickListener(ms);
        Button buttonclue = (Button) findViewById(R.id.btn_clue);
        buttonclue.setOnClickListener(mclue);
        Button buttonmm = (Button) findViewById(R.id.btn_Menu);
        buttonmm.setOnClickListener(mmac);
        questionView = (TextView) findViewById(R.id.questionView);

        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission)
                    != MockPackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{mPermission},
                        REQUEST_CODE_PERMISSION);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        gps = new GPSTracker(MapsActivity.this);

        setQuestion();

        updateTimer = new Timer();
        updateTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                updateLoop();
            }
        }, 0, 10000);


    }


    public void updateLoop() {
        // This is called every 10 seconds to update the team details
        // Log.d("DEBUG", "updateLOOP");
        scoreManager.updateScores();

       // updatePlayerLocations();


        try {
            teamDetails = api.getTeamDetails(api.getTeamid());
            int teamQuestionNumber = teamDetails.get("question").getAsInt();
            if (teamQuestionNumber != questionNumber) {
                questionNumber = teamQuestionNumber;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new AlertDialog.Builder(MapsActivity.this)
                                .setTitle("Location Found!")
                                .setMessage("Your team has found the location you are looking for. Your clues have been updated.")
                                .setPositiveButton("Dismiss", null)
                                .show();
                        setQuestion();
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("IO", "IO" + e);
        }

    }


    public void setQuestion() {
        currentQuestion = questionManager.getQuestion(questionNumber);
        questionView.setText(currentQuestion.getQuestion().toString());
        // Set clues
        clues = currentQuestion.getClues();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        updatePlayerLocations();
    }

    private void updatePlayerLocations() {
        System.out.println("UpdatePlayerLocations");
        // Set player location
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            latitudeS = latitude;
            longitudeS = longitude;
            try {
                String stringLocation = latitude + "," + longitude;
                api.updatePlayerLocation(stringLocation);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("IO", "IO" + e);
            }

            playerLocation = new LatLng(latitude, longitude);
            // runOnUiThread(new Runnable() {
            //   @Override
            // public void run() {
            markerpos = new MarkerOptions();
            if (!markers.containsKey(api.getPlayerName())) {
                System.out.println(api.getPlayerName());
                markerpos.position(playerLocation);
                markerpos.title(api.getPlayerName());
                playerMarker = mMap.addMarker(markerpos);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(playerLocation));
                markers.put(api.getPlayerName(), playerMarker);
            } else {
                // playerMarker = markers.get(api.getPlayerName());
                System.out.println("location update");
                playerMarker.setPosition(playerLocation);




            }
            // }
            //});


            if (teamDetails == null) {
                try {
                    teamDetails = api.getTeamDetails(api.getTeamid());
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("IO", "IO" + e);
                }

                // Add team locations
                JsonArray players = teamDetails.get("players").getAsJsonArray();
                for (int i = 0; i < players.size(); i++) {
                    JsonObject tempPlayer = players.get(i).getAsJsonObject();
                    if (!tempPlayer.get("playername").getAsString().equals(api.getPlayerName())) {
                        System.out.println(tempPlayer.get("playername").getAsString());

                        if (tempPlayer.get("location") != null) {
                            String playerStringLocation = tempPlayer.get("location").getAsString();
                            System.out.println(playerStringLocation);

                            String[] locationParts = playerStringLocation.split(",");

                            double lat = Double.parseDouble(locationParts[0]);
                            double lng = Double.parseDouble(locationParts[1]);
                            LatLng tempLocation = new LatLng(lat, lng);
                            String title = tempPlayer.get("playername").getAsString();

                            if (!markers.containsKey(tempPlayer.get("playername").getAsString())) {
                                Marker tempMarker = mMap.addMarker(new MarkerOptions().position(tempLocation).title(title));
                                markers.put(tempPlayer.get("playername").getAsString(), tempMarker);
                            } else {
                                Marker tempMarker = markers.get(tempPlayer.get("playername").getAsString());
                                tempMarker.setPosition(tempLocation);
                            }
                        }


                    }


                }
            }


        } else {
            gps.showSettingsAlert();
        }
    }





    private View.OnClickListener mclue = new View.OnClickListener() {
        public void onClick(View v) {
            if (clueNumber > 1) {
                new AlertDialog.Builder(MapsActivity.this)
                        .setTitle("You have no more clues left")
                        .setMessage("You have run out of clues.")
                        .setPositiveButton("Dismiss", null)
                        .show();
            } else {
                clueNumber++;
                questionScore--;
                int displayNumber = clueNumber + 1;
                int cluesRemaining = currentQuestion.getNumberOfClues() - displayNumber;
                currentQuestion.addUsedClue(clues.get(clueNumber));
                currentQuestion.setCurrentClue(clues.get(clueNumber));
                new AlertDialog.Builder(MapsActivity.this)
                        .setTitle("Clue " + displayNumber)
                        .setMessage(clues.get(clueNumber) + "\n" + "You have " + cluesRemaining + " clues remaining.")
                        .setPositiveButton("Sure", null)
                        .show();
            }
        }
    };

    private View.OnClickListener mc = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent(MapsActivity.this, QRScan.class);
            startActivityForResult(intent, REQUEST_SCAN);
        }
    };
    private View.OnClickListener ms = new View.OnClickListener() {
        public void onClick(View v) {
            Intent msIntent = new Intent(MapsActivity.this, ScoresActivity.class);
            MapsActivity.this.startActivity(msIntent);
        }
    };
    private View.OnClickListener mmac = new View.OnClickListener() {
        public void onClick(View v) {
            Intent mmacIntent = new Intent(MapsActivity.this, MainMenuActivity.class);
            MapsActivity.this.startActivity(mmacIntent);
        }
    };

    //decodes QR Code
    @Override
    protected void onActivityResult(int request, int result, Intent data) {
        if (data != null) {
            //data retrieved from the qr code
            final Barcode qr = data.getParcelableExtra("QR code");
            //add comparing qr data to backend answer here
            if (qr.displayValue.equals(currentQuestion.getAnswer())) {
                // Scanned the correct QR code
                new AlertDialog.Builder(MapsActivity.this)
                        .setTitle("Location Found!")
                        .setMessage("Congratulations, you have found the correct location.")
                        .setPositiveButton("Dismiss", null)
                        .show();
                // Update team question number and score
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            int newQuestionNumber = questionNumber + 1;
                            api.updateTeamQuestionNumber(newQuestionNumber);
                            int score = api.getTeamScore() + questionScore;
                            api.updateTeamScore(score);
                            currentQuestion = questionManager.getNextQuestion();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.e("IO", "IO" + e);
                        }

                    }
                }).start();



            } else {
                new AlertDialog.Builder(MapsActivity.this)
                        .setTitle("Error")
                        .setMessage("You have not found the correct location")
                        .setPositiveButton("Dismiss", null)
                        .show();
            }
        } else {
            Toast.makeText(MapsActivity.this, "Scanning cancelled", Toast.LENGTH_SHORT).show();
        }
    }
}
