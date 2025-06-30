package com.example.drugfoodapplication.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.drugfoodapplication.R;
import com.example.drugfoodapplication.data.entity.User;
import com.example.drugfoodapplication.ui.viewmodel.RegisterViewModel;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextName, editTextEmail, editTextPassword, editTextAge;
    private Button buttonRegister, buttonSelectDiseases, buttonSelectAllergies;
    private TextView textSelectedDiseases, textSelectedAllergies;

    // Çoklu seçim için hastalık ve alerji dizileri
    private String[] diseasesArray = {"Asthma", "Diabetes", "Hypertension", "COPD"};
    private String[] allergiesArray = {"Bee Allergy", "Pollen Allergy", "Peanut Allergy", "Dust Allergy"};

    private boolean[] selectedDiseases, selectedAllergies;
    private List<String> chosenDiseases = new ArrayList<>();
    private List<String> chosenAllergies = new ArrayList<>();

    // ViewModel!
    private RegisterViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextAge = findViewById(R.id.editTextAge);
        buttonRegister = findViewById(R.id.buttonRegister);
        buttonSelectDiseases = findViewById(R.id.buttonSelectDiseases);
        buttonSelectAllergies = findViewById(R.id.buttonSelectAllergies);
        textSelectedDiseases = findViewById(R.id.textSelectedDiseases);
        textSelectedAllergies = findViewById(R.id.textSelectedAllergies);

        selectedDiseases = new boolean[diseasesArray.length];
        selectedAllergies = new boolean[allergiesArray.length];

        // ViewModel'ı bağla!
        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        buttonSelectDiseases.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
            builder.setTitle("Select Diseases");
            builder.setMultiChoiceItems(diseasesArray, selectedDiseases, (dialog, which, isChecked) -> {
                selectedDiseases[which] = isChecked;
            });
            builder.setPositiveButton("OK", (dialog, which) -> {
                chosenDiseases.clear();
                for (int i = 0; i < diseasesArray.length; i++) {
                    if (selectedDiseases[i]) {
                        chosenDiseases.add(diseasesArray[i]);
                    }
                }
                textSelectedDiseases.setText(chosenDiseases.isEmpty() ? "No diseases selected" : TextUtils.join(", ", chosenDiseases));
            });
            builder.setNegativeButton("Cancel", null);
            builder.show();
        });

        buttonSelectAllergies.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
            builder.setTitle("Select Allergies");
            builder.setMultiChoiceItems(allergiesArray, selectedAllergies, (dialog, which, isChecked) -> {
                selectedAllergies[which] = isChecked;
            });
            builder.setPositiveButton("OK", (dialog, which) -> {
                chosenAllergies.clear();
                for (int i = 0; i < allergiesArray.length; i++) {
                    if (selectedAllergies[i]) {
                        chosenAllergies.add(allergiesArray[i]);
                    }
                }
                textSelectedAllergies.setText(chosenAllergies.isEmpty() ? "No allergies selected" : TextUtils.join(", ", chosenAllergies));
            });
            builder.setNegativeButton("Cancel", null);
            builder.show();
        });

        buttonRegister.setOnClickListener(v -> {
            String name = editTextName.getText().toString().trim();
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            String ageText = editTextAge.getText().toString().trim();

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) ||
                    TextUtils.isEmpty(password) || TextUtils.isEmpty(ageText)) {
                Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
                return;
            }

            int age;
            try {
                age = Integer.parseInt(ageText);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid age!", Toast.LENGTH_SHORT).show();
                return;
            }

            String diseasesAsString = TextUtils.join(",", chosenDiseases);
            String allergiesAsString = TextUtils.join(",", chosenAllergies);

            User newUser = new User();
            newUser.name = name;
            newUser.email = email;
            newUser.password = password;
            newUser.age = age;
            newUser.diseases = diseasesAsString;
            newUser.allergies = allergiesAsString;

            // ViewModel üzerinden kaydet!
            viewModel.registerUser(newUser);
        });

        // ViewModel LiveData ile sonucu gözlemle
        viewModel.getRegistrationSuccess().observe(this, success -> {
            if (success != null && success) {
                Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, HomeActivity.class);
                intent.putExtra("USER_EMAIL", editTextEmail.getText().toString().trim());
                startActivity(intent);
                finish();
            }
        });

        viewModel.getErrorMessage().observe(this, msg -> {
            if (msg != null) {
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
