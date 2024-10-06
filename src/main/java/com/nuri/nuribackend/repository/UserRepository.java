package com.nuri.nuribackend.repository;

import com.nuri.nuribackend.domain.User;
import com.nuri.nuribackend.dto.User.UserPostRequest;
import com.nuri.nuribackend.dto.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
}
