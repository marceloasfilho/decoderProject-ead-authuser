package com.ead.authuser.controller;

import com.ead.authuser.dto.UserDTO;
import com.ead.authuser.model.UserModel;
import com.ead.authuser.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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

import static com.ead.authuser.specification.SpecificationTemplate.UserSpec;
import static com.ead.authuser.specification.SpecificationTemplate.userCourseId;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/list")
    public ResponseEntity<Page<UserModel>> getAllUsers(
            UserSpec userSpec,
            @PageableDefault(sort = "userId", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(required = false) UUID courseId) {

        Page<UserModel> userModelPage;

        if (courseId != null) {
            userModelPage = this.userService.findAll(userCourseId(courseId).and(userSpec), pageable);
        } else {
            userModelPage = this.userService.findAll(userSpec, pageable);
        }

        if (!userModelPage.isEmpty()) {
            for (UserModel userModel : userModelPage.toList()) {
                userModel.add(linkTo(methodOn(UserController.class).getUserById(userModel.getUserId())).withSelfRel());
            }
        }

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

        this.userService.delete(userById.get());
        return new ResponseEntity<>("user deleted successfully", HttpStatus.OK);
    }

    @PutMapping("/updateUser/{userId}")
    public ResponseEntity<?> updateUser(
            @PathVariable(value = "userId") UUID userId,
            @RequestBody @JsonView(UserDTO.UserView.UpdateUser.class) @Validated(UserDTO.UserView.UpdateUser.class) UserDTO userDTO) {

        log.debug("PUT updateUser userId saved {}", userDTO.getUserId());
        Optional<UserModel> userById = this.userService.findById(userId);

        if (userById.isEmpty()) {
            log.warn("User {} not found", userId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        var userModel = userById.get();
        userModel.setFullName(userDTO.getFullName());
        userModel.setPhoneNumber(userDTO.getPhoneNumber());
        userModel.setCpf(userDTO.getCpf());
        userModel.setLastUpdateDateTime(LocalDateTime.now(ZoneId.of("UTC")));
        this.userService.save(userModel);

        log.debug("PUT registerUser userId saved {}", userModel.getUserId());
        log.info("User {} saved successfully", userModel.getUserId());

        return new ResponseEntity<>(userModel, HttpStatus.OK);
    }

    @PutMapping("/updatePassword/{userId}")
    public ResponseEntity<?> updatePassword(
            @PathVariable(value = "userId") UUID userId,
            @RequestBody @JsonView(UserDTO.UserView.UpdatePassword.class) @Validated(UserDTO.UserView.UpdatePassword.class) UserDTO userDTO) {
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
    public ResponseEntity<?> updateImageUrl(
            @PathVariable(value = "userId") UUID userId,
            @RequestBody @JsonView(UserDTO.UserView.UpdateImageUrl.class) @Validated(UserDTO.UserView.UpdateImageUrl.class) UserDTO userDTO) {
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
