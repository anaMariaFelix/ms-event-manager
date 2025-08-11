package com.anamariafelix.ms_event_manager.service.user;


import com.anamariafelix.ms_event_manager.exception.EmailUniqueViolationException;
import com.anamariafelix.ms_event_manager.model.User;
import com.anamariafelix.ms_event_manager.repository.UserRepository;
import com.anamariafelix.ms_event_manager.service.UserService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.bson.assertions.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void create_ShouldEncodePasswordAndSaveUser_WhenEmailNotExists() {

        User user = new User();
        user.setEmail("userTest@gmail.com");
        user.setPassword("123456");

        String encodedPassword = "encoded123456";
        when(passwordEncoder.encode("123456")).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User savedUser = userService.create(user);

        assertNotNull(savedUser);
        assertEquals(encodedPassword, savedUser.getPassword());
        verify(passwordEncoder).encode("123456");
        verify(userRepository).save(user);
    }

    @Test
    void create_ShouldThrowEmailUniqueViolationException_whenThisEmailAlreadyExistsInTheBank() {
        User user = new User();
        user.setEmail("emailduplicate@gmail.com");
        user.setPassword("123456");

        when(passwordEncoder.encode("123456")).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenThrow(new DuplicateKeyException("Email Duplicate"));

        EmailUniqueViolationException ex = assertThrows(
                EmailUniqueViolationException.class,
                () -> userService.create(user)
        );

        assertEquals("Email emailduplicate@gmail.com already registered", ex.getMessage());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void findByEmail_ShouldReturnUser_WhenUserExists() {
        String email = "usertest@gmail.com";
        User user = new User();
        user.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(user);

        User foundUser = userService.findByEmail(email);

        assertNotNull(foundUser);
        assertEquals(email, foundUser.getEmail());
        verify(userRepository).findByEmail(email);
    }

    @Test
    void findByEmail_ShouldReturnNull_WhenUserNotExists() {
        String email = "usernotfound@gmail.com";
        when(userRepository.findByEmail(email)).thenReturn(null);

        User result = userService.findByEmail(email);

        assertNull(result);
        verify(userRepository).findByEmail(email);
    }
}
