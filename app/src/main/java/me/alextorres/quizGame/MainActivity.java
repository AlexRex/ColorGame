package me.alextorres.quizGame;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends Activity {

    private ProgressDialog pDialog;
    private static String url = "http://api.alextorres.me/v1/questions";

    // JSON NAMES

    private static final String TAG_QUESTION = "question";
    private static final String TAG_CORRECT = "trueAnswer";
    private static final String TAG_SECOND = "secondAnswer";
    private static final String TAG_THIRD = "thirdAnswer";
    private static final String TAG_TYPE = "type";




    JSONArray questions = null;

    ArrayList<HashMap<String, String>> questionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);


        //Animaciones
        AlphaAnimation animation1 = new AlphaAnimation(0.0f, 1.0f);
        animation1.setDuration(1500);
        animation1.setFillAfter(true);


        //Elementos
        Button btnPlay = (Button) findViewById(R.id.btnPlay);
        Button sendNew = (Button) findViewById(R.id.sendNew);
        ImageView image = (ImageView) findViewById(R.id.imageView);
        TextView text = (TextView) findViewById(R.id.appName);


        //Animacion para que los elementos aparezcan poco a poco
        image.startAnimation(animation1);
        btnPlay.startAnimation(animation1);
        sendNew.startAnimation(animation1);
        text.startAnimation(animation1);

        //Array donde se guardan las preguntas
        questionList = new ArrayList<HashMap<String, String>>();

        final Intent sendNewQuest = new Intent(getApplicationContext(), SendNewQuestion.class);

        sendNew.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                startActivity(sendNewQuest);
            }
        });


        btnPlay.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0) {
                //Starting a new Intent
                questionList.clear();
                //finish();
                new GetQuestions().execute();

            }
        });
    }

    /*ASYNC TASK*/
    private class GetQuestions extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Getting questions..");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            Questions quest = new Questions();

            String jsonStr = quest.makeServiceCall(url, Questions.GET);

            Log.d("Response: ", "> "+jsonStr);

            if(jsonStr != null){
                try{
                    questions = new JSONArray(jsonStr);


                    for (int i = 0; i<questions.length(); i++){

                        JSONObject q = questions.getJSONObject(i);

                        String question = q.getString(TAG_QUESTION);
                        String correct = q.getString(TAG_CORRECT);
                        String second = q.getString(TAG_SECOND);
                        String third = q.getString(TAG_THIRD);
                        String type = q.getString(TAG_TYPE);


                        HashMap<String, String> questionHash = new HashMap<String, String>();

                        questionHash.put(TAG_QUESTION, question);
                        questionHash.put(TAG_CORRECT, correct);
                        questionHash.put(TAG_SECOND, second);
                        questionHash.put(TAG_THIRD, third);
                        questionHash.put(TAG_TYPE, type);


                        questionList.add(questionHash);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else{
                Log.e("Questions", "Could't get any data from the url");
            }

            return null;
        }

        protected void onPostExecute(Void result){
            super.onPostExecute(result);
            if(pDialog.isShowing()) pDialog.dismiss();


            Intent nextScreen = new Intent(getApplicationContext(), inGame.class);

            nextScreen.putExtra("questions", questionList);

            startActivity(nextScreen);
           // finish();
        }

    }




}
