package project.practice.mapper;

import org.mapstruct.Mapper;
import project.practice.config.MapperConfig;
import project.practice.dto.UserRequestDto;
import project.practice.dto.UserResponseDto;
import project.practice.model.User;

@Mapper(config = MapperConfig.class)
public interface UserMapper {

    UserResponseDto toDto(User user);

    User toModel(UserRequestDto requestDto);
}
