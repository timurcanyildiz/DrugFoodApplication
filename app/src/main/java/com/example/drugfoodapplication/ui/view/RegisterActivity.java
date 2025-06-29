package com.example.drugfoodapplication.ui.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.drugfoodapplication.R;
import com.example.drugfoodapplication.ui.viewmodel.RegisterViewModel;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextName, editTextEmail, editTextPassword, editTextAge;
    private Spinner spinnerDisease;
    private CheckBox checkBee, checkPollen, checkPeanut;
    private Button buttonRegister;
    private RegisterViewModel registerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Giriş alanları ve bileşenleri bağlama
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextAge = findViewById(R.id.editTextAge);
        spinnerDisease = findViewById(R.id.spinnerDisease);
        checkBee = findViewById(R.id.checkBee);
        checkPollen = findViewById(R.id.checkPollen);
        checkPeanut = findViewById(R.id.checkPeanut);
        buttonRegister = findViewById(R.id.buttonRegister);

        // Spinner için adapter
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.disease_list,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDisease.setAdapter(adapter);

        // ViewModel bağlama
        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        // Kayıt butonuna tıklanma olayı
        buttonRegister.setOnClickListener(registerView -> {
            String name = editTextName.getText().toString().trim();
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            String ageText = editTextAge.getText().toString().trim();
            String disease = spinnerDisease.getSelectedItem().toString();

            // Giriş doğrulama
            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(ageText)) {
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                return;
            }

            int age;
            try {
                age = Integer.parseInt(ageText);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid age entered", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean hasBeeAllergy = checkBee.isChecked();
            boolean hasPollenAllergy = checkPollen.isChecked();
            boolean hasPeanutAllergy = checkPeanut.isChecked();

            registerViewModel.registerUser(
                    name,
                    email,
                    password,
                    age,
                    disease,
                    hasBeeAllergy,
                    hasPollenAllergy,
                    hasPeanutAllergy
            );

            Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
