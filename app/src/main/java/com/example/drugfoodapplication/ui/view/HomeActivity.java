package com.example.drugfoodapplication.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.drugfoodapplication.R;
import com.example.drugfoodapplication.ui.fragments.HomeFragment;
import com.example.drugfoodapplication.ui.fragments.FoodFragment;
import com.example.drugfoodapplication.ui.fragments.InteractionsFragment;
import com.example.drugfoodapplication.ui.fragments.ProfileFragment;
import com.example.drugfoodapplication.ui.fragments.MedicationsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeActivity extends AppCompatActivity {

    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Login/Register'dan gelen kullanıcı maili
        userEmail = getIntent().getStringExtra("USER_EMAIL");
        if (userEmail == null || userEmail.isEmpty()) {
            // Giriş olmadan açılırsa, login'e yönlendir ve bu activity'yi kapat
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }


        // Varsayılan fragment: Home
        loadFragment(HomeFragment.newInstance(userEmail));

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        FloatingActionButton fabHome = findViewById(R.id.fab_home);
        fabHome.setOnClickListener(v -> {
            loadFragment(HomeFragment.newInstance(userEmail));
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_medications) {
                loadFragment(MedicationsFragment.newInstance(userEmail));
                return true;
            } /*else if (id == R.id.nav_food) {
                loadFragment(FoodFragment.newInstance(userEmail));
                return true;
            } else if (id == R.id.nav_interactions) {
                loadFragment(InteractionsFragment.newInstance(userEmail));
                return true;
            } else if (id == R.id.nav_profile) {
                loadFragment(ProfileFragment.newInstance(userEmail));
                return true;
            }*/
            return false;
        });

    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
