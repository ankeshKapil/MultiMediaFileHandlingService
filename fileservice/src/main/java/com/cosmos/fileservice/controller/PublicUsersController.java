package com.cosmos.fileservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cosmos.fileservice.domain.User;
import com.cosmos.fileservice.service.UserAuthenticationService;
import com.cosmos.fileservice.service.UserCrudService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/public/users")
@FieldDefaults(makeFinal = true)
@AllArgsConstructor
@Api(value="User Mangement")
final class PublicUsersController {
  @NonNull
  @Autowired
  UserAuthenticationService authentication;
  @NonNull
  UserCrudService users;

  @ApiOperation(value="register new user to the server",hidden=true)
 
  @PostMapping("/register")
  String register(
    @RequestParam("username") final String username,
    @RequestParam("password") final String password) {
    users
      .save(
        User.builder()
          .username(username)
          .password(password)
          .build()
      );

    return login(username, password);
  }

  @ApiOperation(value="lets you login to the server using username and password and returns a jwt token")
  @PostMapping("/login")
  String login(
    @RequestParam("username") final String username,
    @RequestParam("password") final String password) {
    return authentication
      .login(username, password)
      .orElseThrow(() -> new RuntimeException("invalid login and/or password"));
  }
}



