package com.ead.authuser.controller;

import com.ead.authuser.dto.InstructorDTO;
import com.ead.authuser.enums.UserType;
import com.ead.authuser.model.UserModel;
import com.ead.authuser.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/instructor")
@RequiredArgsConstructor
public class InstructorController {

    private final UserService userService;

    @PostMapping("/assign")
    public ResponseEntity<?> assignUserToInstructor(@RequestBody @Valid InstructorDTO instructorDTO) {
        Optional<UserModel> userById = this.userService.findById(instructorDTO.getUserId());

        if (userById.isEmpty()) {
            return new ResponseEntity<>("ERROR: User not found", HttpStatus.NOT_FOUND);
        }

        UserModel userModel = userById.get();
        userModel.setUserType(UserType.INSTRUCTOR);
        userModel.setLastUpdateDateTime(LocalDateTime.now(ZoneId.of("UTC")));
        this.userService.save(userModel);
        return new ResponseEntity<>(userModel, HttpStatus.OK);
    }
}