package project.practice.dto;

import java.time.LocalDate;
import lombok.Data;
import project.practice.validation.BirthDate;
import project.practice.validation.Email;

@Data
public class UserRequestDto {
    @Email
    private String email;
    private String firstName;
    private String lastName;
    @BirthDate
    private LocalDate birthDate;
    private String address;
    private String phoneNumber;
}
