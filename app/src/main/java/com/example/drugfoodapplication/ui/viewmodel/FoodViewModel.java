package com.example.drugfoodapplication.ui.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.drugfoodapplication.data.dao.FoodDao;
import com.example.drugfoodapplication.data.database.AppDatabase;
import com.example.drugfoodapplication.data.entity.Food;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class FoodViewModel extends AndroidViewModel {
    private final FoodDao foodDao;
    private final Executor executor = Executors.newSingleThreadExecutor();

    public FoodViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(application);
        foodDao = db.foodDao();
    }

    public LiveData<List<Food>> getFoodsForUser(String userEmail) {
        return foodDao.getFoodsForUser(userEmail);
    }

    public LiveData<List<Food>> getFavoriteFoodsForUser(String userEmail) {
        return foodDao.getFavoriteFoodsForUser(userEmail);
    }

    public void insertFood(Food food) {
        executor.execute(() -> foodDao.insertFood(food));
    }

    public void updateFood(Food food) {
        executor.execute(() -> foodDao.updateFood(food));
    }

    public void deleteFood(Food food) {
        executor.execute(() -> foodDao.deleteFood(food));
    }
}
