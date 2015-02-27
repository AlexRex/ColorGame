package me.alextorres.quizGame;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


public class gameOver extends Activity {


    ArrayList<HashMap<String, String>> questionList;

    long points, maxPoints;

    public static final String PREFS_NAME = "Prefs";
    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game_over);

        Intent i = getIntent();

        Button playAgain = (Button) findViewById(R.id.btnPlayAgain);
        Button goStart = (Button) findViewById(R.id.btnStart);

        TextView pointText = (TextView) findViewById(R.id.pointsFinish);
        TextView maxPointsText = (TextView) findViewById(R.id.maxPointsFinish);

        settings = getSharedPreferences(PREFS_NAME, 0);
        maxPoints = settings.getLong("maxPoints", 0);

        final Intent play = new Intent(getApplicationContext(), inGame.class);
        questionList = (ArrayList<HashMap<String, String>>) i.getSerializableExtra("questions");
        points = i.getLongExtra("points", 0);

        if(points > maxPoints){
            Log.v("dentro", "dentroo");
            SharedPreferences.Editor editor = settings.edit();
            editor.putLong("maxPoints", points);

            // Commit the edits!
            editor.commit();
        }

        Log.v("points", points+"");
        Log.v("maxPoints", maxPoints+"");
        pointText.setText(points+"");
        maxPointsText.setText(maxPoints+"");


        play.putExtra("questions", questionList);



        playAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(play);
                finish();
            }
        });

        goStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }

}