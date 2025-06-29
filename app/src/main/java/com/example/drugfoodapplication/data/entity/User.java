package com.example.drugfoodapplication.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String userName;
    public String email;
    public String password;

    // Yeni eklenen alanlar
    public int age;
    public String disease;
    public boolean allergyBee;
    public boolean allergyPollen;
    public boolean allergyPeanut;
}
