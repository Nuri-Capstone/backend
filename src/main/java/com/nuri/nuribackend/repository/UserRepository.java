package com.nuri.nuribackend.repository;

import com.nuri.nuribackend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT u.id FROM User u", nativeQuery = true)
    List<Long> findAllUserIds();

    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);

}
