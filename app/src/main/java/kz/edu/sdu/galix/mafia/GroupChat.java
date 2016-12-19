package kz.edu.sdu.galix.mafia;

import android.app.DownloadManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class GroupChat extends AppCompatActivity {
    LinearLayout groupChat;
    EditText userMessage;
    ImageView sendMessage;
    ConnectionToServer connection;
    SharedPreferences spf;
    String ROOM_ID, USER_ID, USER_NAME;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        spf = getSharedPreferences("data", MODE_PRIVATE);
        connection = new ConnectionToServer(this, spf);
        ROOM_ID = spf.getString("room_id", "");
        USER_ID = spf.getString("user_id", "");
        USER_NAME = spf.getString("user_name", "");
        groupChat = (LinearLayout) findViewById(R.id.groupChat);
        userMessage = (EditText) findViewById(R.id.userMessage);
        sendMessage = (ImageView) findViewById(R.id.sendMessage);
        Intent i = getIntent();
        setTitle(i.getStringExtra("roomName"));
        Log.d("MyLogs", "roomName " +i.getStringExtra("roomName"));
        new GetMessage().execute();
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MyLogs", "sendMessage " + ROOM_ID + " " + USER_ID);
                String message = userMessage.getText().toString();
                HashMap<String, String> map = new HashMap<>();
                map.put("message", message);
                map.put("roomId", ROOM_ID);
                map.put("userId", USER_ID);
                map.put("userName", USER_NAME);
                connection.Connect("api/chat/add", map, Request.Method.POST);

                userMessage.setText("");
            }
        });
    }


    class GetMessage extends AsyncTask<Void, String, String>{
        @Override
        protected String doInBackground(Void... params) {
            String messages;
            connection.Connect(("api/chat/" + ROOM_ID), null, Request.Method.GET);
            while(true){
                if(connection.getParams() != null) {
                    messages = connection.getParams();
                    break;
                }
            }
            return messages;
        }
        @Override
        protected void onPostExecute(String data) {
            groupChat.removeAllViews();
            try {
                JSONArray messages = new JSONArray(data);
                LinearLayout.LayoutParams rlp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 10);
                for(int i = 0; i < messages.length(); i++) {
                    JSONObject message = new JSONObject("" + messages.get(i));
//                    JSONObject user = new JSONObject("" + message.getString("user"));
                    Log.d("MyLogs", "message - " +   messages);
                    TextView textView = new TextView(GroupChat.this);
                    LayoutInflater inflater = getLayoutInflater();
                    int layout = R.layout.chat_item_left;
                    if(message.getString("userId").equals(USER_ID)) {
                        layout = R.layout.chat_item_right;
                    }

                    View view = inflater.inflate(layout, groupChat, false);

//                    Date date = new Date(message.getInt("date"));
//                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
                    Log.d("MyLogs", "date - " +   message.getString("date"));

                    TextView chat_message = ((TextView)view.findViewById(R.id.chat_message));
                    TextView chat_user = ((TextView)view.findViewById(R.id.chat_user));
                    TextView chat_time = ((TextView)view.findViewById(R.id.chat_time));
                    chat_time.setText(message.getString("date"));
                    chat_message.setText(message.getString("message"));
                    chat_user.setText(message.getString("userName"));
                    LinearLayout ll = (LinearLayout)view.findViewById(R.id.chat_item_layout);
                    ll.setPadding(15,15,15,15);
                    groupChat.addView(view);
                    textView.setLayoutParams(rlp);
                    groupChat.addView(textView);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            super.onPostExecute(data);
        }

    }
}
