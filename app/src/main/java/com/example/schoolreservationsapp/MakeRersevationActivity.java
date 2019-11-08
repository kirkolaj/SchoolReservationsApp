package com.example.schoolreservationsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MakeRersevationActivity extends AppCompatActivity {

    private Calendar meetingStart = Calendar.getInstance();

    private Button fTimeButton;
    private Button tTimeButton;
    Toolbar toolbar;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_rersevation);
        fTimeButton = findViewById(R.id.fromTimeButton);
        tTimeButton = findViewById(R.id.toTimeButton);
        toolbar = findViewById(R.id.toolbar);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();





        toolbar.setTitle("Make Reservation");
    }





    public void fromTimePickButtonClicked(View view) {
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                meetingStart.set(Calendar.HOUR_OF_DAY, hourOfDay);
                meetingStart.set(Calendar.MINUTE, minute);
                DateFormat df = DateFormat.getTimeInstance();
                String timeString = df.format(meetingStart.getTimeInMillis());
                // TODO do not print the seconds from the time (only hours and minutes)
                fTimeButton.setText(timeString);
            }
        };
        Calendar calendar = Calendar.getInstance();
        int currentHourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);
        TimePickerDialog dialog1 = new TimePickerDialog(this, timeSetListener, currentHourOfDay,
                currentMinute, true);
        dialog1.show();
    }


    public void toTimePickButtonClicked(View view) {
        TimePickerDialog.OnTimeSetListener timeSetListener1 = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                meetingStart.set(Calendar.HOUR_OF_DAY, hourOfDay);
                meetingStart.set(Calendar.MINUTE, minute);
                DateFormat df = DateFormat.getTimeInstance();
                String timeString1 = df.format(meetingStart.getTimeInMillis());
                // TODO do not print the seconds from the time (only hours and minutes)
                tTimeButton.setText(timeString1);
            }
        };
        Calendar calendar = Calendar.getInstance();
        int currentHourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);
        TimePickerDialog dialog2 = new TimePickerDialog(this, timeSetListener1, currentHourOfDay,
                currentMinute, true);
        dialog2.show();
    }

    public void btnAddReservation(View view) {
        String userId = firebaseUser.getUid();
        int fromTime = ((int)meetingStart.getTimeInMillis()/1000);
        int toTime = ((int)meetingStart.getTimeInMillis()/1000);
        //int toTime = ((EditText) findViewById(R.id.add_book_author_edittext)).getText().toString();
        String purpose = ((EditText) findViewById(R.id.makeReservationpPurpose)).getText().toString();
        String roomId = ((EditText) findViewById(R.id.roomId)).getText().toString();

        try { // Alternative: make a Book object + use Gson
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("fromTime", fromTime);
            jsonObject.put("toTime", toTime);
            jsonObject.put("userId", userId);
            jsonObject.put("purpose", purpose);
            jsonObject.put("roomId", roomId);
            String jsonDocument = jsonObject.toString();
            //messageView.setText(jsonDocument);
            PostBookOkHttpTask task = new PostBookOkHttpTask();
            task.execute("http://anbo-roomreservationv3.azurewebsites.net/api/reservations", jsonDocument);
        } catch (JSONException ex) {
            //messageView.setText(ex.getMessage());
        }
    }


    private class PostBookOkHttpTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            // https://trinitytuts.com/get-and-post-request-using-okhttp-in-android-application/
            String url = strings[0];
            String postdata = strings[1];
            MediaType MEDIA_TYPE = MediaType.parse("application/json");
            // https://stackoverflow.com/questions/57100451/okhttp3-requestbody-createcontenttype-content-deprecated
            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(postdata, MEDIA_TYPE);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .build();
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    return response.body().string();
                } else {
                    String message = url + "\n" + response.code() + " " + response.message();
                    return message;
                }
            } catch (IOException ex) {
                Log.e("RESERVATION", ex.getMessage());
                return ex.getMessage();
            }

        }

        @Override
        protected void onPostExecute(String jsonString) {
            super.onPostExecute(jsonString);
            //TextView messageView = findViewById(R.id.add_book_message_textview);
            //messageView.setText(jsonString);
            Log.d("MINE", jsonString);
             // finish();
        }

        @Override
        protected void onCancelled(String message) {
            super.onCancelled(message);
            //TextView messageView = findViewById(R.id.add_book_message_textview);
            //messageView.setText(message);
            Log.d("MINE", message);
            //finish();
        }
    }
}

