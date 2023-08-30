package com.ead.authuser.service;

import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UtilsService {
    String buildUrl(UUID userId, Pageable pageable);
    String createUrlDeleteCourseUserIntoCourse(UUID userId);
}
