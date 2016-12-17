package kz.edu.sdu.galix.mafia;

import android.content.Intent;
import android.content.SharedPreferences;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class infoAboutRoom extends AppCompatActivity {
    TextView roomName, numberMafia, numberDoctor, numberCitizen, numberSheriff, creatorName, players;
    ProgressBar progressBarInfoAboutRoom;
    LinearLayout LLInfoAboutRoom;
    ConnectionToServer connection;
    SharedPreferences spf;
    String roomId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connection = new ConnectionToServer(this, spf);
        setContentView(R.layout.activity_info_about_room);
        roomName = (TextView) findViewById(R.id.roomName);
        numberMafia = (TextView) findViewById(R.id.numberMafia);
        numberDoctor = (TextView) findViewById(R.id.numberDoctor);
        numberCitizen = (TextView) findViewById(R.id.numberCitizen);
        numberSheriff = (TextView) findViewById(R.id.numberSheriff);
        creatorName = (TextView) findViewById(R.id.creatorName);
        players = (TextView) findViewById(R.id.players);
        LLInfoAboutRoom = (LinearLayout) findViewById(R.id.LLInfoAboutRoom);
        LLInfoAboutRoom.setVisibility(View.GONE);
        progressBarInfoAboutRoom = (ProgressBar) findViewById(R.id.progressBarInfoAboutRoom);
        Intent i = getIntent();
        roomId = i.getStringExtra("roomId");
        roomName.setText(i.getStringExtra("roomName"));
        new MyTask2().execute();
    }

    class MyTask2 extends AsyncTask<Void, String, String> {
        @Override
        protected String doInBackground(Void[] params) {
            connection.Connect(("api/room/" + roomId), null, Request.Method.GET);
            String allRooms;
            while(true) {
                if(connection.getParams() != null) {
                    allRooms = connection.getParams();
                    break;
                }
            }
            return allRooms;
        }

        @Override
        protected void onPostExecute(String allRooms) {
            try {
                JSONObject room = new JSONObject(allRooms);
                JSONObject user = new JSONObject(room.get("creator") + "");
                numberMafia.setText(room.get("mafia").toString());
                numberDoctor.setText(room.get("doctor").toString());
                numberCitizen.setText(room.get("citizen").toString());
                numberSheriff.setText(room.get("sheriff").toString());
                players.setText(room.get("count") + " / " + room.get("number"));
                creatorName.setText(user.get("name").toString());
                progressBarInfoAboutRoom.setVisibility(View.GONE);
                LLInfoAboutRoom.setVisibility(View.VISIBLE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(allRooms);
        }
    }
}
