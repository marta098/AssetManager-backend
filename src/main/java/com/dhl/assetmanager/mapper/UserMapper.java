package com.dhl.assetmanager.mapper;

import com.dhl.assetmanager.dto.response.UserDto;
import com.dhl.assetmanager.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface UserMapper {

    List<UserDto> userListToUserDtoList(List<User> users);

    @Mapping(source = "role.name", target = "role")
    UserDto userToUserDto(User user);

}
