package kz.edu.sdu.galix.mafia;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ShowAllRooms extends AppCompatActivity {
    String url ="https://rauan-android-backend.herokuapp.com/";
    TextView tv;
    ListView listView;
    ArrayList<HashMap<String, String>> data;
    MySimpleAdapter adapter;
    ConnectionToServer connection;
    SharedPreferences spf;
    int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_rooms);

        connection = new ConnectionToServer(this, spf);
        tv = (TextView) findViewById(R.id.tv);

        listView = (ListView)findViewById(R.id.listView);
        data = new ArrayList<>();
//                HashMap<String, String> map;
//                for(int i = 0; i < 10; i++) {
//                    map = new HashMap<>();
//                    map.put("roomName", "Room2` " + (i + 1));
//                    map.put("creatorName", "Creator Name2  " + (i + 1));
//                    map.put("number", (i + 1) + " / 20");
//                    data.add(map);
//                }
        adapter = new MySimpleAdapter(
            this,
            data,
            R.layout.item,
            new String[]{"roomName", "creatorName", "number"},
            new int[]{R.id.roomName, R.id.creatorName, R.id.number}
        );
        listView.setAdapter(adapter);

        new MyTask().execute();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView v = (TextView) view.findViewById(R.id.roomName);
                count++;
                Log.d("MyLogs", v.getHint().toString());

//                Intent intent = new Intent(ShowAllRooms.this, Chat.class);
//                startActivity(intent);
//                data.get(0).put("number", count + " / 10");
//                data.removeAll(data);
//                HashMap<String, String> map;
//                for(int i = 0; i < 10; i++) {
//                    map = new HashMap<>();
//                    map.put("roomName", "Room2 " + (i + 1));
//                    map.put("creatorName", "Creator Name2  " + (i + 1));
//                    map.put("number", (i + 1) + " / 20");
//                    data.add(map);
//                }
//
//                adapter.notifyDataSetChanged();
            }
        });
    }

    class MyTask extends AsyncTask<Void, String, String> {
        @Override
        protected String doInBackground(Void[] params) {
            connection.Connect("api/room", null, Request.Method.GET);
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
                JSONArray rooms = new JSONArray(allRooms);
                for(int i = 0; i < rooms.length(); i++) {
                    JSONObject room = new JSONObject("" + rooms.get(i));
                    JSONObject creator = new JSONObject("" + room.get("creator"));
                    Log.d("MyLogs", "Room -  " + room.get("name") + " - " + creator.get("name") + " - " +  room.get("count") + " / " +  room.get("number"));
                    HashMap<String, String> map;
                    map = new HashMap<>();
                    map.put("roomName", room.get("name").toString() + "`" + room.get("_id").toString());
                    map.put("creatorName", creator.get("name").toString());
                    map.put("number", room.get("count") + " / " + room.get("number"));
                    data.add(map);
                    adapter.notifyDataSetChanged();
                }
                Log.d("MyLogs", "Dsdsa");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(allRooms);
        }
    }
}
