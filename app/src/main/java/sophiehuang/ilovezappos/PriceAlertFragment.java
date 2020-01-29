package sophiehuang.ilovezappos;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;


public class PriceAlertFragment extends Fragment {
    DatabaseReference ref;
    FirebaseJobDispatcher jobDispatcher;
    Button btnSetAlert;
    EditText etPriceToAlert;
    TextView tvRecentAlerts;
    ConstraintLayout myLayout;
    ArrayList<String> priceList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_price_alert, container, false);

        btnSetAlert = view.findViewById(R.id.btnSetAlert);
        etPriceToAlert = view.findViewById(R.id.etPriceToAlert);
        tvRecentAlerts = view.findViewById(R.id.tvRecentAlerts);
        ref = FirebaseDatabase.getInstance().getReference().child("PricesToAlert");
        myLayout = view.findViewById(R.id.myLayout);


        btnSetAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = ref.push().getKey();
                String strPrice = etPriceToAlert.getText().toString();

                if (Float.parseFloat(strPrice) >= 0) {
                    ref.child(key).setValue(strPrice);
                    etPriceToAlert.setText("");
                    etPriceToAlert.requestFocus();
                    Toast.makeText(getActivity(), "Your hourly alert has been set up.", Toast.LENGTH_LONG).show();
                } else {
                    etPriceToAlert.setText("");
                    Toast.makeText(getActivity(), "Please enter a valid price.", Toast.LENGTH_LONG).show();
                }

                try {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etPriceToAlert.getWindowToken(), 0);
                } catch (Exception e) {
                }

            }
        });

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                priceList = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    priceList.add(String.valueOf(ds.getValue()));
                }

                StringBuilder builder = new StringBuilder();
                for (String p : priceList) {
                    builder.append(p + "\n");
                }

                tvRecentAlerts.setText(builder.toString());
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return view;
    }


    public void startJob(View view) {

    }
}
