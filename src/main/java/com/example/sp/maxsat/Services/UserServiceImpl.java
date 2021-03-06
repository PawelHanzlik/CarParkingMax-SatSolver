package com.example.sp.maxsat.Services;

import com.example.sp.maxsat.Entities.UserEntity;
import com.example.sp.maxsat.Entities.ZoneEntity;
import com.example.sp.maxsat.Exceptions.Classes.NoSuchUserException;
import com.example.sp.maxsat.Exceptions.Classes.NoSuchZoneException;
import com.example.sp.maxsat.Repositories.UserRepository;
import com.example.sp.maxsat.Repositories.ZoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ZoneRepository zoneRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,ZoneRepository zoneRepository) {
        this.userRepository = userRepository;
        this.zoneRepository = zoneRepository;
    }

    @Override
    public UserEntity getUser(Long userId) throws NoSuchUserException{
        Optional<UserEntity> userOptional = this.userRepository.findById(userId);
        if (userOptional.isEmpty()){
            throw new NoSuchUserException();
        }
        return userOptional.get();
    }

    @Override
    public List<UserEntity> getAllUsers() {
        return this.userRepository.findAll();
    }

    @Override
    public UserEntity addUser(UserEntity user) {
        return this.userRepository.save(user);
    }

    @Override
    public void deleteUser(Long userId) throws NoSuchUserException{
        Optional<UserEntity> userOptional = this.userRepository.findById(userId);
        if (userOptional.isEmpty()){
            throw new NoSuchUserException();
        }
        else{
            UserEntity user = userOptional.get();
            this.userRepository.delete(user);
        }
    }

    @Override
    public void changeUserCarSize(Long userId, Integer newCarSize) throws NoSuchUserException {
        Optional<UserEntity> userOptional = this.userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new NoSuchUserException();
        } else {
            UserEntity user = userOptional.get();
            user.setCarSize(newCarSize);
            this.userRepository.save(user);
        }
    }

    @Override
    public void changeUserPreferableZone(Long userId, Long newZoneId) throws NoSuchUserException, NoSuchZoneException {
        Optional<UserEntity> userOptional = this.userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new NoSuchUserException();
        }
        Optional<ZoneEntity> zoneOptional = this.zoneRepository.findById(newZoneId);
        if (zoneOptional.isEmpty()) {
            throw new NoSuchZoneException();
        } else {
            UserEntity user = userOptional.get();
            user.setPreferableZone(newZoneId);
            this.userRepository.save(user);
        }
    }
}
