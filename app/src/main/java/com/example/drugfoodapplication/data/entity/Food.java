package com.example.drugfoodapplication.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "foods")
public class Food {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String userEmail;
    public String name;
    public String category;
    public String description;
    public int calories;
    public boolean favorite;

    //Parametresiz constructor
    public Food() {}

    // Kolay kullanım için parametreli constructor
    public Food(String name, String category, String description, int calories, boolean favorite) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.calories = calories;
        this.favorite = favorite;
    }

    public Food(String name, String category, String description, int calories, boolean favorite, String userEmail) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.calories = calories;
        this.favorite = favorite;
        this.userEmail = userEmail;
    }

}
