package com.nuri.nuribackend.repository;

import com.nuri.nuribackend.domain.User;
import com.nuri.nuribackend.dto.User.UserPostRequest;
import com.nuri.nuribackend.dto.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
