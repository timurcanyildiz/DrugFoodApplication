// FoodDao.java
package com.example.drugfoodapplication.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.drugfoodapplication.data.entity.Food;

import java.util.List;

@Dao
public interface FoodDao {

    @Insert
    void insert(Food food);

    @Update
    void update(Food food);

    @Delete
    void delete(Food food);

    @Query("SELECT * FROM foods WHERE userEmail = :userEmail ORDER BY timestamp DESC")
    List<Food> getFoodsByUser(String userEmail);

    @Query("SELECT * FROM foods WHERE userEmail = :userEmail AND category = :category ORDER BY timestamp DESC")
    List<Food> getFoodsByUserAndCategory(String userEmail, String category);

    @Query("SELECT * FROM foods WHERE userEmail = :userEmail AND isFavorite = 1 ORDER BY timestamp DESC")
    List<Food> getFavoriteFoodsByUser(String userEmail);

    @Query("SELECT SUM(calories) FROM foods WHERE userEmail = :userEmail")
    int getTotalCaloriesByUser(String userEmail);

    @Query("SELECT SUM(calories) FROM foods WHERE userEmail = :userEmail AND date(timestamp/1000, 'unixepoch') = date('now')")
    int getTodayCaloriesByUser(String userEmail);

    @Query("SELECT * FROM foods WHERE userEmail = :userEmail AND name LIKE '%' || :searchQuery || '%' ORDER BY timestamp DESC")
    List<Food> searchFoodsByUser(String userEmail, String searchQuery);

    @Query("DELETE FROM foods WHERE userEmail = :userEmail")
    void deleteAllFoodsByUser(String userEmail);

    @Query("SELECT DISTINCT category FROM foods WHERE userEmail = :userEmail")
    List<String> getCategoriesByUser(String userEmail);

    @Query("SELECT * FROM foods WHERE userEmail = :userEmail AND date(timestamp/1000, 'unixepoch') = date('now') ORDER BY timestamp DESC")
    List<Food> getTodayFoodsByUser(String userEmail);

    @Query("SELECT * FROM foods WHERE userEmail = :userEmail AND date(timestamp/1000, 'unixepoch') = date('now', '-1 day') ORDER BY timestamp DESC")
    List<Food> getYesterdayFoodsByUser(String userEmail);

    @Query("SELECT * FROM foods WHERE userEmail = :userEmail AND date(timestamp/1000, 'unixepoch') >= date('now', '-7 days') ORDER BY timestamp DESC")
    List<Food> getWeeklyFoodsByUser(String userEmail);

    @Query("SELECT category, SUM(calories) as totalCalories FROM foods WHERE userEmail = :userEmail GROUP BY category")
    List<CategoryCalories> getCaloriesByCategory(String userEmail);


    class CategoryCalories {
        public String category;
        public int totalCalories;
    }
}