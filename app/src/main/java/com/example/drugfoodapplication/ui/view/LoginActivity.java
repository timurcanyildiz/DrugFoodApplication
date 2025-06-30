package com.example.drugfoodapplication.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.drugfoodapplication.R;
import com.example.drugfoodapplication.data.database.AppDatabase;
import com.example.drugfoodapplication.data.entity.User;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin, buttonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonRegister = findViewById(R.id.buttonRegister);

        buttonLogin.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Room veritabanından kullanıcıyı çek
            new Thread(() -> {
                User user = AppDatabase.getInstance(this).userDao().getUserByEmail(email);
                runOnUiThread(() -> {
                    if (user == null) {
                        Toast.makeText(this, "User not found!", Toast.LENGTH_SHORT).show();
                    } else if (!user.password.equals(password)) {
                        Toast.makeText(this, "Incorrect password!", Toast.LENGTH_SHORT).show();
                    } else {
                        // Başarılı giriş! Ana ekrana geçiş
                        Intent intent = new Intent(this, HomeActivity.class);
                        intent.putExtra("USER_EMAIL", email);
                        startActivity(intent);
                        finish();
                    }
                });
            }).start();
        });

        buttonRegister.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}
