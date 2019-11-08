package com.example.schoolreservationsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static com.example.schoolreservationsapp.R.id.lwRoomAvailable;

public class RoomAviableActivity extends AppCompatActivity {

    public static final String BASE_URI = "http://anbo-roomreservationv3.azurewebsites.net/api/Rooms";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_aviable);



    }

    @Override
    protected void onStart() {
        super.onStart();
        ReadTask task = new ReadTask();
        task.execute(BASE_URI);

    }
    private class ReadTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... strings) {
            String uri = strings[0];
            OkHttpClient client = new OkHttpClient();
            Request.Builder requestBuilder = new Request.Builder();
            requestBuilder.url(uri);
            Request request = requestBuilder.build();
            Call call = client.newCall(request);
            try {
                Response response = call.execute();
                if (response.isSuccessful()) {
                    ResponseBody responseBody = response.body();
                    String jsonString = responseBody.string();
                    return jsonString;
                } else {
                    cancel(true);
                    return uri + "\n" + response.code() + " " + response.message();
                }
            } catch (IOException ex) {
                cancel(true);
                return ex.getMessage();
            }

        }

        @Override
        protected void onPostExecute(String jsonString) {
            final List<Rooms> rooms = new ArrayList<>();
            try {
                JSONArray array = new JSONArray(jsonString.toString());
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    String name = obj.getString("name");
                    String desc = obj.getString("description");
                    int capacity = obj.getInt("capacity");
                    String remarks = obj.getString("remarks");
                    int id = obj.getInt("id");
                    Rooms room = new Rooms(id, desc, name, capacity, remarks);
                    rooms.add(room);
                }

/*id	integer($int32)
name	string
description	string
capacity	integer($int32)
remarks	string*/

            populateList(jsonString);
            } catch (JSONException ex) {

                Log.e("ROOMS", ex.getMessage());
            }
        }

        @Override
        protected void onCancelled(String message) {

            Log.e("ROOMS", message);
        }
    }
    private void populateList(String jsonString) {
        Gson gson = new GsonBuilder().create();
        final Rooms[] rooms = gson.fromJson(jsonString, Rooms[].class);
        ListView listView = findViewById(lwRoomAvailable);
        ArrayAdapter<Rooms> adapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_list_item_1, rooms);
        listView.setAdapter(adapter);



    }
}
