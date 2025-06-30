package com.example.drugfoodapplication.ui.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.drugfoodapplication.data.dao.MedicationDao;
import com.example.drugfoodapplication.data.database.AppDatabase;
import com.example.drugfoodapplication.data.entity.Medication;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

// İlaç ekleme, silme, güncelleme işlemlerinin ViewModel'i
public class MedicationsViewModel extends AndroidViewModel {

    private final MedicationDao medicationDao;
    private final Executor executor = Executors.newSingleThreadExecutor();

    public MedicationsViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(application);
        medicationDao = db.medicationDao();
    }

    public LiveData<List<Medication>> getMedicationsForUser(String userEmail) {
        return medicationDao.getMedicationsForUser(userEmail);
    }

    public void insertMedication(Medication medication) {
        executor.execute(() -> medicationDao.insertMedication(medication));
    }

    public void deleteMedication(Medication medication) {
        executor.execute(() -> medicationDao.deleteMedication(medication));
    }

    public void updateMedication(Medication medication) {
        executor.execute(() -> medicationDao.updateMedication(medication));
    }
}
