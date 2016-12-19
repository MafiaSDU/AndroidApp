package kz.edu.sdu.galix.mafia;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import com.android.volley.Request;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    String url ="https://rauan-android-backend.herokuapp.com/";

    ConnectionToServer connection;
    Button create_rooms, btn_all_rooms;
    ProgressBar pb;
    SharedPreferences spf;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_all_rooms = (Button) findViewById(R.id.btn_all_rooms);
        btn_all_rooms.setOnClickListener(this);

        create_rooms = (Button)findViewById(R.id.create_room);
        create_rooms.setOnClickListener(this);

        pb = (ProgressBar)findViewById(R.id.pb1);
        spf = getSharedPreferences("data",MODE_PRIVATE);
        connection = new ConnectionToServer(this, spf);
        if(spf.getString("user_name", "").equals("")) {
            Intent i = new Intent(MainActivity.this,LoginActivity.class);
            startActivityForResult(i, 1);
        }
        Log.d("MyLogs", "MainActivity " + spf.getString("room_id", ""));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1) {
            setTitle(data.getStringExtra("user_name"));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MyLogs","onResume");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HashMap<String,String> data = new HashMap<>();
        data.put("userId",spf.getString("user_id", ""));
        Log.d("MyLogs","data "+data.toString());
        connection.Connect("api/user/delete",data, Request.Method.POST);
        data.put("room_id",spf.getString("room_id", ""));
        data.put("count", spf.getString("count", "3"));
        Log.d("MyLogs","id"+spf.getString("id",null));
        Log.d("MyLogs","data "+data.toString());
        connection.Connect("api/room/"+spf.getString("room_id", null), data, Request.Method.POST);
        editor = spf.edit();
        editor.remove("user_id");
        editor.remove("user_name");
        editor.remove("room_id");
        editor.remove("count");
        editor.commit();
        Log.d("MyLogs","Destroyed");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_all_rooms:
                Intent i = new Intent(MainActivity.this,ShowAllRooms.class);
                startActivity(i);
                break;
            case R.id.create_room:
                Intent i1 = new Intent(MainActivity.this,RoomsActivity.class);
                startActivity(i1);
                break;
        }
    }
}