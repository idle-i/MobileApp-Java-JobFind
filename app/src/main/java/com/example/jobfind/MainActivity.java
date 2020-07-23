package com.example.jobfind;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    static FragmentManager fragmentManager;
    static String search_text = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView main_title = findViewById(R.id.main_title);
        main_title.setText("Jobs List");

        fragmentManager = getSupportFragmentManager();
        final BottomNavigationView bottom_nav = findViewById(R.id.bottom_nav);

        Intent i = getIntent();
        try {
            if (i.getExtras().containsKey("startFavoriteList")) {
                i.removeExtra("startFavoriteList");

                fragmentManager.beginTransaction()
                        .replace(R.id.main, new FragmentFavorite(MainActivity.this), "FragmentFavorite")
                        .commit();
                main_title.setText("Favorite jobs");
                bottom_nav.setSelectedItemId(R.id.menu_2);
            } else {
                fragmentManager.beginTransaction()
                        .add(R.id.main, new FragmentJobList(MainActivity.this, getSupportFragmentManager()))
                        .commit();
            }
        } catch (Exception e) {
            fragmentManager.beginTransaction()
                    .add(R.id.main, new FragmentJobList(MainActivity.this, getSupportFragmentManager()))
                    .commit();
        }

        bottom_nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == bottom_nav.getSelectedItemId()) {
                    return false;
                } else {
                    if (item.getItemId() == bottom_nav.getMenu().getItem(0).getItemId()) {
                        fragmentManager.beginTransaction()
                                .replace(R.id.main, new FragmentJobList(MainActivity.this, getSupportFragmentManager()))
                                .commit();
                        main_title.setText("Jobs List");
                    } else {
                        fragmentManager.beginTransaction()
                                .replace(R.id.main, new FragmentFavorite(MainActivity.this), "FragmentFavorite")
                                .commit();
                        main_title.setText("Favorite jobs");
                    }
                }
                return true;
            }
        });
    }
}
