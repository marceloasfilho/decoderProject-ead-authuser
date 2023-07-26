package com.ead.authuser.service;

import com.ead.authuser.model.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    List<UserModel> findAll();

    Optional<UserModel> findById(UUID userId);

    void deleteById(UUID userId);

    void save(UserModel userModel);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Page<UserModel> findAll(Specification<UserModel> specification, Pageable pageable);
}
