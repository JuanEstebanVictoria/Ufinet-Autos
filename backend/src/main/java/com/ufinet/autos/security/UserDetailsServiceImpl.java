package com.ufinet.autos.security;

import com.ufinet.autos.model.User;
import com.ufinet.autos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Loads user data from the database for Spring Security authentication.
 * Returns a {@link CustomUserDetails} object that includes the user's database ID.
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Finds the user by username and returns their details for authentication.
     *
     * @param username the username to look up
     * @return a {@link CustomUserDetails} object with the user's id, username, and password
     * @throws UsernameNotFoundException if no user with that username exists
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found with username: " + username));

        return new CustomUserDetails(user.getId(), user.getUsername(), user.getPassword());
    }
}
