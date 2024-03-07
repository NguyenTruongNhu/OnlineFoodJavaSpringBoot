package com.ntndev.onlinefoodordering.service;


import com.ntndev.onlinefoodordering.model.User;

public interface UserService {
    User findUserByJwtToken(String jwt) throws Exception;
    User findUserByEmail(String email) throws Exception;
}
