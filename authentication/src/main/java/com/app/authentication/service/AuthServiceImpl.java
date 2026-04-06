package com.app.authentication.service;

import com.app.authentication.dto.RequestDto;
import com.app.authentication.dto.ResponseDto;
import com.app.authentication.dto.ResponseUser;
import com.app.authentication.dto.SignInDto;
import com.app.authentication.entity.User;

import java.util.List;

public interface AuthServiceImpl {
    public String signup(RequestDto requestDto);
    public ResponseDto signin(String email,String password);

    public boolean validateToken(String token);

    public ResponseUser getUserById(Long id);

    public ResponseUser getUserByEmail(String email);

    public ResponseUser updateProfile(Long id, RequestDto user);

    public void changePassword(Long id,String password);

    public void deactivateAccount(Long id);

    public List<ResponseUser> getAllUser();
}
