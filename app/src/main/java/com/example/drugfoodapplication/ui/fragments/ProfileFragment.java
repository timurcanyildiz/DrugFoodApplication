package com.example.drugfoodapplication.ui.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.drugfoodapplication.R;
import com.example.drugfoodapplication.data.entity.User;
import com.example.drugfoodapplication.ui.view.HomeActivity;
import com.example.drugfoodapplication.ui.view.LoginActivity;
import com.example.drugfoodapplication.ui.viewmodel.ProfileViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProfileFragment extends Fragment {

    private static final String ARG_USER_EMAIL = "user_email";
    private String userEmail;

    private TextView tvUserName, tvUserEmail, tvAge, tvWeight, tvHeight, tvGender, tvAllergies, tvChronicDiseases;
    private ImageButton btnEditWeight, btnEditHeight, btnEditGender, btnEditAllergies, btnEditDiseases;
    private Button btnChangePassword, btnLogout;
    private Switch switchDarkMode;

    private ProfileViewModel viewModel;
    private User currentUser;

    // Multi-choice için sabitler
    private final String[] diseasesArray = {"Asthma", "Diabetes", "Hypertension", "COPD"};
    private final String[] allergiesArray = {"Bee Allergy", "Pollen Allergy", "Peanut Allergy", "Dust Allergy"};
    private boolean[] selectedDiseases = new boolean[diseasesArray.length];
    private boolean[] selectedAllergies = new boolean[allergiesArray.length];

    public static ProfileFragment newInstance(String userEmail) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString("user_email", userEmail);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userEmail = getArguments().getString("user_email");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        if (userEmail == null || userEmail.isEmpty()) {
            Toast.makeText(getContext(), "Hata: Kullanıcı bilgisi bulunamadı!", Toast.LENGTH_SHORT).show();
            // Giriş ekranına dön
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            requireActivity().finish();
            return null;
        }


        // View'ları tanımla
        tvUserName = view.findViewById(R.id.tv_user_name);
        tvUserEmail = view.findViewById(R.id.tv_user_email);
        tvAge = view.findViewById(R.id.tv_age);
        tvWeight = view.findViewById(R.id.tv_weight);
        tvHeight = view.findViewById(R.id.tv_height);
        tvGender = view.findViewById(R.id.tv_gender);
        tvAllergies = view.findViewById(R.id.tv_allergies);
        tvChronicDiseases = view.findViewById(R.id.tv_chronic_diseases);
        btnEditWeight = view.findViewById(R.id.btn_edit_weight);
        btnEditHeight = view.findViewById(R.id.btn_edit_height);
        btnEditGender = view.findViewById(R.id.btn_edit_gender);
        btnEditAllergies = view.findViewById(R.id.btn_edit_allergies);
        btnEditDiseases = view.findViewById(R.id.btn_edit_diseases);
        btnChangePassword = view.findViewById(R.id.btn_change_password); // layoutunda olmalı
        btnLogout = view.findViewById(R.id.btn_logout);
        switchDarkMode = view.findViewById(R.id.switch_dark_mode); // layoutunda olmalı

        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        // Kullanıcıyı gözlemle
        viewModel.getUserByEmail(userEmail).observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                currentUser = user;
                if (user != null) {
                    tvUserName.setText(user.name);
                    tvUserEmail.setText(user.email);
                    tvAge.setText(user.age > 0 ? user.age + " yaş" : "Eklenmedi");
                    tvWeight.setText(user.weight != null ? user.weight + " kg" : "Eklenmedi");
                    tvHeight.setText(user.height != null ? user.height + " cm" : "Eklenmedi");
                    tvGender.setText(user.gender != null ? user.gender : "Eklenmedi");
                    tvAllergies.setText(user.allergies != null && !user.allergies.isEmpty() ? user.allergies : "Eklenmedi");
                    tvChronicDiseases.setText(user.diseases != null && !user.diseases.isEmpty() ? user.diseases : "Eklenmedi");
                }
            }
        });

        // Kilo düzenleme
        btnEditWeight.setOnClickListener(v -> showEditNumberDialog("Kilo", "kg", currentUser.weight, (value) -> {
            currentUser.weight = value;
            viewModel.updateUser(currentUser);
        }));

        // Boy düzenleme
        btnEditHeight.setOnClickListener(v -> showEditNumberDialog("Boy", "cm", currentUser.height, (value) -> {
            currentUser.height = value;
            viewModel.updateUser(currentUser);
        }));

        // Cinsiyet düzenleme
        btnEditGender.setOnClickListener(v -> showEditGenderDialog());

        // --- Çoklu seçim ile hastalık düzenleme ---
        btnEditDiseases.setOnClickListener(v -> showMultiChoiceDialog(
                "Kronik Hastalıkları Düzenle", diseasesArray, currentUser.diseases, selectedDiseases,
                values -> {
                    currentUser.diseases = TextUtils.join(",", values);
                    viewModel.updateUser(currentUser);
                }
        ));

        // --- Çoklu seçim ile alerji düzenleme ---
        btnEditAllergies.setOnClickListener(v -> showMultiChoiceDialog(
                "Alerjileri Düzenle", allergiesArray, currentUser.allergies, selectedAllergies,
                values -> {
                    currentUser.allergies = TextUtils.join(",", values);
                    viewModel.updateUser(currentUser);
                }
        ));

        // Şifre değiştir
        btnChangePassword.setOnClickListener(v -> showChangePasswordDialog());

        // Dark Mode switch'i
        switchDarkMode.setChecked(isNightModeActive());
        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            AppCompatDelegate.setDefaultNightMode(isChecked
                    ? AppCompatDelegate.MODE_NIGHT_YES
                    : AppCompatDelegate.MODE_NIGHT_NO);
        });


        // Çıkış yap
        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            requireActivity().finish();
        });


        return view;
    }

    // Sayısal alanları düzenle
    private void showEditNumberDialog(String title, String unit, Integer current, ValueUpdateCallback callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title + " Güncelle");

        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        if (current != null) input.setText(String.valueOf(current));
        input.setHint(unit);
        builder.setView(input);

        builder.setPositiveButton("Kaydet", (dialog, which) -> {
            try {
                int value = Integer.parseInt(input.getText().toString());
                callback.onUpdate(value);
            } catch (Exception e) {
                Toast.makeText(getContext(), "Geçersiz değer!", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("İptal", null);
        builder.show();
    }

    // Cinsiyet seçimi
    private void showEditGenderDialog() {
        String[] genders = {"Erkek", "Kadın", "Diğer"};
        int checkedItem = 0;
        if (currentUser.gender != null) {
            for (int i = 0; i < genders.length; i++) {
                if (genders[i].equalsIgnoreCase(currentUser.gender)) {
                    checkedItem = i;
                    break;
                }
            }
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Cinsiyet Seç");
        builder.setSingleChoiceItems(genders, checkedItem, null);
        builder.setPositiveButton("Kaydet", (dialog, which) -> {
            int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
            currentUser.gender = genders[selectedPosition];
            viewModel.updateUser(currentUser);
        });
        builder.setNegativeButton("İptal", null);
        builder.show();
    }

    // --- Çoklu seçim dialogu: Hastalık veya alerji için ---
    private void showMultiChoiceDialog(String title, String[] items, String currentValues, boolean[] selected, MultiChoiceCallback callback) {
        // Mevcut seçimleri işaretle
        Arrays.fill(selected, false);
        if (currentValues != null && !currentValues.isEmpty()) {
            String[] saved = currentValues.split(",");
            for (int i = 0; i < items.length; i++) {
                for (String s : saved) {
                    if (items[i].trim().equalsIgnoreCase(s.trim())) {
                        selected[i] = true;
                    }
                }
            }
        }
        new AlertDialog.Builder(getContext())
                .setTitle(title)
                .setMultiChoiceItems(items, selected, (dialog, which, isChecked) -> selected[which] = isChecked)
                .setPositiveButton("Kaydet", (dialog, which) -> {
                    List<String> chosen = new ArrayList<>();
                    for (int i = 0; i < items.length; i++) {
                        if (selected[i]) chosen.add(items[i]);
                    }
                    callback.onMultiChoice(chosen);
                })
                .setNegativeButton("İptal", null)
                .show();
    }

    // Şifre değiştir dialogu
    private void showChangePasswordDialog() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.dialog_change_password, null);
        EditText etOldPassword = dialogView.findViewById(R.id.et_old_password);
        EditText etNewPassword = dialogView.findViewById(R.id.et_new_password);
        EditText etNewPasswordRepeat = dialogView.findViewById(R.id.et_new_password_repeat);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Şifre Değiştir");
        builder.setView(dialogView);
        builder.setPositiveButton("Kaydet", (dialog, which) -> {
            String oldPass = etOldPassword.getText().toString();
            String newPass = etNewPassword.getText().toString();
            String newPass2 = etNewPasswordRepeat.getText().toString();

            if (!currentUser.password.equals(oldPass)) {
                Toast.makeText(getContext(), "Eski şifre yanlış!", Toast.LENGTH_SHORT).show();
            } else if (!newPass.equals(newPass2)) {
                Toast.makeText(getContext(), "Yeni şifreler uyuşmuyor!", Toast.LENGTH_SHORT).show();
            } else if (newPass.length() < 4) {
                Toast.makeText(getContext(), "Şifre en az 4 karakter olmalı!", Toast.LENGTH_SHORT).show();
            } else {
                currentUser.password = newPass;
                viewModel.updateUser(currentUser);
                Toast.makeText(getContext(), "Şifre güncellendi!", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("İptal", null);
        builder.show();
    }

    // Gece modu kontrolü
    private boolean isNightModeActive() {
        int nightModeFlags = getResources().getConfiguration().uiMode & android.content.res.Configuration.UI_MODE_NIGHT_MASK;
        return nightModeFlags == android.content.res.Configuration.UI_MODE_NIGHT_YES;
    }

    // Callbackler
    interface ValueUpdateCallback { void onUpdate(int value); }
    interface MultiChoiceCallback { void onMultiChoice(List<String> values); }
}
