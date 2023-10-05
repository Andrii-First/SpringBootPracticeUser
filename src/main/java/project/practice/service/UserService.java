package project.practice.service;

import java.time.LocalDate;
import java.util.List;
import project.practice.dto.UserRequestDto;
import project.practice.dto.UserResponseDto;

public interface UserService {

    UserResponseDto save(UserRequestDto requestDto);

    List<UserResponseDto> findAll();

    UserResponseDto findById(Long id);

    List<UserResponseDto> findUsersByBirthDateBetween(LocalDate from, LocalDate to);

    void deleteById(Long id);

    UserResponseDto update(Long id, UserRequestDto userDto);

    UserResponseDto updateAnyField(Long id, UserRequestDto userDto);
}
