package com.ead.authuser.controller;

import com.ead.authuser.client.CourseClient;
import com.ead.authuser.dto.CourseDTO;
import com.ead.authuser.model.UserModel;
import com.ead.authuser.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserCourseController {

    private final CourseClient courseClient;
    private final UserService userService;

    @GetMapping("/{userId}/courses")
    public ResponseEntity<?> getAllCoursesByUser(
            @PageableDefault(sort = "courseId", direction = Sort.Direction.ASC) Pageable pageable,
            @PathVariable(value = "userId") UUID userId) {

        Optional<UserModel> userById = this.userService.findById(userId);
        if (userById.isEmpty()) {
            return new ResponseEntity<>("ERROR: User not found", HttpStatus.NOT_FOUND);
        }

        Page<CourseDTO> allCoursesByUser = this.courseClient.getAllCoursesByUser(userId, pageable);
        return allCoursesByUser.isEmpty() ?
                new ResponseEntity<>("User is not enrolled in any course", HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(allCoursesByUser, HttpStatus.OK);
    }
}
