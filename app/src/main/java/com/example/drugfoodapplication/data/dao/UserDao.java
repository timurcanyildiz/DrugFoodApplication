package com.example.drugfoodapplication.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.example.drugfoodapplication.data.entity.User;

@Dao
public interface UserDao {
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    User getUserByEmail(String email);

    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    User getUserByEmailPassword(String email, String password);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(User user);
}


