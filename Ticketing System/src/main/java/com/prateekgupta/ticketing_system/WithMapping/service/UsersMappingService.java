package com.prateekgupta.ticketing_system.WithMapping.service;

import com.prateekgupta.ticketing_system.WithMapping.dto.UsersMappingDTO;

import java.util.List;

public interface UsersMappingService {
    Object addUser(UsersMappingDTO dto);
    Object addAll(List<UsersMappingDTO> dtos);
    Object getUserById(UsersMappingDTO dto);
    Object getUsers();
    Object updateUser(UsersMappingDTO dto);
    Object updateAllUser(List<UsersMappingDTO> dtos);
    Object deleteUser(UsersMappingDTO dto);
}
