package com.prateekgupta.ticketing_system.service;

import com.prateekgupta.ticketing_system.dto.UsersDTO;

public interface UsersService {
    Object addUser(UsersDTO dto);
    Object getUsers();
    Object updateUser(UsersDTO dto);
}
