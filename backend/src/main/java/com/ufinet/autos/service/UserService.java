package com.ufinet.autos.service;

import com.ufinet.autos.model.User;

/**
 * Service for looking up users by username.
 */
public interface UserService {

    User getByUsername(String username);
}
