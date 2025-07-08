// FoodViewModel.java
package com.example.drugfoodapplication.ui.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.drugfoodapplication.data.database.AppDatabase;
import com.example.drugfoodapplication.data.dao.FoodDao;
import com.example.drugfoodapplication.data.entity.Food;

import java.util.List;
import java.util.concurrent.Executors;

public class FoodViewModel extends AndroidViewModel {

    private FoodDao foodDao;
    private MutableLiveData<List<Food>> allFoods;
    private MutableLiveData<List<Food>> filteredFoods;
    private MutableLiveData<Integer> totalCalories;
    private MutableLiveData<String> currentCategory;
    private String userEmail;

    public FoodViewModel(@NonNull Application application) {
        super(application);

        AppDatabase database = AppDatabase.getInstance(application);
        foodDao = database.foodDao();

        allFoods = new MutableLiveData<>();
        filteredFoods = new MutableLiveData<>();
        totalCalories = new MutableLiveData<>();
        currentCategory = new MutableLiveData<>();

        currentCategory.setValue("Tümü");

        // User email'i SharedPreferences'ten al
        userEmail = getUserEmail();

        loadFoods();
    }

    // Public methods for Fragment
    public LiveData<List<Food>> getAllFoods() {
        return allFoods;
    }

    public LiveData<List<Food>> getFilteredFoods() {
        return filteredFoods;
    }

    public LiveData<Integer> getTotalCalories() {
        return totalCalories;
    }

    public LiveData<String> getCurrentCategory() {
        return currentCategory;
    }

    public void insertFood(Food food) {
        Executors.newSingleThreadExecutor().execute(() -> {
            foodDao.insert(food);
            loadFoods();
        });
    }

    public void updateFood(Food food) {
        Executors.newSingleThreadExecutor().execute(() -> {
            foodDao.update(food);
            loadFoods();
        });
    }

    public void deleteFood(Food food) {
        Executors.newSingleThreadExecutor().execute(() -> {
            foodDao.delete(food);
            loadFoods();
        });
    }

    public void toggleFavorite(Food food) {
        food.setFavorite(!food.isFavorite());
        updateFood(food);
    }

    public void setCategory(String category) {
        currentCategory.setValue(category);
        filterFoodsByCategory(category);
    }

    public void loadFoods() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Food> foods = foodDao.getFoodsByUser(userEmail);
            int totalCal = foodDao.getTotalCaloriesByUser(userEmail);

            allFoods.postValue(foods);
            totalCalories.postValue(totalCal);

            // Current category'ye göre filtrele
            String category = currentCategory.getValue();
            if (category != null) {
                filterFoodsByCategory(category);
            }
        });
    }

    public void loadTodayFoods() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Food> todayFoods = foodDao.getTodayFoodsByUser(userEmail);
            int todayCalories = foodDao.getTodayCaloriesByUser(userEmail);

            allFoods.postValue(todayFoods);
            totalCalories.postValue(todayCalories);

            String category = currentCategory.getValue();
            if (category != null) {
                filterFoodsByCategory(category);
            }
        });
    }

    public void searchFoods(String query) {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Food> searchResults = foodDao.searchFoodsByUser(userEmail, query);
            filteredFoods.postValue(searchResults);
        });
    }

    private void filterFoodsByCategory(String category) {
        List<Food> foods = allFoods.getValue();
        if (foods == null) return;

        Executors.newSingleThreadExecutor().execute(() -> {
            List<Food> filtered;

            if ("Tümü".equals(category)) {
                filtered = foods;
            } else if ("Favoriler".equals(category)) {
                filtered = foodDao.getFavoriteFoodsByUser(userEmail);
            } else {
                filtered = foodDao.getFoodsByUserAndCategory(userEmail, category);
            }

            filteredFoods.postValue(filtered);
        });
    }

    public void getFoodsByCategory(String category) {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Food> categoryFoods = foodDao.getFoodsByUserAndCategory(userEmail, category);
            filteredFoods.postValue(categoryFoods);
        });
    }

    public void getFavoriteFoods() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Food> favoriteFoods = foodDao.getFavoriteFoodsByUser(userEmail);
            filteredFoods.postValue(favoriteFoods);
        });
    }

    public void deleteAllFoods() {
        Executors.newSingleThreadExecutor().execute(() -> {
            foodDao.deleteAllFoodsByUser(userEmail);
            loadFoods();
        });
    }

    public void getCategoriesWithCalories() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<FoodDao.CategoryCalories> categoryCalories = foodDao.getCaloriesByCategory(userEmail);
            // Bu veriyi UI'da göstermek için ayrı bir LiveData kullanabilirsiniz
        });
    }

    public String getUserEmail() {
        // SharedPreferences'ten user email'i al
        return getApplication().getSharedPreferences("user_prefs", 0)
                .getString("user_email", "default@email.com");
    }

    public void setUserEmail(String email) {
        this.userEmail = email;
        loadFoods();
    }

    /*public String getUserEmail() {
        return this.userEmail;
    }*/

    // Utility methods
    public boolean hasNoFoods() {
        List<Food> foods = allFoods.getValue();
        return foods == null || foods.isEmpty();
    }

    public int getFoodCount() {
        List<Food> foods = allFoods.getValue();
        return foods != null ? foods.size() : 0;
    }

    public int getFoodCountByCategory(String category) {
        List<Food> foods = allFoods.getValue();
        if (foods == null) return 0;

        int count = 0;
        for (Food food : foods) {
            if (category.equals("Tümü") || food.getCategory().equals(category)) {
                count++;
            } else if (category.equals("Favoriler") && food.isFavorite()) {
                count++;
            }
        }
        return count;
    }
}