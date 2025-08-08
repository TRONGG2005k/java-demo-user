package user.demo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import user.demo.dto.request.RegisterRequest;
import user.demo.dto.request.UpdateUserRequest;
import user.demo.dto.response.RoleResponse;
import user.demo.dto.response.UserResponse;
import user.demo.entity.Role;
import user.demo.entity.User;

import java.util.Set;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User registerRequestToUser(RegisterRequest request);

    RegisterRequest userToRegisterRequest(User user);

    @Mapping(target = "roles", ignore = true)
    UserResponse userToUserResponse(User user);

    Set<RoleResponse> rolesToRoleResponses(Set<Role> roles);

    @Mapping(target = "roles", ignore = true)
    void updateUserRequestToUser(@MappingTarget User user, UpdateUserRequest request);
}
