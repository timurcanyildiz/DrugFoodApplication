package com.example.drugfoodapplication.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import com.example.drugfoodapplication.data.entity.Interaction;

import java.util.List;

@Dao
public interface InteractionDao {
    @Insert
    void insertInteraction(Interaction interaction);

    @Query("SELECT * FROM interactions")
    LiveData<List<Interaction>> getAllInteractions();

    @Query("SELECT * FROM interactions WHERE drugName = :drugName AND foodName = :foodName LIMIT 1")
    Interaction getInteraction(String drugName, String foodName);

    @Delete
    void deleteInteraction(Interaction interaction);

    @Update
    void updateInteraction(Interaction interaction);
}
