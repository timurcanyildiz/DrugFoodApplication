package com.example.drugfoodapplication.ui.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import com.example.drugfoodapplication.data.dao.UserDao;
import com.example.drugfoodapplication.data.database.AppDatabase;
import com.example.drugfoodapplication.data.entity.User;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class LoginViewModel extends AndroidViewModel {

    private final UserDao userDao;
    private final Executor executor = Executors.newSingleThreadExecutor();

    public interface LoginCallback {
        void onLoginResult(User user);
    }

    public LoginViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(application);
        userDao = db.userDao();
    }

    public void loginUser(String email, String password, LoginCallback callback) {
        executor.execute(() -> {
            User user = userDao.getUserByEmailPassword(email, password);
            callback.onLoginResult(user);
        });
    }
}
