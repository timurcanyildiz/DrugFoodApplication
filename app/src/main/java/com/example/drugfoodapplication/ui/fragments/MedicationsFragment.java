package com.example.drugfoodapplication.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.drugfoodapplication.R;
import com.example.drugfoodapplication.data.entity.Medication;
import com.example.drugfoodapplication.ui.adapter.MedicationsAdapter;

import com.example.drugfoodapplication.ui.dialog.MedicationAddDialogFragment;
import com.example.drugfoodapplication.ui.viewmodel.MedicationsViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;

public class MedicationsFragment extends Fragment implements MedicationAddDialogFragment.MedicationAddListener {


    private static final String ARG_USER_EMAIL = "user_email";
    private String userEmail;
    private MedicationsViewModel medicationViewModel;
    private MedicationsAdapter adapter;

    public static MedicationsFragment newInstance(String userEmail) {
        MedicationsFragment fragment = new MedicationsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USER_EMAIL, userEmail);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userEmail = getArguments().getString(ARG_USER_EMAIL);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medications, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewMedications);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MedicationsAdapter(new ArrayList<>(), new MedicationsAdapter.OnMedicationActionListener() {
            @Override
            public void onEdit(Medication medication) {
                // Düzenleme dialogu edit modunda açılıyor
                MedicationAddDialogFragment dialog = new MedicationAddDialogFragment(userEmail, medication);
                dialog.setTargetFragment(MedicationsFragment.this, 1);
                dialog.show(getParentFragmentManager(), "edit_med_dialog");
            }

            @Override
            public void onDelete(Medication medication) {
                medicationViewModel.deleteMedication(medication);
                Toast.makeText(getContext(), "İlaç silindi: " + medication.name, Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(adapter);

        medicationViewModel = new ViewModelProvider(this).get(MedicationsViewModel.class);
        medicationViewModel.getMedicationsForUser(userEmail).observe(getViewLifecycleOwner(), medications -> {
            adapter.setMedicationList(medications);
        });

        FloatingActionButton fabAdd = view.findViewById(R.id.fabAddMedication);
        fabAdd.setOnClickListener(v -> {
            MedicationAddDialogFragment dialog = new MedicationAddDialogFragment(userEmail);
            dialog.setTargetFragment(this, 1);
            dialog.show(getParentFragmentManager(), "add_med_dialog");
        });

        return view;
    }
    @Override
    public void onMedicationAdded(Medication medication) {
        medicationViewModel.insertMedication(medication);
        Toast.makeText(getContext(), "İlaç eklendi!", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onMedicationEdited(Medication medication) {
        medicationViewModel.updateMedication(medication);
        Toast.makeText(getContext(), "İlaç güncellendi!", Toast.LENGTH_SHORT).show();
    }
}
