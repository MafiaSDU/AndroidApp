package kz.edu.sdu.galix.mafia;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by root on 12/14/16.
 */
public class VolleySingleton {
    private static VolleySingleton mInstance;
    private RequestQueue mRequestQueue;

    public static VolleySingleton getInstance(Context context){
        if(mInstance==null){
            mInstance = new VolleySingleton(context);
        }
        return mInstance;
    }
    private VolleySingleton(Context context){
        mRequestQueue = getRequestQueue(context);
    }
    public RequestQueue getRequestQueue(Context context){
        if(mRequestQueue==null){
            mRequestQueue = Volley.newRequestQueue(context);
            
        }
        return mRequestQueue;
    }
}
