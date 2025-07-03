package com.example.drugfoodapplication.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "interactions")
public class Interaction {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String drugName;
    public String foodName;
    public String riskLevel; // "YÜKSEK", "ORTA", "DÜŞÜK"
    public String description;
    public String riskIcon; // "🔴", "🟡", "🟢"
    public String recommendation; // "Kaçının", "Dikkatli olun" vb.

    public Interaction(String drugName, String foodName, String riskLevel, String description, String riskIcon, String recommendation) {
        this.drugName = drugName;
        this.foodName = foodName;
        this.riskLevel = riskLevel;
        this.description = description;
        this.riskIcon = riskIcon;
        this.recommendation = recommendation;
    }
}
