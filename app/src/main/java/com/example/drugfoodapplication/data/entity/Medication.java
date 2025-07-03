package com.example.drugfoodapplication.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "medications")
public class Medication {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String userEmail;        // Giriş yapan kullanıcının emaili ile eşleşecek
    public String category;
    public String name;
    public String activeSubstance;
    public String dosageType; // Örn: "Günde 1", "Sabah-Akşam", "Sabah-Öğle-Akşam"
    public String doseTimes; //
    public boolean hasReminder;
}
