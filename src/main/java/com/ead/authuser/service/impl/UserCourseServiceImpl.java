package com.ead.authuser.service.impl;

import com.ead.authuser.model.UserCourseModel;
import com.ead.authuser.model.UserModel;
import com.ead.authuser.repository.UserCourseRepository;
import com.ead.authuser.service.UserCourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserCourseServiceImpl implements UserCourseService {
    private final UserCourseRepository userCourseRepository;

    @Override
    public boolean existsByUserAndCourseId(UserModel userModel, UUID courseId) {
        return this.userCourseRepository.existsByUserModelAndCourseId(userModel, courseId);
    }

    @Override
    public UserCourseModel save(UserCourseModel userCourseModel) {
        return this.userCourseRepository.save(userCourseModel);
    }

    @Override
    public boolean existsByCourseId(UUID courseId) {
        return this.userCourseRepository.existsByCourseId(courseId);
    }

    @Override
    @Transactional
    public void deleteUserCourseByCourseId(UUID courseId) {
        this.userCourseRepository.deleteAllByCourseId(courseId);
    }
}
