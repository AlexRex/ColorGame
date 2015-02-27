package me.alextorres.quizGame;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;


public class SendNewQuestion extends Activity {


    private ProgressDialog pDialog;
    private static String url = "http://api.alextorres.me/v1/question";

    // JSON NAMES

    private static final String TAG_QUESTION = "question";
    private static final String TAG_CORRECT = "trueAnswer";
    private static final String TAG_SECOND = "secondAnswer";
    private static final String TAG_THIRD = "thirdAnswer";
    private static final String TAG_TYPE = "typeAnswer";


    private String question, correct, second, third, type;

    private Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_send_new_question);


        spinner = (Spinner) findViewById(R.id.typeSelection);
        List<String> list = new ArrayList<String>();
        list.add("Maths");
        list.add("Science");
        list.add("Arts");
        list.add("Geography");
        list.add("History");
        list.add("Sports");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,list);

        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(dataAdapter);


        Button send = (Button) findViewById(R.id.sendQuestion);



        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PostQuestion().execute();
            }
        });


    }


    /*ASYNC TASK*/
    private class PostQuestion extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(SendNewQuestion.this);
            pDialog.setMessage("Sending question..");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            Questions quest = new Questions();

            question = ((EditText) findViewById(R.id.questionSend)).getText().toString();
            correct = ((EditText) findViewById(R.id.trueAnswerSend)).getText().toString();
            second = ((EditText) findViewById(R.id.secondAnswerSend)).getText().toString();
            third = ((EditText) findViewById(R.id.thirdAnswerSend)).getText().toString();
            type = spinner.getSelectedItem().toString().toLowerCase();

            if(question.trim().equals("") || correct.trim().equals("") || second.trim().equals("") || third.trim().equals("")){
                return null;
            }

            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
            nameValuePair.add(new BasicNameValuePair(TAG_QUESTION, question));
            nameValuePair.add(new BasicNameValuePair(TAG_CORRECT, correct));
            nameValuePair.add(new BasicNameValuePair(TAG_SECOND, second));
            nameValuePair.add(new BasicNameValuePair(TAG_THIRD, third));
            nameValuePair.add(new BasicNameValuePair(TAG_TYPE, type));



            String jsonStr = quest.makeServiceCall(url, Questions.POST, nameValuePair);

            Log.d("Response: ", "> " + jsonStr);



            return null;
        }

        protected void onPostExecute(Void result){
            super.onPostExecute(result);
            if(pDialog.isShowing()) pDialog.dismiss();

        }

    }
}