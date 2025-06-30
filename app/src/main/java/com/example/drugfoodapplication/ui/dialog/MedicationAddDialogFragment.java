package com.example.drugfoodapplication.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.drugfoodapplication.R;
import com.example.drugfoodapplication.data.entity.Medication;

import java.util.*;

public class MedicationAddDialogFragment extends DialogFragment {

    public interface MedicationAddListener {
        void onMedicationAdded(Medication medication);
        void onMedicationEdited(Medication medication);

    }

    private MedicationAddListener listener;
    private String userEmail;
    private Medication editMedication; // NULL ise yeni ekleme, doluysa düzenleme

    // Kategori ve ilaç eşleştirme
    private final Map<String, List<String>> medicationMap = new HashMap<String, List<String>>() {{
        put("Asthma", Arrays.asList("Ventolin", "Flixotide"));
        put("Diabetes", Arrays.asList("Glucophage", "Insulatard"));
        put("Hypertension", Arrays.asList("Beloc", "Aprovel"));
        put("Allergy", Arrays.asList("Claritin", "Zyrtec"));
    }};
    // Etken madde eşleştirme
    private final Map<String, String> activeSubstanceMap = new HashMap<String, String>() {{
        put("Ventolin", "Salbutamol");
        put("Flixotide", "Fluticasone");
        put("Glucophage", "Metformin");
        put("Insulatard", "Insulin");
        put("Beloc", "Metoprolol");
        put("Aprovel", "Irbesartan");
        put("Claritin", "Loratadine");
        put("Zyrtec", "Cetirizine");
    }};

    public MedicationAddDialogFragment(String userEmail) {
        this.userEmail = userEmail;
        this.editMedication = null;
    }
    public MedicationAddDialogFragment(String userEmail, Medication editMedication) {
        this.userEmail = userEmail;
        this.editMedication = editMedication;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (getTargetFragment() instanceof MedicationAddListener) {
            listener = (MedicationAddListener) getTargetFragment();
        } else if (getParentFragment() instanceof MedicationAddListener) {
            listener = (MedicationAddListener) getParentFragment();
        } else if (context instanceof MedicationAddListener) {
            listener = (MedicationAddListener) context;
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        android.view.View view = inflater.inflate(R.layout.dialog_add_medication, null);

        Spinner spinnerCategory = view.findViewById(R.id.spinnerCategory);
        Spinner spinnerMedication = view.findViewById(R.id.spinnerMedication);
        TextView textActiveSubstance = view.findViewById(R.id.textActiveSubstance);
        Spinner spinnerDosage = view.findViewById(R.id.spinnerDosage);
        Switch switchReminder = view.findViewById(R.id.switchReminder);

        // Saat seçiciler (sadece görünür/kapalı)
        LinearLayout timePickersLayout = view.findViewById(R.id.layoutTimePickers);
        TimePicker timePicker1 = view.findViewById(R.id.timePicker1);
        TimePicker timePicker2 = view.findViewById(R.id.timePicker2);
        TimePicker timePicker3 = view.findViewById(R.id.timePicker3);

        // 1- Kategori spinner
        List<String> categories = new ArrayList<>(medicationMap.keySet());
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, categories);
        spinnerCategory.setAdapter(categoryAdapter);

        // 2- İlaç spinner (kategoriye bağlı)
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view1, int position, long id) {
                String selectedCategory = categories.get(position);
                List<String> meds = medicationMap.get(selectedCategory);
                ArrayAdapter<String> medicationAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, meds);
                spinnerMedication.setAdapter(medicationAdapter);
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        // 3- Etken madde otomatik göster
        spinnerMedication.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view12, int position, long id) {
                String selectedMed = (String) spinnerMedication.getSelectedItem();
                String substance = activeSubstanceMap.getOrDefault(selectedMed, "");
                textActiveSubstance.setText(substance);
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        // 4- Dozaj seçimi
        String[] dosages = {"Günde 1", "Sabah-Akşam", "Sabah-Öğle-Akşam"};
        ArrayAdapter<String> dosageAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, dosages);
        spinnerDosage.setAdapter(dosageAdapter);

        // 5- Dozaja göre saat seçici yönetimi
        // Kullanıcı dozaj seçince kaç TimePicker görünecek?
        spinnerDosage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view13, int position, long id) {
                // Tümünü kapat
                timePicker1.setVisibility(View.GONE);
                timePicker2.setVisibility(View.GONE);
                timePicker3.setVisibility(View.GONE);
                if (position == 0) { // Günde 1
                    timePicker1.setVisibility(View.VISIBLE);
                } else if (position == 1) { // Sabah-Akşam
                    timePicker1.setVisibility(View.VISIBLE);
                    timePicker2.setVisibility(View.VISIBLE);
                } else if (position == 2) { // Sabah-Öğle-Akşam
                    timePicker1.setVisibility(View.VISIBLE);
                    timePicker2.setVisibility(View.VISIBLE);
                    timePicker3.setVisibility(View.VISIBLE);
                }
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        // 6- Hatırlatıcı switch: Kapalıysa timepicker'lar kapalı
        switchReminder.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                timePickersLayout.setVisibility(View.VISIBLE);
            } else {
                timePickersLayout.setVisibility(View.GONE);
            }
        });

        // Başta timepicker'lar kapalı
        timePickersLayout.setVisibility(View.GONE);

        timePicker1.setIs24HourView(true);
        timePicker2.setIs24HourView(true);
        timePicker3.setIs24HourView(true);
        if (editMedication != null) {
            int categoryIndex = categories.indexOf(editMedication.category);
            if (categoryIndex >= 0) spinnerCategory.setSelection(categoryIndex);

            // İlaç spinner doldurulduktan sonra ilacı set et
            spinnerCategory.post(() -> {
                List<String> meds = medicationMap.get(editMedication.category);
                if (meds != null) {
                    ArrayAdapter<String> medicationAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, meds);
                    spinnerMedication.setAdapter(medicationAdapter);
                    int medIndex = meds.indexOf(editMedication.name);
                    if (medIndex >= 0) spinnerMedication.setSelection(medIndex);
                }
            });

            textActiveSubstance.setText(editMedication.activeSubstance);

            int dosageIndex = Arrays.asList(dosages).indexOf(editMedication.dosageType);
            if (dosageIndex >= 0) spinnerDosage.setSelection(dosageIndex);

            switchReminder.setChecked(editMedication.hasReminder);

            if (editMedication.hasReminder && !TextUtils.isEmpty(editMedication.doseTimes)) {
                String[] times = editMedication.doseTimes.split(",");
                if (times.length > 0) setTimeToPicker(timePicker1, times[0]);
                if (times.length > 1) setTimeToPicker(timePicker2, times[1]);
                if (times.length > 2) setTimeToPicker(timePicker3, times[2]);
                timePickersLayout.setVisibility(View.VISIBLE);
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(editMedication == null ? "Add Medication" : "Edit Medication")
                .setView(view)
                .setPositiveButton(editMedication == null ? "Add" : "Update", (dialog, which) -> {
                    boolean isEdit = (editMedication != null);
                    Medication medication = isEdit ? editMedication : new Medication();
                    medication.userEmail = userEmail;
                    medication.category = (String) spinnerCategory.getSelectedItem();
                    medication.name = (String) spinnerMedication.getSelectedItem();
                    medication.activeSubstance = textActiveSubstance.getText().toString();
                    medication.dosageType = (String) spinnerDosage.getSelectedItem();
                    medication.hasReminder = switchReminder.isChecked();

                    // Saatleri ayarla
                    if (medication.hasReminder) {
                        List<String> times = new ArrayList<>();
                        if (timePicker1.getVisibility() == View.VISIBLE) {
                            times.add(String.format("%02d:%02d", timePicker1.getHour(), timePicker1.getMinute()));

                        }
                        if (timePicker2.getVisibility() == View.VISIBLE) {
                            times.add(String.format("%02d:%02d", timePicker2.getHour(), timePicker2.getMinute()));


                        }
                        if (timePicker3.getVisibility() == View.VISIBLE) {
                            times.add(String.format("%02d:%02d", timePicker3.getHour(), timePicker3.getMinute()));


                        }
                        medication.doseTimes = TextUtils.join(",", times); // örn: "08:00,20:00"
                    } else {
                        medication.doseTimes = "";
                    }

                    if (!isEdit) {
                        if (listener != null) listener.onMedicationAdded(medication);
                    } else {
                        if (listener != null) listener.onMedicationEdited(medication);
                    }
                })
                .setNegativeButton("Cancel", null);

        return builder.create();
    }
    private void setTimeToPicker(TimePicker picker, String time) {
        try {
            String[] parts = time.split(":");
            picker.setHour(Integer.parseInt(parts[0]));
            picker.setMinute(Integer.parseInt(parts[1]));
        } catch (Exception e) {
            picker.setHour(8);
            picker.setMinute(0);
        }
    }
}