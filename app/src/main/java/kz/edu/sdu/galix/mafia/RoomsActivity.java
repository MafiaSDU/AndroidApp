package kz.edu.sdu.galix.mafia;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;

import java.util.ArrayList;
import java.util.HashMap;

public class RoomsActivity extends AppCompatActivity {

    EditText room_name,mafias,citizens,doctors,sheriff;
    SharedPreferences spf;
    TextView user_name;
    String[] numbers = new String[15];
    ConnectionToServer connect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms);
        mafias = (EditText) findViewById(R.id.mafia);
        citizens = (EditText) findViewById(R.id.citizens);
        doctors = (EditText) findViewById(R.id.doctors);
        sheriff = (EditText) findViewById(R.id.sheriff);
        room_name = (EditText)findViewById(R.id.room_name);
        spf = getSharedPreferences("id",MODE_PRIVATE);
        user_name = (TextView)findViewById(R.id.creator_name);
        user_name.setText(spf.getString("name",null));
        connect = new ConnectionToServer(this,spf);
    }
    public void create_room(View v){
        HashMap<String,String> room = new HashMap<String,String>();
        room.put("mafia",mafias.getText().toString());
        room.put("sheriff",sheriff.getText().toString());
        room.put("doctor",doctors.getText().toString());
        room.put("citizen",citizens.getText().toString());
        room.put("name",room_name.getText().toString());
        connect.Connect("api/room/add",room, Request.Method.POST);
    }
    public void createList(){
        for(int i=1;i<=10;i++){
            numbers[i]=""+i;
            Log.d("MyLogs",numbers[i]+"");
        }

    }

}
