package com.ead.authuser.controller;

import com.ead.authuser.models.UserModel;
import com.ead.authuser.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserModel>> getAllUsers() {
        return new ResponseEntity<>(this.userService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserModel> getUserById(@PathVariable(value = "userId") UUID userId) {
        Optional<UserModel> userById = this.userService.findById(userId);
        return userById.map(userModel -> new ResponseEntity<>(userModel, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUserById(@PathVariable(value = "userId") UUID userId) {
        Optional<UserModel> userById = this.userService.findById(userId);

        if (userById.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        this.userService.deleteById(userById.get().getUserId());
        return new ResponseEntity<>("user deleted successfully", HttpStatus.OK);
    }
}
