package me.alextorres.quizGame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class inGame extends Activity {

    ProgressBar progressBar;
    CountDownTimer gameTimer;
    long countdownPeriod;
    long sum;

    long points;
    int contador;
    int sumP;


    LinearLayout answerA, answerB, answerC;
    TextView textQuestion, textAnswerA, textAnswerB, textAnswerC;

    TextView textPoints;

    String question, trueAnswer, secondAnswer, thirdAnswer, type;

    ArrayList<HashMap<String, String>> questionList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_in_game);

        Intent i = getIntent();

        questionList = (ArrayList<HashMap<String, String>>) i.getSerializableExtra("questions");


        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        progressBar.setMax(8000);
        countdownPeriod = 4000;
        sum = 4000;

        points = 0;
        contador = 0;
        sumP = 5;

        answerA = (LinearLayout) findViewById(R.id.answerA);
        answerB = (LinearLayout) findViewById(R.id.answerB);
        answerC = (LinearLayout) findViewById(R.id.answerC);
        textQuestion = (TextView) findViewById(R.id.pregunta);

        textAnswerA = (TextView) findViewById(R.id.textAnswerA);
        textAnswerB = (TextView) findViewById(R.id.textAnswerB);
        textAnswerC = (TextView) findViewById(R.id.textAnswerC);

        textPoints = (TextView) findViewById(R.id.points);


        final Intent gameOver = new Intent(getApplicationContext(), gameOver.class);
        gameOver.putExtra("questions", questionList);




        answerA.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(gameTimer != null){
                    gameTimer.cancel();
                }

                if(checkAnswer(textAnswerA)){
                    createCountDown();
                    addPoints();
                    newQuestion();
                }
                else{
                    gameOver.putExtra("points", points);
                    startActivity(gameOver);
                    finish();
                }


            }
        });

        answerB.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(gameTimer != null){
                    gameTimer.cancel();
                }

                if(checkAnswer(textAnswerB)){
                    createCountDown();
                    addPoints();
                    newQuestion();
                }
                else{
                    gameOver.putExtra("points", points);
                    startActivity(gameOver);
                    finish();
                }


            }
        });

        answerC.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(gameTimer != null){
                    gameTimer.cancel();
                }

                if(checkAnswer(textAnswerC)){
                    createCountDown();
                    addPoints();
                    newQuestion();
                }
                else{
                    gameOver.putExtra("points", points);
                    startActivity(gameOver);
                    finish();
                }


            }
        });

        newQuestion();
        createCountDown();
    }

    private void addPoints(){
        if(contador==10){
            sumP+=5;
            contador=0;
        }
        points += sumP;
        contador++;

        textPoints.setText(points+"");
    }

    private boolean checkAnswer(TextView answ){

        if(answ.getText().equals(trueAnswer)){
            return true;
        }

        return false;
    }


    private void changeBackground(int color){
        LinearLayout preguntaBack = (LinearLayout) findViewById(R.id.preguntaBack);

        preguntaBack.setBackgroundColor(getResources().getColor(color));
    }

    private void createCountDown(){

        gameTimer = new CountDownTimer(countdownPeriod + sum, 1) {
            @Override
            public void onTick(long l) {
                int prog = (int) l;
                //Log.v("timer", prog+"");
                progressBar.setProgress(prog);
                progressBar.setMax((int) (countdownPeriod+sum));
            }

            @Override
            public void onFinish() {
                progressBar.setProgress(0);
                final Intent gameOver = new Intent(getApplicationContext(), gameOver.class);
                gameOver.putExtra("questions", questionList);
                gameOver.putExtra("points", points);
                startActivity(gameOver);
                finish();
                //Log.v("timer", "finished");
            }
        }.start();

        sum -=10;


    }

    private void newQuestion(){
        int tam = questionList.size();
        int rand = new Random().nextInt(tam);
        int randAnsw = new Random().nextInt(3);


        question = questionList.get(rand).get("question");
        trueAnswer = questionList.get(rand).get("trueAnswer");
        secondAnswer = questionList.get(rand).get("secondAnswer");
        thirdAnswer = questionList.get(rand).get("thirdAnswer");
        type = questionList.get(rand).get("type");

        if(type.equals("maths")){
            changeBackground(R.color.Amarillo);
        }
        else if(type.equals("arts")){
            changeBackground(R.color.Naranja);
        }
        else if(type.equals("science")){
            changeBackground(R.color.Azul);
        }
        else if(type.equals("geography")){
            changeBackground(R.color.Verde);
        }
        else if(type.equals("sports")){
            changeBackground(R.color.Violeta);
        }
        else if(type.equals("history")){
            changeBackground(R.color.Rojo);
        }

        textQuestion.setText(question);
        switch(randAnsw){
            case 0: textAnswerA.setText(trueAnswer);
                    textAnswerB.setText(secondAnswer);
                    textAnswerC.setText(thirdAnswer);
                    break;
            case 1: textAnswerC.setText(trueAnswer);
                    textAnswerA.setText(secondAnswer);
                    textAnswerB.setText(thirdAnswer);
                    break;
            case 2: textAnswerB.setText(trueAnswer);
                    textAnswerC.setText(secondAnswer);
                    textAnswerA.setText(thirdAnswer);
                    break;
        }
    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.v("finish", "finished");
        if(gameTimer != null){
            gameTimer.cancel();
        }
        this.finish();
    }



}
