package com.ufinet.autos.domain.port.in;

import com.ufinet.autos.domain.model.User;

/**
 * Service for looking up users by username.
 */
public interface UserService {

    User getByUsername(String username);
}
