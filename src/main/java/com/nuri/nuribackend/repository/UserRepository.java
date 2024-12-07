package com.nuri.nuribackend.repository;

import com.nuri.nuribackend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT id FROM users", nativeQuery = true)
    List<Long> findAllUserIds();
}
