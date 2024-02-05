package com.example.techblogplatformapi.repositories;


import com.example.techblogplatformapi.entity.Role;
import com.example.techblogplatformapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User,Integer> {

    Optional<User> findByEmail(String email);
    User findByRole(Role role);
}
