package com.example.drugfoodapplication.ui.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.drugfoodapplication.data.database.AppDatabase;
import com.example.drugfoodapplication.data.entity.User;

public class ProfileViewModel extends AndroidViewModel {

    private final AppDatabase db;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        db = AppDatabase.getInstance(application);
    }

    public LiveData<User> getUserByEmail(String email) {
        return db.userDao().getUserLiveDataByEmail(email);
    }

    public void updateUser(User user) {
        new Thread(() -> db.userDao().updateUser(user)).start();
    }
}
