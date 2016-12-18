package kz.edu.sdu.galix.mafia;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    String url ="https://rauan-android-backend.herokuapp.com/";
    EditText ed_name;
    ConnectionToServer connection;
    Button start,go_to_rooms, btn_all_rooms;
    ProgressBar pb;
    SharedPreferences spf;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start = (Button)findViewById(R.id.btn_add);
        btn_all_rooms = (Button) findViewById(R.id.btn_all_rooms);
        btn_all_rooms.setOnClickListener(this);

        go_to_rooms = (Button)findViewById(R.id.btn_go_to_rooms);
        go_to_rooms.setVisibility(View.INVISIBLE);
        go_to_rooms.setOnClickListener(this);
        ed_name = (EditText)findViewById(R.id.name);
        pb = (ProgressBar)findViewById(R.id.pb1);
        spf = getSharedPreferences("data",MODE_PRIVATE);
        connection = new ConnectionToServer(this, spf);
        Log.d("MyLogs", "MainActivity " + spf.getString("room_id", ""));

        Intent i = new Intent(MainActivity.this,ShowAllRooms.class);
        startActivity(i);
    }


    public void add(View v){
        HashMap<String,String> user = new HashMap<>();
        user.put("name", ed_name.getText().toString());
        connection.Connect("api/user/add",user, Request.Method.POST);
        pb.setVisibility(View.VISIBLE);
        editor = spf.edit();
        editor.putString("name",ed_name.getText().toString());
        ed_name.setText("");
        start.setEnabled(false);
        go_to_rooms.setVisibility(View.VISIBLE);
        Intent i = new Intent(MainActivity.this,RoomsActivity.class);
        startActivity(i);
    }

    @Override
    protected void onDestroy() {
        Log.d("Log","Destroyed");
        HashMap<String,String> id = new HashMap<>();
        id.put("user_id",spf.getString("id",null));
        HashMap<String,String> room_id = new HashMap<>();
        room_id.put("room_id",spf.getString("room_id",null));
        editor = spf.edit();
        Log.d("Log","id"+spf.getString("id",null));
        connection.Connect("api/user/delete",id, Request.Method.POST);
        connection.Connect("api/room/"+spf.getString("room_id", null), room_id, Request.Method.DELETE);
        editor.remove("user_id");
        editor.remove("user_name");
        editor.remove("room_id");
        editor.commit();
        Log.d("Log","id"+spf.getString("id",null));
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_all_rooms:
                Intent i = new Intent(MainActivity.this,ShowAllRooms.class);
                startActivity(i);
                break;
//            case R.id.go_to_rooms:
//                Intent i1 = new Intent(MainActivity.this,ShowAllRooms.class);
//                startActivity(i1);
//                break;
        }
    }
}