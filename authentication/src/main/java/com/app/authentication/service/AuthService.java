package com.app.authentication.service;

import com.app.authentication.config.UserDetail;
import com.app.authentication.dto.RequestDto;
import com.app.authentication.dto.ResponseDto;
import com.app.authentication.dto.ResponseUser;
import com.app.authentication.dto.SignInDto;
import com.app.authentication.entity.User;
import com.app.authentication.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AuthService implements AuthServiceImpl {

    @Autowired
    JwtService jwtService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    UserDetail userDetail;

    @Autowired
    AuthenticationManager manager;

    public String signup(RequestDto requestDto){
          if(userRepository.existsByEmail(requestDto.getEmail())){
              throw new IllegalArgumentException("User Already exists");
          }
          requestDto.setPasswordHash(encoder.encode(requestDto.getPasswordHash()));
          userRepository.save(converToUser(requestDto));
          return "SuccessFully Added User "+requestDto.getFullName();
    }



    public ResponseDto signin(String email,String password){
        ResponseDto responseDto = new ResponseDto();
        manager.authenticate(new UsernamePasswordAuthenticationToken(email,password));
        UserDetails userDetails = userDetail.loadUserByUsername(email);
        String role = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("ROLE_USER");
        String token = jwtService.generateToken(userDetails.getUsername(),role);
        responseDto.setToken(token);
        log.debug(responseDto.getToken());
        responseDto.setMessage("Successfully Generated Token");
        return responseDto;
    }

    @Override
    public boolean validateToken(String token) {
        UserDetails userDetails = userDetail.loadUserByUsername(jwtService.extractUsername(token));
        return jwtService.validateToken(token,userDetails);
    }

    @Override
    public ResponseUser getUserById(Long id) {
        User user = userRepository.findById(id).get();
        return convertUserToResonse(user);
    }

    @Override
    public ResponseUser getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).get();
        return convertUserToResonse(user);
    }

    @Override
    public ResponseUser updateProfile(Long id, RequestDto user) {
        User u = userRepository.findById(id).get();
        User user1 = converToUser(user);
        user1.setUserId(u.getUserId());
        userRepository.save(user1);
        return convertUserToResonse(user1);
    }

    @Override
    public void changePassword(Long id, String password) {
        User user = userRepository.findById(id).get();
        user.setPasswordHash(encoder.encode(password));
        userRepository.save(user);
    }

    @Override
    public void deactivateAccount(Long id) {
        User user = userRepository.findById(id).get();
        user.setIsActive(false);
        userRepository.save(user);
    }

    @Override
    public List<ResponseUser> getAllUser() {
        List<User> list =  userRepository.findAll();
        List<ResponseUser>result = new ArrayList<>();
        for (User s: list){
            result.add(convertUserToResonse(s));
        }
        return result;
    }

    private User converToUser(RequestDto requestDto){
        User user = new User();
        user.setIsActive(requestDto.getIsActive());
        user.setEmail(requestDto.getEmail());
        user.setPhone(requestDto.getPhone());
        user.setNationality(requestDto.getNationality());
        user.setFullName(requestDto.getFullName());
        user.setProvider(requestDto.getProvider());
        user.setRole(requestDto.getRole());
        user.setPasswordHash(requestDto.getPasswordHash());
        user.setPassportNumber(requestDto.getPassportNumber());
        user.setCreatedAt(requestDto.getCreatedAt());
        return user;
    }

    private RequestDto convertToDto(User user){
        RequestDto requestDto = new RequestDto(user.getFullName(),
                user.getEmail(),
                user.getPasswordHash(),
                user.getPhone(),
                user.getRole(),
                user.getProvider(),
                user.getIsActive(),
                user.getPassportNumber(),
                user.getNationality(),
                user.getCreatedAt());
        return requestDto;
    }

    private ResponseUser convertUserToResonse(User user){
        return  new ResponseUser(user.getUserId(),
                  user.getFullName(),
                  user.getEmail(),
                  user.getPhone(),
                  user.getRole(),
                  user.getProvider(),
                  user.getIsActive(),
                  user.getPassportNumber(),
                  user.getNationality(),
                  user.getCreatedAt());
    }
}
