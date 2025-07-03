package com.example.drugfoodapplication.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import com.example.drugfoodapplication.data.entity.Food;

import java.util.List;

@Dao
public interface FoodDao {
    @Insert
    void insertFood(Food food);

    @Update
    void updateFood(Food food);

    @Delete
    void deleteFood(Food food);

    @Query("SELECT * FROM foods WHERE userEmail = :userEmail")
    LiveData<List<Food>> getFoodsForUser(String userEmail);

    @Query("SELECT * FROM foods WHERE userEmail = :userEmail AND favorite = 1")
    LiveData<List<Food>> getFavoriteFoodsForUser(String userEmail);
}
