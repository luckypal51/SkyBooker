package com.app.authentication.controller;

import com.app.authentication.dto.ResponseUser;
import com.app.authentication.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    AuthService authService;

    @GetMapping("/get-all-user")
    public ResponseEntity<List<ResponseUser>> getAllUser(){
        return ResponseEntity.status(HttpStatus.FOUND).body(authService.getAllUser());
    }
}
