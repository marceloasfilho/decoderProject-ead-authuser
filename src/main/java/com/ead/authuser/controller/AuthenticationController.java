package com.ead.authuser.controller;

import com.ead.authuser.dto.UserDTO;
import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.enums.UserType;
import com.ead.authuser.model.UserModel;
import com.ead.authuser.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody @JsonView(UserDTO.UserView.RegisterUser.class) @Validated(UserDTO.UserView.RegisterUser.class) UserDTO userDTO) {

        if (this.userService.existsByUsername(userDTO.getUsername())) {
            return new ResponseEntity<>("ERROR: Username already taken!", HttpStatus.CONFLICT);
        }

        if (this.userService.existsByEmail(userDTO.getEmail())) {
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

    @GetMapping("/")
    public String index() {
        log.trace("TRACE - GRANULARIDADE FINA, MUITOS DETALHES");
        log.debug("DEBUG - AMBIENTE DE DSV, IMPLEMENTAÇÃO DE CÓDIGO, VALORES DE VARIÁVEIS, ETC.");
        log.info("INFO - AMBIENTE DE PRD, INFORMAÇÕES DE PROCESSOS RELEVANTE PARA ARQUITETURA, SEM MUITA GRANULARIDADE");
        log.warn("WARN - PERDA DE DADOS SECUNDÁRIOS, PROCESSOS QUE OCORREM MAIS DE UMA VEZ (NÃO CHEGA A SER CONSIDERADO UM ERRO");
        log.error("ERROR - MUITO UTILIZADO EM BLOCOS TRY CATCH, ESTRUTURAR ERROS NO SISTEMA");
        return "Logging Spring Boot...";
    }
}
