package com.example.drugfoodapplication.ui.view;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.drugfoodapplication.R;
import com.example.drugfoodapplication.data.entity.Medication;
import com.example.drugfoodapplication.ui.adapter.MedicationsAdapter;
import com.example.drugfoodapplication.ui.viewmodel.MedicationsViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.*;

public class MedicationsActivity extends AppCompatActivity {

    private FloatingActionButton fabAddMedication;
    private RecyclerView recyclerView;
    private MedicationsAdapter adapter;
    private MedicationsViewModel viewModel;
    private String userEmail;

    // Kategori-ilaç ve ilaç-etken madde listeleri (demo amaçlı)
    private final Map<String, List<String>> categoryMedications = new HashMap<String, List<String>>() {{
        put("Painkiller", Arrays.asList("Parol", "Minoset"));
        put("Diabetes", Arrays.asList("Glucophage", "Janumet"));
        put("Cancer", Arrays.asList("Xeloda", "Nexavar"));
    }};
    private final Map<String, String> medicationActiveSubstance = new HashMap<String, String>() {{
        put("Parol", "Paracetamol");
        put("Minoset", "Paracetamol");
        put("Glucophage", "Metformin");
        put("Janumet", "Sitagliptin + Metformin");
        put("Xeloda", "Capecitabine");
        put("Nexavar", "Sorafenib");
    }};
    private final List<String> dosageTypes = Arrays.asList("Günde 1", "Sabah-Akşam", "Sabah-Öğle-Akşam");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medications);

        // Kullanıcının emailini intent ile al
        userEmail = getIntent().getStringExtra("USER_EMAIL");

        recyclerView = findViewById(R.id.recyclerViewMedications);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MedicationsAdapter(new ArrayList<>(), new MedicationsAdapter.OnMedicationActionListener() {
            @Override
            public void onDelete(Medication medication) {
                viewModel.deleteMedication(medication);
            }
            @Override
            public void onEdit(Medication medication) {
                showAddOrUpdateDialog(medication);
            }
        });
        recyclerView.setAdapter(adapter);
        fabAddMedication = findViewById(R.id.fabAddMedication);
        fabAddMedication.setOnClickListener(view -> showAddOrUpdateDialog(null));

        viewModel = new ViewModelProvider(this).get(MedicationsViewModel.class);

        // Listeyi gözlemle
        viewModel.getMedicationsForUser(userEmail).observe(this, medications -> {
            // Burada RecyclerView adapterini güncelle
            adapter.setMedicationList(medications);
        });


    }

    private void showAddOrUpdateDialog(@Nullable Medication medication) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(medication == null ? "Add Medication" : "Update Medication");

        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_add_medication, null);

        Spinner spinnerCategory = dialogView.findViewById(R.id.spinnerCategory);
        Spinner spinnerMedication = dialogView.findViewById(R.id.spinnerMedication);
        TextView textActiveSubstance = dialogView.findViewById(R.id.textActiveSubstance);
        Spinner spinnerDosage = dialogView.findViewById(R.id.spinnerDosage);
        LinearLayout layoutTimePickers = dialogView.findViewById(R.id.layoutTimePickers);
        Switch switchReminder = dialogView.findViewById(R.id.switchReminder);

        // Kategorileri spinner'a yükle
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, new ArrayList<>(categoryMedications.keySet()));
        spinnerCategory.setAdapter(categoryAdapter);

        // Dozaj tiplerini spinner'a yükle
        ArrayAdapter<String> dosageTypeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, dosageTypes);
        spinnerDosage.setAdapter(dosageTypeAdapter);

        // Kategori seçilince ilgili ilaçlar spinner'a yüklenir
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = (String) spinnerCategory.getSelectedItem();
                List<String> meds = categoryMedications.get(selectedCategory);
                ArrayAdapter<String> medicationAdapter = new ArrayAdapter<>(MedicationsActivity.this,
                        android.R.layout.simple_spinner_dropdown_item, meds);
                spinnerMedication.setAdapter(medicationAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // İlaç seçilince etken madde otomatik güncellenir
        spinnerMedication.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedMed = (String) spinnerMedication.getSelectedItem();
                String substance = medicationActiveSubstance.get(selectedMed);
                textActiveSubstance.setText("Active Substance: " + (substance != null ? substance : "---"));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Dozaj tipi seçilince uygun sayıda saat seçtireceğiz
        spinnerDosage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                layoutTimePickers.removeAllViews();
                int doseCount = 1; // Default
                String selectedDosageType = (String) spinnerDosage.getSelectedItem();
                if ("Günde 1".equals(selectedDosageType)) doseCount = 1;
                else if ("Sabah-Akşam".equals(selectedDosageType)) doseCount = 2;
                else if ("Sabah-Öğle-Akşam".equals(selectedDosageType)) doseCount = 3;

                for (int i = 0; i < doseCount; i++) {
                    Button btnTime = new Button(MedicationsActivity.this);
                    btnTime.setText("Select Time " + (i + 1));
                    btnTime.setTag("time_" + i);
                    final int index = i;

                    btnTime.setOnClickListener(v -> {
                        // Saat seçici aç
                        Calendar calendar = Calendar.getInstance();
                        int hour = calendar.get(Calendar.HOUR_OF_DAY);
                        int minute = calendar.get(Calendar.MINUTE);
                        TimePickerDialog timePickerDialog = new TimePickerDialog(MedicationsActivity.this,
                                (view1, hourOfDay, minute1) -> {
                                    String time = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute1);
                                    btnTime.setText(time);
                                    btnTime.setTag(time);
                                }, hour, minute, true);
                        timePickerDialog.show();
                    });
                    layoutTimePickers.addView(btnTime);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Eğer update işlemi ise mevcut ilaç bilgilerini yükle
        if (medication != null) {
            // Kategori ve ilaç seçimi
            spinnerCategory.setSelection(new ArrayList<>(categoryMedications.keySet()).indexOf(medication.category));
            // spinnerMedication'ın doğru şekilde dolması için ufak gecikme
            spinnerCategory.post(() -> {
                List<String> meds = categoryMedications.get(medication.category);
                ArrayAdapter<String> medicationAdapter = new ArrayAdapter<>(MedicationsActivity.this,
                        android.R.layout.simple_spinner_dropdown_item, meds);
                spinnerMedication.setAdapter(medicationAdapter);
                spinnerMedication.setSelection(meds.indexOf(medication.name));
            });
            textActiveSubstance.setText("Active Substance: " + medication.activeSubstance);
            spinnerDosage.setSelection(dosageTypes.indexOf(medication.dosageType));
            switchReminder.setChecked(medication.hasReminder);
            // Saatler
            spinnerDosage.post(() -> {
                String[] times = medication.doseTimes.split(",");
                for (int i = 0; i < layoutTimePickers.getChildCount() && i < times.length; i++) {
                    Button btn = (Button) layoutTimePickers.getChildAt(i);
                    btn.setText(times[i]);
                    btn.setTag(times[i]);
                }
            });
        }

        builder.setView(dialogView);
        builder.setPositiveButton(medication == null ? "Add" : "Update", (dialog, which) -> {
            String selectedCategory = (String) spinnerCategory.getSelectedItem();
            String selectedMedication = (String) spinnerMedication.getSelectedItem();
            String activeSubstance = medicationActiveSubstance.get(selectedMedication);
            String selectedDosageType = (String) spinnerDosage.getSelectedItem();

            // Saatleri topla
            List<String> times = new ArrayList<>();
            for (int i = 0; i < layoutTimePickers.getChildCount(); i++) {
                Button btn = (Button) layoutTimePickers.getChildAt(i);
                Object tag = btn.getTag();
                if (tag != null && tag.toString().contains(":")) {
                    times.add(tag.toString());
                } else {
                    times.add("08:00");
                }
            }
            String doseTimes = TextUtils.join(",", times);

            boolean reminder = switchReminder.isChecked();

            Medication newMedication = new Medication();
            if (medication != null) {
                newMedication.id = medication.id; // update için ID aktarılır
            }
            newMedication.userEmail = userEmail;
            newMedication.category = selectedCategory;
            newMedication.name = selectedMedication;
            newMedication.activeSubstance = activeSubstance;
            newMedication.dosageType = selectedDosageType;
            newMedication.doseTimes = doseTimes;
            newMedication.hasReminder = reminder;

            if (medication == null) {
                viewModel.insertMedication(newMedication);
            } else {
                viewModel.updateMedication(newMedication);
            }
        });


        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
}
