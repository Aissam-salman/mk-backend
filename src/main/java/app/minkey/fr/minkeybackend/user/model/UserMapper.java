package app.minkey.fr.minkeybackend.user.model;

import app.minkey.fr.minkeybackend.dto.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    UserResponse toUserResponse(User user);
    User mapDtoToUser(UserResponse userResponse);
}