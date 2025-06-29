package com.example.drugfoodapplication.ui.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import com.example.drugfoodapplication.data.dao.UserDao;
import com.example.drugfoodapplication.data.database.AppDatabase;
import com.example.drugfoodapplication.data.entity.User;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RegisterViewModel extends AndroidViewModel {

    private final UserDao userDao;
    private final Executor executor = Executors.newSingleThreadExecutor();

    public RegisterViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(application);
        userDao = db.userDao();
    }

    // Güncellenmiş metod
    public void registerUser(String userName, String email, String password, int age,
                             String disease, boolean allergyBee,
                             boolean allergyPollen, boolean allergyPeanut) {

        User newUser = new User();
        newUser.userName = userName;
        newUser.email = email;
        newUser.password = password;
        newUser.age = age;
        newUser.disease = disease;
        newUser.allergyBee = allergyBee;
        newUser.allergyPollen = allergyPollen;
        newUser.allergyPeanut = allergyPeanut;

        executor.execute(() -> userDao.insertUser(newUser));
    }
}
