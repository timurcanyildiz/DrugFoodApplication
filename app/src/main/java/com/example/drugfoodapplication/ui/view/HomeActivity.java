package com.example.drugfoodapplication.ui.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.drugfoodapplication.R;
import com.example.drugfoodapplication.ui.fragments.HomeFragment;
import com.example.drugfoodapplication.ui.fragments.FoodFragment;
import com.example.drugfoodapplication.ui.fragments.InteractionsFragment;
import com.example.drugfoodapplication.ui.fragments.MedicationsFragment;
import com.example.drugfoodapplication.ui.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeActivity extends AppCompatActivity {

    private String userEmail; // Giriş yapan kullanıcının emaili burada tutulur

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // E-posta bilgisini sakla: Tema/dil değişimiyle Activity tekrar yaratılırsa kaybolmasın!
        if (savedInstanceState != null) {
            userEmail = savedInstanceState.getString("USER_EMAIL");
        } else {
            userEmail = getIntent().getStringExtra("USER_EMAIL");
        }

        // Eğer userEmail boşsa giriş ekranına gönder
        if (userEmail == null || userEmail.isEmpty()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // Profil sayfası dışarıdan özel istekle açılacaksa
        boolean openProfile = getIntent().getBooleanExtra("OPEN_PROFILE", false);

        // Alt navigation bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        FloatingActionButton fabHome = findViewById(R.id.fab_home);

        // Hangi sayfa açılacak?
        if (openProfile) {
            loadFragment(ProfileFragment.newInstance(userEmail));
            bottomNavigationView.setSelectedItemId(R.id.nav_profile);
        } else {
            loadFragment(HomeFragment.newInstance(userEmail));
        }

        // FAB'a tıklayınca ana sayfaya dön
        fabHome.setOnClickListener(v -> loadFragment(HomeFragment.newInstance(userEmail)));

        // Navigation menüde fragment geçişlerini yönet
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
            }*/ else if (id == R.id.nav_profile) {
                loadFragment(ProfileFragment.newInstance(userEmail));
                return true;
            }
            return false;
        });
    }

    // userEmail bilgisini activity yeniden yaratılırsa kaybetmemek için kaydediyoruz
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("USER_EMAIL", userEmail);
    }

    // Fragment yükleme fonksiyonu
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
