# :computer: Java Practical Test Assignment

## :page_facing_up: Description

This practical test assignment involves implementing a RESTful API based on a Spring Boot web application for managing the "Users" resource. The task is divided into two parts: learning RESTful API design practices and implementing the API according to the specified requirements.

## :books: Resources

1. [RESTful API Design. Best Practices in a Nutshell](#)
2. [Error Handling for REST with Spring | Baeldung](#)
3. [Testing in Spring Boot | Baeldung](#)
4. [Testing | Spring](#)

## :white_check_mark: Requirements

1. **User Fields:**
   - :email: Email (required). Must have validation against an email pattern.
   - :man: First name (required).
   - :woman: Last name (required).
   - :calendar: Birth date (required). Value must be earlier than the current date.
   - :house: Address (optional).
   - :telephone_receiver: Phone number (optional).

2. **Functionality:**
   - :heavy_plus_sign: Create user. It allows the registration of users who are more than [18] years old. The value [18] should be taken from the properties file.
   - :arrows_counterclockwise: Update one or more user fields.
   - :arrows_counterclockwise: Update all user fields.
   - :x: Delete user.
   - :mag: Search for users by birth date range. Add validation that checks that "From" is less than "To." Should return a list of objects.

3. :bookmark_tabs: Code is covered by unit tests using Spring.
4. :exclamation: Error handling for REST.
5. API responses are in JSON format.
6. :floppy_disk: Use of an H2 InMemory database.
7. Any version of Spring Boot. Java version of your choice.

## :file_folder: Project Structure

```plaintext
project/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── project.practice/
│   │   │   │   ├── config/
│   │   │   │   │   └── MapperConfig.java
│   │   │   │   ├── controller/
│   │   │   │   │   └── UserController.java
│   │   │   │   ├── dto/
│   │   │   │   │   ├── UserRequestDto.java
│   │   │   │   │   └── UserResponseDto.java
│   │   │   │   ├── exception/
│   │   │   │   │   ├── CustomGlobalExceptionHandler.java
│   │   │   │   │   └── EntityNotFoundException.java
│   │   │   │   ├── mapper/
│   │   │   │   │   └── UserMapper.java
│   │   │   │   ├── model/
│   │   │   │   │   └── User.java
│   │   │   │   ├── repository/
│   │   │   │   │   └── UserRepository.java
│   │   │   │   ├── service/
│   │   │   │   │   ├── impl/
│   │   │   │   │   │   └── UserServiceImpl.java
│   │   │   │   │   ├── UserService.java
│   │   │   │   │   └── UserServiceImpl.java
│   │   │   │   └── validation/
│   │   │   │       ├── BirthDate.java
│   │   │   │       ├── BirthDateValidator.java
│   │   │   │       ├── Email.java
│   │   │   │       └── EmailValidator.java
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── resources/
│   └── test/
│   │   └── java/
│   │       └── project.practice/
│   │           ├── controller/
│   │           │   └── UserControllerTest.java
│   │           └── service/
│   │               └── impl/
│   │                   └── UserServiceImplTest.java
│   ├── resources/
│   └── ...
├── pom.xml
└── README.md
```

:computer: Usage
To use this application, follow these steps:

- :wrench: Environment Setup and Dependency Installation
- :arrow_forward: Running the Application


  :bulb: Sample API Usage
  
### :heavy_plus_sign: Creating a User
```
POST /users
Content-Type: application/json

{
    "email": "example@email.com",
    "firstName": "John",
    "lastName": "Doe",
    "birthDate": "2000-01-15",
    "address": "123 Main St",
    "phoneNumber": "+1234567890"
}
```

### :arrows_counterclockwise: Updating All User Fields

```
PUT /users/1
Content-Type: application/json

{
    "email": "new@email.com",
    "firstName": "New",
    "lastName": "Name",
    "birthDate": "1995-09-21",
    "address": "456 Elm St",
    "phoneNumber": "+9876543210"
}
```

### :mag: Searching for Users by Birth Date Range

```
GET /users/by-birthDate-interval?from=1995-01-01&to=2000-01-01
```

### :x: Deleting a User

```
DELETE /users/1
```

## :bust_in_silhouette: Author
## Andrii First
