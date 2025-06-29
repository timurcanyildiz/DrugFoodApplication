package com.example.drugfoodapplication.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.drugfoodapplication.R;
import com.example.drugfoodapplication.ui.viewmodel.HomeViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    TextView dateText, welcomeUser, remainingTime;
    BottomNavigationView bottomNavigationView;
    HomeViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // UI bileşenlerinin bağlanması
        dateText = findViewById(R.id.textDate);
        welcomeUser = findViewById(R.id.welcomeUser);
        remainingTime = findViewById(R.id.remainingTime);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // LoginActivity'den gelen kullanıcı adı
        String userName = getIntent().getStringExtra("USER_NAME");
        if (userName != null) {
            welcomeUser.setText("Welcome, " + userName + "!");
        } else {
            welcomeUser.setText("Welcome!");
        }

        // Tarih ve saatin gösterimi
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault());
        String currentDateTime = sdf.format(new Date());
        dateText.setText(currentDateTime);

        // ViewModel oluşturulması
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        // Kullanıcı email bilgisini intent veya SharedPreferences'tan almalısın
        String userEmail = "user@example.com";
        viewModel.loadUser(userEmail);

        // Kullanıcı verisini gözlemleme
        viewModel.userLiveData.observe(this, user -> {
            if (user != null) {
                welcomeUser.setText("Welcome, " + user.userName + "!");
            } else {
                welcomeUser.setText("Welcome!");
            }
        });
        FloatingActionButton fabHome = findViewById(R.id.fab_home);

        fabHome.setOnClickListener(homeView -> {
            // Zaten Home'da iseniz isterseniz refresh yapabilirsin veya başka ekranda olursan HomeActivity'ye dönecek şekilde kullanabilirsin.
            // Örneğin:
             Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
             startActivity(intent);

            // Şimdilik refresh (veya Toast):

            Toast.makeText(this, "You are already on the Home Page!", Toast.LENGTH_SHORT).show();
        });


        // Bottom navigation ayarı
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_medications) {
                    Intent medIntent = new Intent(HomeActivity.this, MedicationsActivity.class);
                    medIntent.putExtra("USER_EMAIL", userEmail);
                    startActivity(medIntent);
                    return true;
                } else if (id == R.id.nav_food) {
                    Toast.makeText(HomeActivity.this, "Food sayfası henüz aktif değil.", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (id == R.id.nav_interactions) {
                    Toast.makeText(HomeActivity.this, "Interactions sayfası henüz aktif değil.", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (id == R.id.nav_profile) {
                    Toast.makeText(HomeActivity.this, "Profile sayfası henüz aktif değil.", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }

        });

        // Şimdilik sabit ilaç süresi örneği
        remainingTime.setText("8h 21m");
    }
}
