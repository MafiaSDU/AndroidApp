package kz.edu.sdu.galix.mafia;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;

public class infoAboutRoom extends AppCompatActivity implements View.OnClickListener{
    TextView roomName, numberMafia, numberDoctor, numberCitizen, numberSheriff, creatorName, players;
    Button btn_connect_to_room, btn_go_to_chat;
    ProgressBar progressBarInfoAboutRoom;
    LinearLayout LLInfoAboutRoom;
    ConnectionToServer connection;
    SharedPreferences spf;
    String roomId;
    String URL;
    JSONObject room;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_about_room);
        connection = new ConnectionToServer(this, spf);
        spf = getSharedPreferences("data",MODE_PRIVATE);

        btn_connect_to_room = (Button)findViewById(R.id.btnConnectToRoom);
        btn_go_to_chat = (Button) findViewById(R.id.btnGoToChat);
        btn_connect_to_room.setOnClickListener(this);
        btn_go_to_chat.setOnClickListener(this);

        btn_go_to_chat.setVisibility(View.GONE);

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
        Log.d("MyLogs", "infoAboutRoom " + spf.getString("room_id", ""));
        if(spf.getString("room_id", "").equals(roomId)) {
            btn_connect_to_room.setVisibility(View.GONE);
            btn_go_to_chat.setVisibility(View.VISIBLE);
        }
        new MyTask2().execute(("api/room/" + roomId));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnConnectToRoom:
                new MyTask2().execute("api/room/addUser");
                break;
//            case R.id.btn_go_to_chat:
//
//                break;
        }
    }

    class MyTask2 extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String[] params) {
            String url = params[0];
            URL = url;
            if(url.equals("api/room/addUser")) {
                HashMap<String, String> map = new HashMap<>();
                map.put("roomId", roomId);
                map.put("userId", spf.getString("user_id", ""));
                try {
                    map.put("count", "" + room.getInt("count"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                connection.Connect(url, map, Request.Method.POST);
            } else {
                connection.Connect(url, null, Request.Method.GET);
            }
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
        protected void onPostExecute(String data) {
            Log.d("MyLogs", "data - " + data + "     ---" + URL+ " : " + ("api/room/" + roomId));
            try {
                if(URL.equals(("api/room/" + roomId))) {
                    room = new JSONObject(data);
                    JSONObject user = new JSONObject(room.get("creator") + "");
                    numberMafia.setText(room.get("mafia").toString());
                    numberDoctor.setText(room.get("doctor").toString());
                    numberCitizen.setText(room.get("citizen").toString());
                    numberSheriff.setText(room.get("sheriff").toString());
                    players.setText(room.get("count") + " / " + room.get("number"));
                    creatorName.setText(user.get("name").toString());
                    progressBarInfoAboutRoom.setVisibility(View.GONE);
                    LLInfoAboutRoom.setVisibility(View.VISIBLE);
                } else {
                    btn_connect_to_room.setVisibility(View.GONE);
                    btn_go_to_chat.setVisibility(View.VISIBLE);
                    SharedPreferences.Editor edit = spf.edit();
                    edit.remove("room_id");
                    edit.putString("room_id", room.getString("_id"));
                    Log.d("MyLogs", "getString" + room.getString("_id"));
                    Toast.makeText(infoAboutRoom.this,"Success " , Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(data);
        }


    }
}
