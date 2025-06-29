package com.example.drugfoodapplication.ui.adapter;

import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.drugfoodapplication.R;
import com.example.drugfoodapplication.data.entity.Medication;
import java.util.List;

// Listener interface ile Activity/Fragment'e geri bildirim sağlanır
public class MedicationsAdapter extends RecyclerView.Adapter<MedicationsAdapter.MedicationViewHolder> {

    private List<Medication> medicationList;
    private OnMedicationActionListener listener;

    public interface OnMedicationActionListener {
        void onDelete(Medication medication);
        void onUpdate(Medication medication);
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
        holder.textName.setText(medication.name);
        holder.textCategory.setText(medication.category);
        holder.textSubstance.setText(medication.activeSubstance);
        holder.textDosage.setText(medication.dosageType + " (" + medication.doseTimes + ")");
        holder.textReminder.setText(medication.hasReminder ? "Reminder ON" : "Reminder OFF");

        holder.itemView.setOnLongClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            MenuInflater inflater = popupMenu.getMenuInflater();
            inflater.inflate(R.menu.menu_medication_action, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.action_delete) {
                    listener.onDelete(medication);
                    return true;
                } else if (item.getItemId() == R.id.action_update) {
                    listener.onUpdate(medication);
                    return true;
                }
                return false;
            });
            popupMenu.show();
            return true;
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
        TextView textName, textCategory, textSubstance, textDosage, textReminder;
        MedicationViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textMedicationName);
            textCategory = itemView.findViewById(R.id.textCategory);
            textSubstance = itemView.findViewById(R.id.textSubstance);
            textDosage = itemView.findViewById(R.id.textDosage);
            textReminder = itemView.findViewById(R.id.textReminder);
        }
    }
}
