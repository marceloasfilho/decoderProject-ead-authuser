package com.ead.authuser.controller;

import com.ead.authuser.dto.UserDTO;
import com.ead.authuser.model.UserModel;
import com.ead.authuser.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/list")
    public ResponseEntity<Page<UserModel>> getAllUsers(@PageableDefault(sort = "userId", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<UserModel> userModelPage = this.userService.findAll(pageable);
        return new ResponseEntity<>(userModelPage, HttpStatus.OK);
    }

    @GetMapping("/getUser/{userId}")
    public ResponseEntity<UserModel> getUserById(@PathVariable(value = "userId") UUID userId) {
        Optional<UserModel> userById = this.userService.findById(userId);
        return userById.map(userModel -> new ResponseEntity<>(userModel, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/deleteUser/{userId}")
    public ResponseEntity<?> deleteUserById(@PathVariable(value = "userId") UUID userId) {
        Optional<UserModel> userById = this.userService.findById(userId);

        if (userById.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        this.userService.deleteById(userById.get().getUserId());
        return new ResponseEntity<>("user deleted successfully", HttpStatus.OK);
    }

    @PutMapping("/updateUser/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable(value = "userId") UUID userId, @RequestBody @JsonView(UserDTO.UserView.UpdateUser.class) @Validated(UserDTO.UserView.UpdateUser.class) UserDTO userDTO) {
        Optional<UserModel> userById = this.userService.findById(userId);

        if (userById.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        var userModel = userById.get();
        userModel.setFullName(userDTO.getFullName());
        userModel.setPhoneNumber(userDTO.getPhoneNumber());
        userModel.setCpf(userDTO.getCpf());
        userModel.setLastUpdateDateTime(LocalDateTime.now(ZoneId.of("UTC")));
        this.userService.save(userModel);

        return new ResponseEntity<>(userModel, HttpStatus.OK);
    }

    @PutMapping("/updatePassword/{userId}")
    public ResponseEntity<?> updatePassword(@PathVariable(value = "userId") UUID userId, @RequestBody @JsonView(UserDTO.UserView.UpdatePassword.class) @Validated(UserDTO.UserView.UpdatePassword.class) UserDTO userDTO) {
        Optional<UserModel> userById = this.userService.findById(userId);

        if (userById.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (!userById.get().getPassword().equals(userDTO.getOldPassword())) {
            return new ResponseEntity<>("ERROR: Password mismatch", HttpStatus.CONFLICT);
        }

        var userModel = userById.get();
        userModel.setPassword(userDTO.getPassword());
        userModel.setLastUpdateDateTime(LocalDateTime.now(ZoneId.of("UTC")));
        this.userService.save(userModel);

        return new ResponseEntity<>(userModel, HttpStatus.OK);
    }

    @PutMapping("/updateImageUrl/{userId}")
    public ResponseEntity<?> updateImageUrl(@PathVariable(value = "userId") UUID userId, @RequestBody @JsonView(UserDTO.UserView.UpdateImageUrl.class) @Validated(UserDTO.UserView.UpdateImageUrl.class) UserDTO userDTO) {
        Optional<UserModel> userById = this.userService.findById(userId);

        if (userById.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        var userModel = userById.get();
        userModel.setImageUrl(userDTO.getImageUrl());
        userModel.setLastUpdateDateTime(LocalDateTime.now(ZoneId.of("UTC")));
        this.userService.save(userModel);

        return new ResponseEntity<>(userModel, HttpStatus.OK);
    }
}
