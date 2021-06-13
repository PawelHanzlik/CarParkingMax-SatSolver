package com.example.sp.maxsat.UnitTest;

import com.example.sp.maxsat.Entities.UserEntity;
import com.example.sp.maxsat.Exceptions.Classes.NoSuchUserException;
import com.example.sp.maxsat.Exceptions.Classes.NoSuchZoneException;
import com.example.sp.maxsat.Repositories.UserRepository;
import com.example.sp.maxsat.Repositories.ZoneRepository;
import com.example.sp.maxsat.Services.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static com.example.sp.maxsat.DataProviders.UserServiceUnitTestDataProvider.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

public class UserServiceUnitTests {

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Mock
    ZoneRepository zoneRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUserTest(){
        when(userRepository.findById(userId)).thenReturn(java.util.Optional.ofNullable(userEntity));
        UserEntity userEntity1 = this.userService.getUser(userId);
        assertNotNull(userEntity1);
        assertEquals(1, userEntity1.getUserId());
        assertEquals("small", userEntity1.getCarSize());
        assertEquals(1, userEntity1.getPreferableZone());
        assertEquals("test", userEntity1.getName());
        assertEquals("test", userEntity1.getSurname());
        assertEquals(30, userEntity1.getAge());
    }

    @Test
    void getUserNoSuchUserExceptionTest(){
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(NoSuchUserException.class, () -> userService.getUser(userId));
    }

    @Test
    void getAllUsers(){
        when(userRepository.findAll()).thenReturn(userEntities);
        List<UserEntity> userEntityList = this.userService.getAllUsers();
        assertEquals(userEntity,userEntityList.get(0));
        assertEquals(userEntity1,userEntityList.get(1));
    }

    @Test
    void createUserTest(){
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity1);
        UserEntity newUserEntity = this.userService.addUser(userEntity1);
        assertNotNull(newUserEntity);
        assertEquals(2, userEntity1.getUserId());
        assertEquals("small", userEntity1.getCarSize());
        assertEquals(1, userEntity1.getPreferableZone());
        assertEquals("new_test", userEntity1.getName());
        assertEquals("new_test", userEntity1.getSurname());
        assertEquals(40, userEntity1.getAge());
    }

    @Test
    void deleteUserTest() {
        Optional<UserEntity> optionalUserEntity = Optional.of(userEntity);

        when(userRepository.findById(userId)).thenReturn(optionalUserEntity);

        userService.deleteUser(userId);

        Mockito.verify(userRepository, times(1)).delete(optionalUserEntity.get());
    }

    @Test
    void deleteUserNoSuchUserExceptionTest(){
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(NoSuchUserException.class, () -> userService.deleteUser(userId));
    }

    @Test
    void changeUserCarSizeTest(){
        when(userRepository.findById(3L)).thenReturn(java.util.Optional.ofNullable(userEntity2));
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity3);
        this.userService.changeUserCarSize(3L,1);
        assertEquals(3L, userEntity2.getUserId());
        assertEquals(1, userEntity2.getCarSize());
        assertEquals(1, userEntity2.getPreferableZone());
        assertEquals("test", userEntity2.getName());
        assertEquals("test", userEntity2.getSurname());
        assertEquals(35, userEntity2.getAge());
    }

    @Test
    void changeUserCarSizeNoSuchUserExceptionTest(){
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(NoSuchUserException.class, () -> userService.changeUserCarSize(userId,2));
    }

    @Test
    void changeUserPreferableZoneTest(){
        when(userRepository.findById(4L)).thenReturn(java.util.Optional.ofNullable(userEntity4));
        when(zoneRepository.findById(3L)).thenReturn(java.util.Optional.ofNullable(zoneEntity));
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity5);
        this.userService.changeUserPreferableZone(4L,3L);
        assertEquals(4L, userEntity4.getUserId());
        assertEquals("small", userEntity4.getCarSize());
        assertEquals(3L, userEntity4.getPreferableZone());
        assertEquals("test", userEntity4.getName());
        assertEquals("test", userEntity4.getSurname());
        assertEquals(30, userEntity4.getAge());
    }

    @Test
    void changeUserPreferableZoneNoSuchUserExceptionTest(){
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(NoSuchUserException.class, () -> userService.changeUserPreferableZone(3L,3L));
    }

    @Test
    void changeUserPreferableZoneNoSuchZoneExceptionTest(){
        when(userRepository.findById(3L)).thenReturn(java.util.Optional.ofNullable(userEntity3));
        when(zoneRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NoSuchZoneException.class, () -> userService.changeUserPreferableZone(3L,3L));
    }
}
