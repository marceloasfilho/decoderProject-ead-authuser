package com.ead.authuser.controller;

import com.ead.authuser.client.UserClient;
import com.ead.authuser.dto.CourseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserCourseController {

    private final UserClient userClient;

    @GetMapping("/{userId}/courses")
    public ResponseEntity<Page<CourseDTO>> getAllCoursesByUser(
            @PageableDefault(sort = "courseId", direction = Sort.Direction.ASC) Pageable pageable,
            @PathVariable(value = "userId") UUID userId) {

        Page<CourseDTO> allCoursesByUser = this.userClient.getAllCoursesByUser(userId, pageable);
        return new ResponseEntity<>(allCoursesByUser, HttpStatus.OK);
    }
}
