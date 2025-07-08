// Food.java
package com.example.drugfoodapplication.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "foods")
public class Food {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private String category;
    private String description;
    private String portion;
    private int calories;
    private String userEmail;
    private boolean isFavorite;
    private long timestamp;

    public Food() {
        this.timestamp = System.currentTimeMillis();
    }

    public Food(String name, String category, String description, String portion, int calories, String userEmail, boolean isFavorite) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.portion = portion;
        this.calories = calories;
        this.userEmail = userEmail;
        this.isFavorite = isFavorite;
        this.timestamp = System.currentTimeMillis();
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPortion() {
        return portion;
    }

    public void setPortion(String portion) {
        this.portion = portion;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Food{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                ", portion='" + portion + '\'' +
                ", calories=" + calories +
                ", userEmail='" + userEmail + '\'' +
                ", isFavorite=" + isFavorite +
                ", timestamp=" + timestamp +
                '}';
    }
}