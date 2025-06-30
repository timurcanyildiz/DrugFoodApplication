package com.example.drugfoodapplication.ui.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.drugfoodapplication.data.dao.UserDao;
import com.example.drugfoodapplication.data.database.AppDatabase;
import com.example.drugfoodapplication.data.entity.User;

public class HomeViewModel extends AndroidViewModel {

    private final UserDao userDao;
    public LiveData<User> userLiveData;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(application);
        userDao = db.userDao();
    }

    public void loadUser(String email) {
        User user = userDao.getUserByEmail(email);
    }
}
