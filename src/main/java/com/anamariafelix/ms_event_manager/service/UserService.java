package com.anamariafelix.ms_event_manager.service;

import com.anamariafelix.ms_event_manager.exception.EmailUniqueViolationException;
import com.anamariafelix.ms_event_manager.model.User;
import com.anamariafelix.ms_event_manager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User create(User user){
        try{
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userRepository.save(user);
        }catch (DuplicateKeyException e){
            throw new EmailUniqueViolationException(String.format("Email %s already registered", user.getEmail()));
        }
    }

    public User findByEmail(String username) {
        return userRepository.findByEmail(username);
    }
}
