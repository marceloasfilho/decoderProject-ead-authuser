package com.ead.authuser.controller;

import com.ead.authuser.client.UserClient;
import com.ead.authuser.dto.CourseDTO;
import com.ead.authuser.dto.UserCourseDTO;
import com.ead.authuser.model.UserCourseModel;
import com.ead.authuser.model.UserModel;
import com.ead.authuser.service.UserCourseService;
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

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserCourseController {

    private final UserClient userClient;
    private final UserService userService;
    private final UserCourseService userCourseService;

    @GetMapping("/{userId}/courses")
    public ResponseEntity<Page<CourseDTO>> getAllCoursesByUser(
            @PageableDefault(sort = "courseId", direction = Sort.Direction.ASC) Pageable pageable,
            @PathVariable(value = "userId") UUID userId) {

        Page<CourseDTO> allCoursesByUser = this.userClient.getAllCoursesByUser(userId, pageable);
        return new ResponseEntity<>(allCoursesByUser, HttpStatus.OK);
    }

    @PostMapping("/{userId}/courses/enroll")
    public ResponseEntity<?> saveEnrollmentUserInCourse(@PathVariable(value = "userId") UUID userId,
                                                        @RequestBody @Valid UserCourseDTO userCourseDTO) {
        Optional<UserModel> userById = this.userService.findById(userId);
        if (userById.isEmpty()) {
            return new ResponseEntity<>("ERROR: User not found", HttpStatus.NOT_FOUND);
        }

        if (this.userCourseService.existsByUserAndCourseId(userById.get(), userCourseDTO.getCourseId())) {
            return new ResponseEntity<>("ERROR: User already enrolled to this course", HttpStatus.CONFLICT);
        }

        var userCourseModel = new UserCourseModel();
        userCourseModel.setUserModel(userById.get());
        userCourseModel.setCourseId(userCourseDTO.getCourseId());

        UserCourseModel saved = this.userCourseService.save(userCourseModel);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }
}
