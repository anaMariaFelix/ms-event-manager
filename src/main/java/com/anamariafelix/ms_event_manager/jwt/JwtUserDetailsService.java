package com.anamariafelix.ms_event_manager.jwt;

import com.anamariafelix.ms_event_manager.model.User;
import com.anamariafelix.ms_event_manager.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class JwtUserDetailsService implements UserDetailsService {

    private  final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userService.findByEmail(username);
        log.info("INFO: User found: {}, password in the database: {}", user.getEmail(), user.getPassword());
        return new JwtUserDetails(user);
    }

    public JwtToken getTokenAuthenticated(String usuername, String role){
        return JwtUtils.createToken(usuername, role.substring("Role_".length()));
    }
}

