package kz.edu.sdu.galix.mafia;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;

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
import java.util.Map;

public class ConnectionToServer {
    String url ="https://rauan-android-backend.herokuapp.com/";
    Context context;
    String response = "";
    String resp, id;
    SharedPreferences spf ;
    private RequestQueue mRequestQueue;
    private DiskBasedCache mCache;
    ConnectionToServer(Context context, SharedPreferences spf){
        this.context = context;
        this.spf = spf;
    }
    public void Connect(final String urlPath, final HashMap<String, String> params, int method) {

        mRequestQueue = VolleySingleton.getInstance(context.getApplicationContext()).getRequestQueue
                (context.getApplicationContext());

        StringRequest request = new StringRequest(
                method,
                url + urlPath,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("MyLogs", "Response is: "+ response);
                        resp = response;
                        if(urlPath.equals("api/user/add")){
                            try {
                                JSONObject json = new JSONObject(response);
                                Log.d("MyLogs",json.getString("_id"));
                                SharedPreferences.Editor editor = spf.edit();
                                editor.putString("id",json.getString("_id"));
                                editor.commit();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if(urlPath=="api/room/add"){

//                            try {
//                                JSONObject json = new JSONObject(response);
                                Log.d("MyLogs",response);
//                                SharedPreferences.Editor editor = spf.edit();
//                                editor.putString("rooid",json.getString("_id"));
//                                editor.commit();
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
                        }

                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("MyLogs", error.toString());
                    }
                }){
            protected HashMap<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
        request.setTag("POST");
        mRequestQueue.add(request);
    }
    public String getParams(){
        return resp != null ? resp : null;
    }
}