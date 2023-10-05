package project.practice.controller;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import java.time.LocalDate;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import project.practice.dto.UserRequestDto;
import project.practice.dto.UserResponseDto;
import project.practice.service.UserService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    private UserResponseDto userResponseDto = new UserResponseDto();
    private UserResponseDto userResponseDto2 = new UserResponseDto();

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
        userResponseDto.setId(1L);
        userResponseDto.setEmail("user1@example.com");
        userResponseDto.setFirstName("Nicolas");
        userResponseDto.setLastName("Cage");
        userResponseDto.setBirthDate(LocalDate.of(2000, 5, 15));
        userResponseDto.setAddress("Odessa");
        userResponseDto.setPhoneNumber("+1234567890");

        userResponseDto2.setId(2L);
        userResponseDto2.setEmail("user2@example.com");
        userResponseDto2.setFirstName("Vin");
        userResponseDto2.setLastName("Diesel");
        userResponseDto2.setBirthDate(LocalDate.of(1995, 9, 21));
        userResponseDto2.setAddress("Ukraine");
        userResponseDto2.setPhoneNumber("+9876543210");
    }

    @Test
    void getAll_ShouldReturnAllUsers_WhenGettingAllUsers() {
        List<UserResponseDto> mockUserResponseDto = List.of(userResponseDto, userResponseDto2);

        Mockito.when(userService.findAll()).thenReturn(mockUserResponseDto);

        RestAssuredMockMvc.when()
                .get("/users")
                .then()
                .statusCode(200)
                .body("size()", Matchers.equalTo(2))
                .body("[0].id", Matchers.equalTo(1))
                .body("[0].email", Matchers.equalTo("user1@example.com"))
                .body("[0].firstName", Matchers.equalTo("Nicolas"))
                .body("[1].id", Matchers.equalTo(2))
                .body("[1].birthDate", Matchers.equalTo(LocalDate.of(1995, 9, 21).toString()))
                .body("[1].address", Matchers.equalTo("Ukraine"));
    }

    @Test
    void findUserById_ShouldReturnUserById_WhenGettingUserById() {

        Mockito.when(userService.findById(1L)).thenReturn(userResponseDto);

        RestAssuredMockMvc.when()
                .get("/users/1")
                .then()
                .statusCode(200)
                .body("id", Matchers.equalTo(1))
                .body("email", Matchers.equalTo("user1@example.com"))
                .body("firstName", Matchers.equalTo("Nicolas"));
    }

    @Test
    void findUsersByBirthDateBetween_ShouldReturnUsersInRange_GettingUsersByBirthDateBetween() {
        LocalDate from = LocalDate.of(1995,1,1);
        LocalDate to = LocalDate.of(2000,1,1);

        List<UserResponseDto> mockUserResponseDto = List.of(userResponseDto, userResponseDto2);

        Mockito.when(userService.findUsersByBirthDateBetween(from, to))
                .thenReturn(mockUserResponseDto);

        RestAssuredMockMvc.given()
                .contentType(ContentType.JSON)
                .queryParam("from", from.toString())
                .queryParam("to", to.toString())
                .when()
                .get("/users/by-birthDate-interval")
                .then()
                .statusCode(200)
                .body("size()", Matchers.equalTo(2))
                .body("[0].id", Matchers.equalTo(1))
                .body("[1].id", Matchers.equalTo(2))
                .body("[0].firstName", Matchers.equalTo("Nicolas"))
                .body("[1].firstName", Matchers.equalTo("Vin"))
                .body("[0].birthDate", Matchers.equalTo(LocalDate.of(2000, 5, 15).toString()))
                .body("[1].birthDate", Matchers.equalTo(LocalDate.of(1995, 9, 21).toString()));
    }

    @Test
    void updateAllUserFields_ShouldUpdateAllFields_WhenUpdatingUser() {
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setEmail(userResponseDto2.getEmail());
        userRequestDto.setFirstName(userResponseDto2.getFirstName());
        userRequestDto.setLastName(userResponseDto2.getLastName());
        userRequestDto.setBirthDate(userResponseDto2.getBirthDate());
        Mockito.when(userService.update(userResponseDto.getId(),
                userRequestDto)).thenReturn(userResponseDto);

        RestAssuredMockMvc.given()
                .contentType(ContentType.JSON)
                .queryParam("id", userResponseDto.getId())
                .body(userRequestDto)
                .when()
                .put("/users/1")
                .then()
                .statusCode(200)
                .body("id", Matchers.equalTo(1))
                .body("email", Matchers.equalTo("user1@example.com"))
                .body("firstName", Matchers.equalTo("Nicolas"));

    }

    @Test
    void updateAnyFields_ShouldUpdateSpecificFields_WhenUpdatingUserFields() {
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setEmail(userResponseDto2.getEmail());

        Mockito.when(userService.updateAnyField(userResponseDto.getId(),
                userRequestDto)).thenReturn(userResponseDto);

        RestAssuredMockMvc.given()
                .contentType(ContentType.JSON)
                .queryParam("id", userResponseDto.getId())
                .body(userRequestDto)
                .when()
                .put("/users/1/update-fields")
                .then()
                .statusCode(200)
                .body("id", Matchers.equalTo(1))
                .body("email", Matchers.equalTo("user1@example.com"))
                .body("firstName", Matchers.equalTo("Nicolas"));
    }

    @Test
    void createUser_ShouldCreateUser_WhenCreatingUser() {
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setEmail(userResponseDto2.getEmail());
        userRequestDto.setFirstName(userResponseDto2.getFirstName());
        userRequestDto.setLastName(userResponseDto2.getLastName());
        userRequestDto.setBirthDate(userResponseDto2.getBirthDate());

        Mockito.when(userService.save(userRequestDto)).thenReturn(userResponseDto);

        RestAssuredMockMvc.given()
                .contentType(ContentType.JSON)
                .body(userRequestDto)
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .body("id", Matchers.equalTo(1))
                .body("email", Matchers.equalTo("user1@example.com"))
                .body("firstName", Matchers.equalTo("Nicolas"))
                .body("birthDate", Matchers.equalTo(LocalDate.of(2000, 5, 15).toString()));
    }

    @Test
    void delete_ShouldDeleteUser_WhenDeletingUser() {
        RestAssuredMockMvc.given()
                .contentType(ContentType.JSON)
                .queryParam("id", userResponseDto.getId())
                .when()
                .delete("users/1")
                .then()
                .statusCode(204);
    }
}
