package com.example.sp.maxsat.Services;

import com.example.sp.maxsat.Entities.UserEntity;
import com.example.sp.maxsat.Exceptions.Classes.NoSuchUserException;
import com.example.sp.maxsat.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
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
}
