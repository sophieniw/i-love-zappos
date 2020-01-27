package sophiehuang.ilovezappos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.mikephil.charting.data.Entry;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sophiehuang.ilovezappos.Model.BitstampJsonApi;
import sophiehuang.ilovezappos.Model.Transaction;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //declare the bottom navigator view with the view id
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_bar);

        //set the title of the bar at top
        setTitle("Coin Zap");

        // below set a navigation select listener so that when users click on different navigation menu options
        // they would be directed to different fragments
        // navigationListener object is defined below
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationListener);

        // to keep the fragment when rotating phones
        if(savedInstanceState==null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new TransactionsFragment()).commit();
        }
    }

    // define navigationListener for the setOnNavigation..Listener above
    private BottomNavigationView.OnNavigationItemSelectedListener navigationListener=new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selectedFragment = null;

            //switch statement to select fragments based on clicked/selected navigation bar item
            switch(menuItem.getItemId()){
                case R.id.nav_transactions:
                    selectedFragment=new TransactionsFragment();
                    break;
                case R.id.nav_order_book:
                    selectedFragment=new OrderBookFragment();
                    break;
                case R.id.nav_price_alert:
                    selectedFragment=new PriceAlertFragment();
                    break;
            }

            //replace the current fragment with the selected fragment
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();

            return true;
        }
    };

}

