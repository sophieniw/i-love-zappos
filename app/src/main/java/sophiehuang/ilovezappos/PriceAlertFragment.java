package sophiehuang.ilovezappos;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;

import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

import sophiehuang.ilovezappos.Model.JobDispatcher.MyJobService;

import static com.firebase.jobdispatcher.FirebaseJobDispatcher.SCHEDULE_RESULT_SUCCESS;


public class PriceAlertFragment extends Fragment {
    private DatabaseReference ref;
    private Button btnSetAlert, btnResetAlert;
    private EditText etPriceToAlert;
    private ConstraintLayout myLayout;


    FirebaseJobDispatcher jobDispatcher;
    private static final String JOB_TAG = "Price_Alert_Job_Tag";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_price_alert, container, false);

        btnSetAlert = view.findViewById(R.id.btnSetAlert);
        btnResetAlert = view.findViewById(R.id.btnResetAlert);
        etPriceToAlert = view.findViewById(R.id.etPriceToAlert);
        ref = FirebaseDatabase.getInstance().getReference().child("PriceToAlert");
        myLayout = view.findViewById(R.id.myLayout);

        jobDispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(getActivity()));


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    btnSetAlert.setEnabled(false);
                    etPriceToAlert.setText(String.valueOf(dataSnapshot.getValue()));
                    etPriceToAlert.setEnabled(false);
                } else {
                    btnResetAlert.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnSetAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String strPrice = etPriceToAlert.getText().toString();

                //check if the price entered by the user is null or negative
                if (Float.parseFloat(strPrice) >= 0 && strPrice != null) {
                    ref.setValue(strPrice);
                    etPriceToAlert.setText(strPrice);
                    etPriceToAlert.setEnabled(false);
                    btnSetAlert.setEnabled(false);
                    btnResetAlert.setEnabled(true);
                    etPriceToAlert.requestFocus();

                    startJob(view);

                } else {
                    etPriceToAlert.setText("");
                    Toast.makeText(getActivity(), "Please enter a valid price.", Toast.LENGTH_LONG).show();
                }

                //below to close the keyboard after setting the price alert
                try {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etPriceToAlert.getWindowToken(), 0);
                } catch (Exception e) {
                }

            }
        });

        btnResetAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopJob(view);
                btnSetAlert.setEnabled(true);
                btnResetAlert.setEnabled(false);
                etPriceToAlert.setEnabled(true);
                etPriceToAlert.setText("");
            }
        });


        return view;
    }


    public void startJob(View view) {
//        final int INTERVAL= (int) TimeUnit.HOURS.toSeconds(1); // every 1 hour to start the job
//        final int END = (int)TimeUnit.MINUTES.toSeconds(5); // 5 mins delay window
        Job job = jobDispatcher.newJobBuilder()
                .setService(MyJobService.class)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTag(JOB_TAG)
                .setTrigger(Trigger.executionWindow(5, 5))
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .setReplaceCurrent(false)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .build();
        jobDispatcher.mustSchedule(job);

        Toast.makeText(getActivity(), "Your price alert has been set.", Toast.LENGTH_LONG).show();
        Log.d("START_JOB_IN_FRAG:", "JobDispatcher Schedule Result: " + (jobDispatcher.schedule(job) == SCHEDULE_RESULT_SUCCESS));
    }

    public void stopJob(View view) {
        jobDispatcher.cancel(JOB_TAG);
        ref.setValue(null);
        etPriceToAlert.setText("");
        Toast.makeText(getActivity(), "Your price alert is canceled. You may set a new price alert.", Toast.LENGTH_LONG).show();
    }


}
