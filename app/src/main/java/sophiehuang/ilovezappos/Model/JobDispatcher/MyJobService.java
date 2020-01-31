package sophiehuang.ilovezappos.Model.JobDispatcher;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.firebase.jobdispatcher.JobService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import retrofit2.Call;
import retrofit2.Response;
import sophiehuang.ilovezappos.MainActivity;
import sophiehuang.ilovezappos.Model.DataObjects.Ticker;
import sophiehuang.ilovezappos.Model.Retrofit2API.ApiClient;
import sophiehuang.ilovezappos.Model.Retrofit2API.BitstampJsonApi;
import sophiehuang.ilovezappos.R;

//==========================================
// CODE SNAPSHOT
// MyJobService class is meant to be a class that extends JobService from FirebaseJobDispatcher.
// the onStartJob() method triggers an async task that contains a background task;
// in the background task, Ticker object is retrieved through retrofit2 callback  every hour;
// the price of the Ticker object then is compared to the user alert price retrieved from Firebase database
// if the user alert price is lower than the hourly ticker price, then the onPostExecute generate
// and push a notification. Clicking the notification will open the application
//

public class MyJobService extends JobService {

    private AsyncTask mBackgroundTask;
    static BitstampJsonApi bitstampJsonApi;
    static DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("PriceToAlert");
    static String userPrice, lastPrice;
    static boolean lastPriceIsLower;

    private NotificationManager notificationManager;
    private NotificationCompat.Builder notificationBuilder;
    static final int NOTIFICATION_ID = 1;
    static final String CHANNEL_ID = "001";



    @Override
    public boolean onStartJob(com.firebase.jobdispatcher.JobParameters job) {
        new MyAsyncTask().execute();
        jobFinished(job, false);
        return true;
    }

    @Override
    public boolean onStopJob(com.firebase.jobdispatcher.JobParameters job) {
        Toast.makeText(getApplicationContext(), "Job Stopped:" + lastPriceIsLower, Toast.LENGTH_LONG).show();
        if (mBackgroundTask != null) {
            mBackgroundTask.cancel(true);
        }
        return true;
    }


    private class MyAsyncTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean c = checkTicker();
            Log.d("DO_IN_BACKGROUND:", "doInBackground() checker: " + c);
            return c;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            Log.i("ON_POST_EXECUTE", "aboolean: " + aBoolean);
            if (aBoolean) {
                sendNotification();
            }
        }
    }


    private boolean checkTicker() {
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userPrice = String.valueOf(dataSnapshot.getValue());
                Log.d("CHECK_TICKER_LISTENER", "UserPrice from listener in checkticker: " + userPrice);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        bitstampJsonApi = ApiClient.getClient().create(BitstampJsonApi.class);
        Call<Ticker> tickerCall = bitstampJsonApi.getTicker();

        try {
            Response<Ticker> response = tickerCall.execute();
            if (response.isSuccessful()) {
                String lastPrice = response.body().getLast();

                float floatLastPrice = Float.parseFloat(lastPrice);
                float floatUserPrice = Float.parseFloat(userPrice);
                if (floatLastPrice < floatUserPrice) {
                    lastPriceIsLower = true;
                    Log.i("ON_RESPONSE", "lastPriceIsLower inside onResponse: " + lastPriceIsLower);
                } else {
                    Toast.makeText(getApplicationContext(), "going into else statement", Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            Log.e("MY_JOB_SERVICE", "Exception  caught:" + e.getMessage() + " " + userPrice + " " + lastPrice);
        }
        return lastPriceIsLower;
    }


    private void sendNotification() {
        Log.d("SEND_NOTIFICATION()", "Entering sendNotification()");

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 1, intent, 0);

        notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setContentTitle("Coin Zap BTC Alert")
                .setContentText("Hourly Alert: BTC price has fallen below your alert price!")
                .setSmallIcon(R.drawable.ic_priority_high_black_24dp)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Notification from Coin Zap", NotificationManager.IMPORTANCE_HIGH);
        notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
        notificationBuilder.setChannelId(CHANNEL_ID);

        Log.d("SEND_NOTIFICATION()", notificationBuilder.toString());

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }




}
