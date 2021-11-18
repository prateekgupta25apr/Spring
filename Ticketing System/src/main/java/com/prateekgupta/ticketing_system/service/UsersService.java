package com.prateekgupta.ticketing_system.service;

import com.prateekgupta.ticketing_system.dto.UsersDTO;

import java.util.List;

public interface UsersService {
    Object addUser(UsersDTO dto);
    Object addAll(List<UsersDTO> dtos);
    Object getUserById(UsersDTO dto);
    Object getUsers();
    Object updateUser(UsersDTO dto);
    Object updateAll(List<UsersDTO> dtos);
    Object deleteUser(UsersDTO dto);
}
