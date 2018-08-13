package com.cosmos.fileservice.controller;

import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cosmos.fileservice.domain.User;
import com.cosmos.fileservice.service.UserAuthenticationService;

import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

@ApiOperation(value="Secured user API",hidden=true)
@RestController
@RequestMapping("/users")
@FieldDefaults(level = PRIVATE, makeFinal = true)
@AllArgsConstructor(access = PACKAGE)
final class SecuredUsersController {
  @NonNull
  UserAuthenticationService authentication;

  @GetMapping("/current")
  User getCurrent(@AuthenticationPrincipal final User user) {
    return user;
  }

  @ApiOperation(value="logout not required from server end",hidden=true)
  @GetMapping("/logout")
  boolean logout(@AuthenticationPrincipal final User user) {
    authentication.logout(user);
    return true;
  }
}