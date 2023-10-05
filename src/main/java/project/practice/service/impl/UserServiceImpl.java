package project.practice.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import project.practice.dto.UserRequestDto;
import project.practice.dto.UserResponseDto;
import project.practice.exception.EntityNotFoundException;
import project.practice.mapper.UserMapper;
import project.practice.model.User;
import project.practice.repository.UserRepository;
import project.practice.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    private static final String PATTERN_OF_EMAIL = "^(?=.{1,64}@)[A-Za-z0-9_-]"
            + "+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    private Environment environment;
    private UserRepository repository;
    private UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository repository,
                           UserMapper userMapper,
                           Environment environment) {
        this.repository = repository;
        this.userMapper = userMapper;
        this.environment = environment;
    }

    @Override
    public UserResponseDto save(UserRequestDto requestDto) {
        User user = userMapper.toModel(requestDto);
        return userMapper.toDto(repository.save(user));
    }

    @Override
    public List<UserResponseDto> findAll() {
        return repository.findAll()
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    public UserResponseDto findById(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find user by id " + id));
        return userMapper.toDto(user);
    }

    @Override
    public List<UserResponseDto> findUsersByBirthDateBetween(LocalDate from, LocalDate to) {
        return repository.findUsersByBirthDateBetween(from, to).stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public UserResponseDto update(Long id, UserRequestDto userDto) {
        User user = userMapper.toModel(userDto);
        user.setId(id);
        return userMapper.toDto(repository.save(user));
    }

    @Override
    public UserResponseDto updateAnyField(Long id, UserRequestDto userDto) {
        User user = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find "
                        + "user for updating by id: " + id));
        ;
        return userMapper.toDto(repository.save(updateUserNotNullFields(user,userDto)));
    }

    private User updateUserNotNullFields(User updatingUser, UserRequestDto updates) {

        if (updates.getEmail() != null) {
            if (!(Pattern.compile(PATTERN_OF_EMAIL).matcher(updates.getEmail()).matches())) {
                throw new RuntimeException("Invalid email for updating.");
            }
            updatingUser.setEmail(updates.getEmail());
        }
        if (updates.getFirstName() != null) {
            updatingUser.setFirstName(updates.getFirstName());
        }
        if (updates.getLastName() != null) {
            updatingUser.setLastName(updates.getLastName());
        }
        if (updates.getAddress() != null) {
            updatingUser.setAddress(updates.getAddress());
        }
        if (updates.getBirthDate() != null) {
            if (!(updates.getBirthDate().plusYears(Long.parseLong(Objects
                            .requireNonNull(environment.getProperty("minAge"))))
                    .isBefore(LocalDate.now()))) {
                throw new RuntimeException("Invalid date of birth. Age should not be less than "
                + environment.getProperty("minAge"));
            }
            updatingUser.setBirthDate(updates.getBirthDate());
        }
        if (updates.getPhoneNumber() != null) {
            updatingUser.setPhoneNumber(updates.getPhoneNumber());
        }
        return updatingUser;
    }
}
