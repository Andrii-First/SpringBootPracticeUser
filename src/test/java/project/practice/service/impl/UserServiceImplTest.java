package project.practice.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import project.practice.dto.UserRequestDto;
import project.practice.dto.UserResponseDto;
import project.practice.exception.EntityNotFoundException;
import project.practice.mapper.UserMapper;
import project.practice.model.User;
import project.practice.repository.UserRepository;

@SpringBootTest
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private Environment environment;

    @Test
    public void testUpdateAnyField() {
        UserRequestDto updatedUserDto = new UserRequestDto();
        updatedUserDto.setFirstName("UpdatedFirstName");
        updatedUserDto.setLastName("UpdatedLastName");
        updatedUserDto.setAddress("UpdatedAddress");
        updatedUserDto.setBirthDate(LocalDate.of(2003, 4, 5));
        updatedUserDto.setPhoneNumber("UpdatedPhoneNumber");
        Long userId = 1L;

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setEmail("test@example.com");
        existingUser.setFirstName("Bob");
        existingUser.setLastName("Bobson");
        existingUser.setAddress("Ukraine");
        existingUser.setBirthDate(LocalDate.of(2002, 3, 4));
        existingUser.setPhoneNumber("+3000999");

        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setEmail(existingUser.getEmail());
        updatedUser.setFirstName(updatedUserDto.getFirstName());
        updatedUser.setLastName(updatedUserDto.getLastName());
        updatedUser.setAddress(updatedUserDto.getAddress());
        updatedUser.setBirthDate(updatedUserDto.getBirthDate());
        updatedUser.setPhoneNumber(updatedUserDto.getPhoneNumber());

        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setId(userId);
        responseDto.setEmail(updatedUser.getEmail());
        responseDto.setFirstName(updatedUser.getFirstName());
        responseDto.setLastName(updatedUser.getLastName());
        responseDto.setAddress(updatedUser.getAddress());
        responseDto.setBirthDate(updatedUser.getBirthDate());
        responseDto.setPhoneNumber(updatedUser.getPhoneNumber());

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        when(userMapper.toDto(any(User.class))).thenReturn(responseDto);
        when(environment.getProperty("minAge")).thenReturn("18");

        UserResponseDto updatedResponseDto = userService.updateAnyField(userId, updatedUserDto);

        assertNotNull(updatedResponseDto);
        assertEquals(userId, updatedResponseDto.getId());
        assertEquals(updatedUserDto.getFirstName(), updatedResponseDto.getFirstName());
        assertEquals(updatedUserDto.getLastName(), updatedResponseDto.getLastName());
        assertEquals(updatedUserDto.getAddress(), updatedResponseDto.getAddress());
        assertEquals(updatedUserDto.getBirthDate(), updatedResponseDto.getBirthDate());
        assertEquals(updatedUserDto.getPhoneNumber(), updatedResponseDto.getPhoneNumber());

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(updatedUser);
    }

    @Test
    public void testUpdateAnyFieldUserNotFound() {
        Long userId = 1L;
        UserRequestDto updatedUserDto = new UserRequestDto();
        updatedUserDto.setFirstName("UpdatedFirstName");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService
                .updateAnyField(userId, updatedUserDto));
    }

    @Test
    public void testUpdateAnyFieldNoUpdates() {

        Long userId = 1L;
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setEmail("test@example.com");
        existingUser.setFirstName("John");

        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setId(userId);
        responseDto.setEmail(existingUser.getEmail());
        responseDto.setFirstName(existingUser.getFirstName());

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);
        when(userMapper.toDto(any(User.class))).thenReturn(responseDto);

        UserRequestDto updatedUserDto = new UserRequestDto();

        UserResponseDto updatedResponseDto = userService.updateAnyField(userId, updatedUserDto);

        assertNotNull(updatedResponseDto);
        assertEquals(userId, updatedResponseDto.getId());
        assertEquals(existingUser.getFirstName(), updatedResponseDto.getFirstName());

        verify(userRepository, times(1)).findById(userId);
    }
}
