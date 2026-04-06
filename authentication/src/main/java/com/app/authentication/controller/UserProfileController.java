package com.app.authentication.controller;

import com.app.authentication.dto.RequestDto;
import com.app.authentication.dto.ResponseUser;
import com.app.authentication.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserProfileController {

    @Autowired
    AuthService authService;


    @PutMapping("/update-profile")
    public ResponseEntity<ResponseUser> updateProfile(@RequestParam Long id,@RequestBody RequestDto requestDto){
        return ResponseEntity.status(HttpStatus.OK).body(authService.updateProfile(id, requestDto));
    }

    @PatchMapping("/deactivate-account")
    public void deactivateAccount(@RequestParam Long id){
        authService.deactivateAccount(id);
    }
}
