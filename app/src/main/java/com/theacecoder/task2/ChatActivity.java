package com.theacecoder.task2;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Archit on 27-07-2016.
 */

public class ChatActivity extends ListActivity {

    private static final String TAG = "ChatActivity";
    private RequestQueue queue;
    private EditText editText;
    private ImageButton button;
    ListView listView = null;
    private MySharedPreference mySharedPreference;
    ChatAdapter chatAdapter;
    private static ChatActivity chatActivity;
    public static final String URL_CHAT = "http://www.digitalizeindia.com/app/include/serverfirebase.php";
    private BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                chatAdapter.notifyDataSetChanged();
            }
        };

        chatActivity = this;
        chatAdapter = new ChatAdapter(ChatActivity.this, MainActivity.getInstance().getChats());
        editText = (EditText) findViewById(R.id.messageInput);
        button = (ImageButton) findViewById(R.id.sendButton);
        listView = getListView();
        mySharedPreference = new MySharedPreference(this);
        listView.setAdapter(chatAdapter);
        chatAdapter.notifyDataSetChanged();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = editText.getText().toString();
                if(!message.equals("")) {
                    Chat c = new Chat(message,mySharedPreference.getUserId());
                    MainActivity.getInstance().getChats().add(c);
                    chatAdapter.notifyDataSetChanged();
                    editText.setText("");
                    sendNewMessage(mySharedPreference.getChatToken(),mySharedPreference.getUserId(),message);
                }
                else {
                    Toast.makeText(getApplication(),"Please type something.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        super.onStop();
        mySharedPreference.setChatStatus(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mySharedPreference.setChatStatus(true);
        LocalBroadcastManager.getInstance(this).registerReceiver((receiver),
                new IntentFilter(MyFirebaseMessagingService.COPA_RESULT));
    }

    public static ChatActivity getInstance() {
        return chatActivity;
    }

    public ChatAdapter getChatAdapter() {
        return chatAdapter;
    }

    private void sendNewMessage(final String s1, final String s2, final String s3){
        queue = Volley.newRequestQueue(getApplicationContext());
        mySharedPreference = new MySharedPreference(getApplicationContext());
        StringRequest stringPostRequest = new StringRequest(Request.Method.POST, URL_CHAT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Chat Response: " + response.toString());
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
                params.put("chattoken", s1);
                params.put("mytoken", mySharedPreference.getMyToken());
                params.put("author", s2);
                params.put("message", s3);
                return params;
            }
        };
        queue.add(stringPostRequest);
    }
}
