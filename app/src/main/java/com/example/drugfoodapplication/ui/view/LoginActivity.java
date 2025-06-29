package com.example.drugfoodapplication.ui.view;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

import com.example.drugfoodapplication.R;

public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    Button loginBtn, registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // UI elemanları bağlanıyor
        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
        loginBtn = findViewById(R.id.buttonLogin);
        registerBtn = findViewById(R.id.buttonRegister);

        // Giriş butonu listener ekleme
        loginBtn.setOnClickListener(v -> {
            String userEmail = email.getText().toString();
            String userPassword = password.getText().toString();

            if (userEmail.isEmpty() || userPassword.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else {
                // Burada giriş doğrulama işlemleri yapılacak (Firebase vs.)
                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
            }
        });

        // Kayıt butonu listener ekleme
        registerBtn.setOnClickListener(v -> {
            // Kayıt ekranına yönlendirme yapılacak
            Toast.makeText(this, "Redirecting to register...", Toast.LENGTH_SHORT).show();
        });
    }
}
