package com.example.schoolreservationsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

import static com.example.schoolreservationsapp.R.id.lwReservationbyUser;
import static com.example.schoolreservationsapp.R.id.roomId;

public class DeleteReservationActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;


    public static final String BASE_URI = "http://anbo-roomreservationv3.azurewebsites.net/api/Reservations/user/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_reservation);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();



    }

    @Override
    protected void onStart() {
        super.onStart();
        String userId = firebaseUser.getUid();
        DeleteReservationActivity.ReadTask task = new DeleteReservationActivity.ReadTask();
        task.execute(BASE_URI + userId);
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

    //
    @Override
    protected void onPostExecute(String jsonString) {
        final List<Reservations> reservations = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(jsonString.toString());
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                int id = obj.getInt("id");
                int fromTime = obj.getInt("fromTime");
                int toTime = obj.getInt("toTime");
                String userId = obj.getString("userId");
                String purpose = obj.getString("purpose");
                int roomId = obj.getInt("roomId");
                Reservations reservation = new Reservations(id, fromTime, toTime, userId,purpose, roomId);
                reservations.add(reservation);
            }

/*id	integer($int32)
fromTime	integer($int32)
toTime	integer($int32)
userId	string
purpose	string
roomId	integer($int32)
*/


                populateList(jsonString);
                } catch (JSONException ex) {

                 Log.e("RES", ex.getMessage());
           }
    }

    @Override
    protected void onCancelled(String message) {

        Log.e("RES", message);
    }
}

    private void populateList(String jsonString) {
        Gson gson = new GsonBuilder().create();
        final Reservations[] reservations = gson.fromJson(jsonString, Reservations[].class);
        ListView listView = findViewById(lwReservationbyUser);
        ArrayAdapter<Reservations> adapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_list_item_1, reservations);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                    Toast.makeText(getApplicationContext(),
                            "id: " + id , Toast.LENGTH_LONG);





                    //Toast.makeText(getBaseContext(),parent.getItemAtPosition(position)+ "is selected", Toast.LENGTH_LONG);
                //final String url = "http://anbo-roomreservationv3.azurewebsites.net/api/Reservations/" + Reservations.getId();
                //view.setSelected(true);


                //final String url = "http://anbo-roomreservationv3.azurewebsites.net/api/Reservations/" + id;
                //Reservations book = reservations.get((int) id);
                //Reservations book = reservations[(int) id];
                //Reservations book = (Reservations) parent.getItemAtPosition(position);
            }
        });




    }



}
