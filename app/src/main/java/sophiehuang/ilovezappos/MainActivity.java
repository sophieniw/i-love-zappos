package sophiehuang.ilovezappos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

//==========================================
// CODE SNAPSHOT
// the main activity creates and handles a bottom navigation view with 3 fragments:
// 1. TransactionsFragment: to display transaction history
// 2. OrderBookFragment: to display the recent bids and asks details
// 3. PriceAlertFragment: to allow users to set or reset an hourly price alert
//
//

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set the title of the bar at top
        setTitle("Coin Zap");

        //declare variables that would be used later
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_bar);

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

