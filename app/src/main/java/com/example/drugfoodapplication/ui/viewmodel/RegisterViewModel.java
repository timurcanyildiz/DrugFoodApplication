package com.example.drugfoodapplication.ui.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.drugfoodapplication.data.database.AppDatabase;
import com.example.drugfoodapplication.data.entity.User;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RegisterViewModel extends AndroidViewModel {

    private final AppDatabase appDatabase;
    private final Executor executor;

    // Kayıt başarılı/başarısız, hata mesajı gibi durumlar için LiveData
    private final MutableLiveData<Boolean> registrationSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public RegisterViewModel(@NonNull Application application) {
        super(application);
        appDatabase = AppDatabase.getInstance(application);
        executor = Executors.newSingleThreadExecutor();
    }

    // Kayıt işlemini başlatır
    public void registerUser(User user) {
        executor.execute(() -> {
            // Aynı email ile kullanıcı var mı kontrol et
            User existingUser = appDatabase.userDao().getUserByEmail(user.email);
            if (existingUser != null) {
                // Zaten kayıtlı
                errorMessage.postValue("This email is already registered.");
                registrationSuccess.postValue(false);
            } else {
                // Yeni kullanıcı ekle
                appDatabase.userDao().insertUser(user);
                registrationSuccess.postValue(true);
            }
        });
    }

    // LiveData ile UI katmanına bildirimler
    public LiveData<Boolean> getRegistrationSuccess() {
        return registrationSuccess;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
}
