package project.practice.controller;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import project.practice.dto.UserRequestDto;
import project.practice.dto.UserResponseDto;
import project.practice.service.UserService;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserResponseDto> getAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public UserResponseDto findUserById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @GetMapping("/by-birthDate-interval")
    public List<UserResponseDto> findUsersByBirthDateBetween(@RequestParam LocalDate from,
                                                             @RequestParam LocalDate to) {
        if (to.isBefore(from)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Date 'from' should be "
                    + "before date 'to'");
        }
        return userService.findUsersByBirthDateBetween(from, to);
    }

    @PutMapping("/{id}")
    public UserResponseDto updateAllUserFields(@RequestBody @Valid UserRequestDto userDto,
                                               @PathVariable Long id) {
        return userService.update(id,userDto);
    }

    @PutMapping("/{id}/update-fields")
    public UserResponseDto updateAnyFields(@RequestBody UserRequestDto userDtoUpdates,
                                            @PathVariable Long id) {
        return userService.updateAnyField(id,userDtoUpdates);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto createUser(@RequestBody @Valid UserRequestDto userRequestDto) {

        return userService.save(userRequestDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userService.deleteById(id);
    }
}
