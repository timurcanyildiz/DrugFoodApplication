package com.example.drugfoodapplication.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public String email;
    public String password;
    public int age;

    // Yeni alanlar!
    public String diseases; // Örnek: "Asthma,Diabetes"
    public String allergies; // Örnek: "Bee Allergy,Pollen Allergy"
}
