package com.ead.authuser.service.impl;

import com.ead.authuser.service.UtilsService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UtilsServiceImpl implements UtilsService {
    private static final String REQUEST_URI = "http://localhost:8082";

    @Override
    public String buildUrl(UUID userId, Pageable pageable) {
        return REQUEST_URI +
                "/course?userId=" +
                userId +
                "&page=" +
                pageable.getPageNumber() +
                "&size=" +
                pageable.getPageSize() +
                "&sort=" +
                pageable.getSort().toString().replaceAll(": ", ",");
    }
}
