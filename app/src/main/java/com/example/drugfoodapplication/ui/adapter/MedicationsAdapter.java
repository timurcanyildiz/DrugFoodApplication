package com.example.drugfoodapplication.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.drugfoodapplication.R;
import com.example.drugfoodapplication.data.entity.Medication;
import java.util.List;

public class MedicationsAdapter extends RecyclerView.Adapter<MedicationsAdapter.MedicationViewHolder> {

    private List<Medication> medicationList;
    private OnMedicationActionListener listener;

    public interface OnMedicationActionListener {
        void onEdit(Medication medication);
        void onDelete(Medication medication);
    }

    public MedicationsAdapter(List<Medication> medicationList, OnMedicationActionListener listener) {
        this.medicationList = medicationList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MedicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_medication, parent, false);
        return new MedicationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicationViewHolder holder, int position) {
        Medication medication = medicationList.get(position);
        holder.textMedicationName.setText(medication.name);
        holder.textCategory.setText("Category: " + medication.category);
        holder.textActiveSubstance.setText("Active: " + medication.activeSubstance);
        holder.textDosage.setText("Dosage: " + medication.dosageType);

        // Edit butonuna tıklama
        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(medication);
        });

        // Delete butonuna tıklama
        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(medication);
        });
    }

    @Override
    public int getItemCount() {
        return medicationList.size();
    }

    public void setMedicationList(List<Medication> medications) {
        this.medicationList = medications;
        notifyDataSetChanged();
    }

    static class MedicationViewHolder extends RecyclerView.ViewHolder {
        TextView textMedicationName, textCategory, textActiveSubstance, textDosage;
        Button btnEdit, btnDelete;

        MedicationViewHolder(@NonNull View itemView) {
            super(itemView);
            textMedicationName = itemView.findViewById(R.id.textMedicationName);
            textCategory = itemView.findViewById(R.id.textCategory);
            textActiveSubstance = itemView.findViewById(R.id.textActiveSubstance);
            textDosage = itemView.findViewById(R.id.textDosage);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
