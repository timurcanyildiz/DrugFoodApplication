package com.example.drugfoodapplication.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.drugfoodapplication.R;
import com.example.drugfoodapplication.ui.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin, buttonRegister;
    private TextView textForgotPassword;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonRegister = findViewById(R.id.buttonRegister);
        textForgotPassword = findViewById(R.id.textForgotPassword);

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        // Login işlemi
        buttonLogin.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            loginViewModel.loginUser(email, password, user -> {
                if (user != null) {
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    intent.putExtra("USER_NAME", user.userName);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Incorrect email or password", Toast.LENGTH_SHORT).show();
                }
            });
        });

        // Register butonuna tıklandığında RegisterActivity'e geçiş
        buttonRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        // Forgot password tıklanınca (burada örnek olarak Toast ile uyarı)
        textForgotPassword.setOnClickListener(v -> {
            Toast.makeText(this, "Password reset feature coming soon!", Toast.LENGTH_SHORT).show();
            // Eğer yeni bir activity açmak istiyorsan:
            // startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
        });
    }
}
