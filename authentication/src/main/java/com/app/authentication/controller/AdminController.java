package com.app.authentication.controller;

import com.app.authentication.dto.RequestDto;
import com.app.authentication.dto.ResponseUser;
import com.app.authentication.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    AuthService authService;

    @GetMapping("/get-all-user")
    public ResponseEntity<List<ResponseUser>> getAllUser(){
        return ResponseEntity.status(HttpStatus.OK).body(authService.getAllUser());
    }
    
    @DeleteMapping("/delete-user")
    public ResponseEntity<Void> deleteUserById(@RequestParam Long id){
    	authService.deleteUserById(id);
    	return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    

    @PutMapping("/update-profile")
    public ResponseEntity<ResponseUser> updateProfile(@RequestParam Long id,@RequestBody RequestDto requestDto){
        return ResponseEntity.status(HttpStatus.OK).body(authService.updateProfile(id, requestDto));
    }

}
