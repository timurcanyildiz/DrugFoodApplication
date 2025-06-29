package com.example.drugfoodapplication.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.drugfoodapplication.data.entity.Medication;

import java.util.List;

@Dao
public interface MedicationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMedication(Medication medication);

    @Query("SELECT * FROM medications WHERE userEmail = :userEmail")
    LiveData<List<Medication>> getMedicationsForUser(String userEmail);

    // Geri sayım için kullanılacak, örneğin en yakın dozajı bulmak vs.
    @Delete
    void deleteMedication(Medication medication);

    @Update
    void updateMedication(Medication medication);
}
