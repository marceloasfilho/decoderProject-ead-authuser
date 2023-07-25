package com.ead.authuser.controller;

import com.ead.authuser.dto.UserDTO;
import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.enums.UserType;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {

        if (this.userService.existsByUsername(userDTO.getUsername())){
            return new ResponseEntity<>("ERROR: Username already taken!", HttpStatus.CONFLICT);
        }

        if (this.userService.existsByEmail(userDTO.getEmail())){
            return new ResponseEntity<>("ERROR: Email already taken!", HttpStatus.CONFLICT);
        }

        var userModel = new UserModel();
        BeanUtils.copyProperties(userDTO, userModel);
        userModel.setUserStatus(UserStatus.ACTIVE);
        userModel.setUserType(UserType.STUDENT);
        userModel.setCreationDateTime(LocalDateTime.now(ZoneId.of("UTC")));
        userModel.setLastUpdateDateTime(LocalDateTime.now(ZoneId.of("UTC")));
        this.userService.save(userModel);
        return new ResponseEntity<>(userModel, HttpStatus.CREATED);
    }
}
