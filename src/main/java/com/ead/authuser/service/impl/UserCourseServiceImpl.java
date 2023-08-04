package com.ead.authuser.service.impl;

import com.ead.authuser.repository.UserCourseRepository;
import com.ead.authuser.service.UserCourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCourseServiceImpl implements UserCourseService {
    private final UserCourseRepository userCourseRepository;
}
