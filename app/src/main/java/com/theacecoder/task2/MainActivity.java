package com.theacecoder.task2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private EditText et;
    private MySharedPreference mySharedPreference;
    private Button button;
    private TextView tv;
    private JSONParser jsonParser;
    private static MainActivity mainActivity;
    public static final String URL_GET_TOKEN = "http://www.digitalizeindia.com/app/include/gettoken.php";
    private static ArrayList<Chat> arrayChat = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivity = this;
        jsonParser = new JSONParser();
        mySharedPreference = new MySharedPreference(this);
        if(mySharedPreference.getUserId().equals("unset")) {
            assignUserName();
        }

        et = (EditText) findViewById(R.id.et);
        tv = (TextView) findViewById(R.id.tv1);
        button = (Button) findViewById(R.id.button);

        tv.setText("Your Username is " + mySharedPreference.getUserId());

        if (getIntent().getExtras() != null) {
            String a=null,m=null;
            for (String key : getIntent().getExtras().keySet()) {
                String value = getIntent().getExtras().getString(key);
                if(key.equals("mytoken")) {
                    mySharedPreference.saveChatToken(value);
                }
                if(key.equals("author")) {
                    a = value;
                }
                else if(key.equals("message")) {
                    m = value;
                }
                Log.d(TAG, "Key: " + key + " Value: " + value);
            }
            Chat c = new Chat(m,a);
            arrayChat.add(c);
            if(mySharedPreference.getChatStatus()) {
                ChatActivity.getInstance().getChatAdapter().notifyDataSetChanged();
            }
            else {
                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                startActivity(intent);
            }
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chatid = et.getText().toString();
                new GetChatToken().execute(chatid);
            }
        });

        if(mySharedPreference.getFromNotification()) {
            Intent intent = new Intent(MainActivity.this, ChatActivity.class);
            startActivity(intent);
            mySharedPreference.setFromNotification(false);
        }

    }

    public static MainActivity getInstance() {
        return mainActivity;
    }

    public ArrayList<Chat> getChats() {
        return arrayChat;
    }

    public void assignUserName() {
        char[] randomchars = "0123456789abcdefghijklmopqrstubwxyz".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for(int i=0;i<=5;i++) {
            char c = randomchars[random.nextInt(randomchars.length)];
            sb.append(c);
        }
        mySharedPreference.saveUserID(sb.toString());
    }

    class GetChatToken extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
        }
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("uid", args[0]));

            // getting JSON string from URL
            JSONObject json = jsonParser.makeHttpRequest(URL_GET_TOKEN, "GET", params);
            Log.d("Chat JSON: ", json.toString());
            boolean error = false;
            String chattoken = null;
            try {
                chattoken = json.getString("token");
                error = json.getBoolean("error");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(error)
                return null;
            else
                return chattoken;
        }

        protected void onPostExecute(String param) {
            if(param == null) {
                Toast.makeText(getApplicationContext(), "User Name does not exist!", Toast.LENGTH_SHORT).show();
            }
            else {
                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                mySharedPreference.saveChatToken(param);
                startActivity(intent);
                //Toast.makeText(getApplicationContext(), param, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
