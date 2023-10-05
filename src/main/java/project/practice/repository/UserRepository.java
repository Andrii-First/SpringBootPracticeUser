package project.practice.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import project.practice.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findUsersByBirthDateBetween(LocalDate from, LocalDate to);

}
