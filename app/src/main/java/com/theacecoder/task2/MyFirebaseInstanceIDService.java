package com.theacecoder.task2;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Archit on 27-07-2016.
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "InstanceIDService";
    private RequestQueue queue;
    private MySharedPreference mySharedPreference;
    public static final String URL_CREATE_USER = "http://www.digitalizeindia.com/app/include/token.php";

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        mySharedPreference = new MySharedPreference(getApplicationContext());
        mySharedPreference.saveMyToken(refreshedToken);
        sendTheRegisteredTokenToWebServer(refreshedToken);
    }

    private void sendTheRegisteredTokenToWebServer(final String token){
        queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringPostRequest = new StringRequest(Request.Method.POST, URL_CREATE_USER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());
                JSONObject jObj;
                boolean error = false;
                try {
                    jObj = new JSONObject(response);
                    error = jObj.getBoolean("error");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (error) {
                    Toast.makeText(getApplicationContext(), "Could not register.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "User Registered.", Toast.LENGTH_LONG).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid", mySharedPreference.getUserId());
                params.put("token", token);
                return params;
            }
        };
        queue.add(stringPostRequest);
    }
}
