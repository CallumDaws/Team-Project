package com.example.victorwang.campuschase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class HelpActivity extends AppCompatActivity {
    private TextView help;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);
        help = (TextView) findViewById(R.id.tx_helpd);
        help.setMovementMethod(new ScrollingMovementMethod());
        help.setText("Here's a step-by-step guide to Campus Chase:\n" +
                "Note: Please make sure your device is connected to the internet for proper functionality\n" +
                "\nStep One: Enter your name and choose a profile picture, these can only been seen by your team members, and press GO\n" +
                "\nStep Two: In your team decide who is going to create a team. Once decided that person press Create Team, enter your team name and press Create Team." +
                " You should see a code appear below, get all your team members to press Join Team on their devices and enter in this code so that they join your team" +
                " (NOTE: MAKE SURE ALL YOUR TEAMS MEMBERS JOINED BEFORE YOU CONTINUE AS THIS CODE CANNOT FOUND AGAIN)." +
                " You can see how many team members have joined before beginning by pressing My Team Status, once all team members have joined press Done.\n" +
                "\nStep Three: You and your team have now started the treasure hunt! You will see a map with your location, this will help you navigate and learn your way around campus." +
                " In the top left you will see a menu button; this is where you can find your team member's locations, this help page, about us, the map, your hint history and remaining hints" +
                " In the top there is your riddle to find the next location and in the top right is where you can get a clues to help solve your riddle, beware that using hints will reduce " +
                "the points you receive once you've solved the ridde. In the bottom left is the scan button which you will use to scan the QR code at the riddle location, if you're in" +
                " the wrong location you will scan the wrong QR code and won't be given the next riddle until you find the correct location, in the bottom right is quick access to the scoreboard.\n" +
                "\nStep Four: Once you complete the treasure hunt you can share your final score to social media and see how to stack against other teams.\n" +
                "\nHave fun and enjoy getting to know the campus and your team member.");
    }
}
