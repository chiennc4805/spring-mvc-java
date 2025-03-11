package vn.hoidanit.laptopshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.hoidanit.laptopshop.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User save(User eric);

    List<User> findAll();

    User findById(long id);

    List<User> findByEmail(String email);

    void deleteById(long id);
}
