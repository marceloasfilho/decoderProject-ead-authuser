package com.ead.authuser.service.impl;

import com.ead.authuser.model.UserCourseModel;
import com.ead.authuser.model.UserModel;
import com.ead.authuser.repository.UserCourseRepository;
import com.ead.authuser.repository.UserRepository;
import com.ead.authuser.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserCourseRepository userCourseRepository;

    @Override
    public Optional<UserModel> findById(UUID userId) {
        return this.userRepository.findById(userId);
    }

    @Override
    public void save(UserModel userModel) {
        this.userRepository.save(userModel);
    }

    @Override
    public boolean existsByUsername(String username) {
        return this.userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return this.userRepository.existsByEmail(email);
    }

    @Override
    public Page<UserModel> findAll(Specification<UserModel> specification, Pageable pageable) {
        return this.userRepository.findAll(specification, pageable);
    }

    @Override
    public void delete(UserModel userModel) {
        List<UserCourseModel> allUserCourseIntoUser = this.userCourseRepository.findAllUserCourseIntoUser(userModel.getUserId());
        if (!allUserCourseIntoUser.isEmpty()) {
            this.userCourseRepository.deleteAll(allUserCourseIntoUser);
        }
        this.userRepository.delete(userModel);
    }
}
