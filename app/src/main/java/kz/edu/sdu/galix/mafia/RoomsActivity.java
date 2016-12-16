package kz.edu.sdu.galix.mafia;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

public class RoomsActivity extends AppCompatActivity {
    Spinner mafias,citizens,doctors,sheriff;
    EditText room_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms);
        mafias = (Spinner)findViewById(R.id.mafia);
        citizens = (Spinner)findViewById(R.id.citizens);
        doctors = (Spinner)findViewById(R.id.doctors);
        sheriff = (Spinner)findViewById(R.id.sheriff);
        room_name = (EditText)findViewById(R.id.room_name);
    }

}
