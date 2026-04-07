package com.demo.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.demo.user.dto.UserRequest;
import com.demo.user.dto.UserResponse;
import com.demo.user.entity.Users;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toResponse(Users user);

    @Mapping(target = "roleId", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Users toEntity(UserRequest request);
}