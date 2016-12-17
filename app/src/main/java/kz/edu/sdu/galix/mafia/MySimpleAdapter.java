package kz.edu.sdu.galix.mafia;

/**
 * Created by Рауан on 17.12.2016.
 */

import android.content.Context;
import android.util.Log;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

public class MySimpleAdapter extends SimpleAdapter {
    public MySimpleAdapter(ShowAllRooms context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
    }
    @Override
    public void setViewText(TextView v, String text) {
        String name = text;
        if(v.getId() == R.id.roomName) {
            String info[] = text.split("`");
            name = info[0];
            String _id = info[1];
            Log.d("MyLogs",name + " " +  _id);
//            v.setText(name);
            v.setHint(_id);
        }
        super.setViewText(v, name);
    }
}