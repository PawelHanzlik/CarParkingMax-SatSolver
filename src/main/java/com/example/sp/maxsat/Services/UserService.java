package com.example.sp.maxsat.Services;

import com.example.sp.maxsat.Entities.UserEntity;
import com.example.sp.maxsat.Exceptions.Classes.NoSuchUserException;
import com.example.sp.maxsat.Exceptions.Classes.NoSuchZoneException;

import java.util.List;

public interface UserService {

    UserEntity getUser(Long userId) throws NoSuchUserException;
    List<UserEntity> getAllUsers();
    UserEntity addUser(UserEntity user);
    void deleteUser(Long userId) throws NoSuchUserException;
    void changeUserCarSize(Long userId, Integer newCarSize) throws NoSuchUserException;
    void changeUserPreferableZone(Long userId, Long zoneId) throws NoSuchUserException, NoSuchZoneException;
}
