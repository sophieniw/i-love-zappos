package sophiehuang.ilovezappos.Model.JobDispatcher;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.AsyncTask;

public class MyJobService extends JobService {

    BackgroundTask backgroundTask;

    @Override
    public boolean onStartJob(JobParameters params) {


        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }

    public static class BackgroundTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            return null;
        }
    }
}
