package com.trodev.businessscanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.firebase.messaging.FirebaseMessaging;
import com.trodev.businessscanner.fragment.AboutFragment;
import com.trodev.businessscanner.fragment.ScanFragment;
import com.trodev.businessscanner.fragment.HomeFragment;
import com.trodev.businessscanner.fragment.NotificationFragment;


public class Dashboard extends AppCompatActivity {

    MeowBottomNavigation bottomNavigation;
    public final static int HOME_ID = 1;
    public final static int SCAN_ID = 2;
    // public final static int NOTIFICATION_ID =3;
    public final static int ABOUTUS_ID = 4;

    private Boolean isSelected = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        FirebaseMessaging.getInstance().subscribeToTopic("SMS");

        Toast.makeText(this, "Please Check your Camera Permission", Toast.LENGTH_LONG).show();

        bottomNavigation = findViewById(R.id.bottomNav);
        bottomNavigation.show(HOME_ID, true);

        bottomNavigation.add(new MeowBottomNavigation.Model(HOME_ID, R.drawable.ic_home));
        bottomNavigation.add(new MeowBottomNavigation.Model(SCAN_ID, R.drawable.ic_qr));
        //  bottomNavigation.add(new MeowBottomNavigation.Model(NOTIFICATION_ID, R.drawable.ic_notification));
        bottomNavigation.add(new MeowBottomNavigation.Model(ABOUTUS_ID, R.drawable.ic_about));


        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                switch (item.getId()) {
                    case HOME_ID:
                        isSelected = false;
                        break;
                    case SCAN_ID:
                        //      case NOTIFICATION_ID:
                    case ABOUTUS_ID:
                        isSelected = true;
                        break;

                }
            }
        });

        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                switch (item.getId()) {
                    case HOME_ID:
                        replaceFragment(new HomeFragment());
                        break;
                    case SCAN_ID:
                        replaceFragment(new ScanFragment());
                        break;
/*                    case NOTIFICATION_ID:
                        replaceFragment(new NotificationFragment());
                        break;*/
                    case ABOUTUS_ID:
                        replaceFragment(new AboutFragment());
                        break;

                }
            }
        });


        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {

            }
        });

    }

    public void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();
    }


    @Override
    public void onBackPressed() {
        if (isSelected) {
            bottomNavigation.show(HOME_ID, true);
            isSelected = false;
        } else {
            super.onBackPressed();
        }
    }
}