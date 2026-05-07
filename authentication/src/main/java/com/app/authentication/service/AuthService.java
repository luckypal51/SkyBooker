package com.app.authentication.service;

import com.app.authentication.config.UserDetail;
import com.app.authentication.dto.RequestDto;
import com.app.authentication.dto.ResponseDto;
import com.app.authentication.dto.ResponseUser;
import com.app.authentication.dto.SignInDto;
import com.app.authentication.entity.User;
import com.app.authentication.exception.AuthenticationException;
import com.app.authentication.exception.ResouceNotFoundException;
import com.app.authentication.repository.UserRepository;
import com.app.authentication.util.ConstantValue;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
              throw new AuthenticationException(ConstantValue.USER_EXISTS);
          }
          requestDto.setPassword(encoder.encode(requestDto.getPassword()));
          userRepository.save(converToUser(requestDto));
          return ConstantValue.SUCCESSFULLY_ADDED_USER+requestDto.getFullName();
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
        responseDto.setMessage(ConstantValue.GENERATED_TOKEN);
        return responseDto;
    }

    @Override
    public boolean validateToken(String token) {
        UserDetails userDetails = userDetail.loadUserByUsername(jwtService.extractUsername(token));
        return jwtService.validateToken(token,userDetails);
    }

    @Override
    public ResponseUser getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(()->new ResouceNotFoundException(ConstantValue.USER_NOT_FOUND_ID+id));
        return convertUserToResonse(user);
    }

    @Override
    public ResponseUser getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(()->new ResouceNotFoundException(ConstantValue.USER_NOT_FOUND_EMAIL+email));
        return convertUserToResonse(user);
    }

    @Override
    public ResponseUser updateProfile(Long id, RequestDto user) {

        User u = userRepository.findById(id)
                .orElseThrow(() -> new ResouceNotFoundException(
                        ConstantValue.USER_NOT_FOUND_ID + id));

        u.setEmail(user.getEmail());
        u.setFullName(user.getFullName());
        u.setNationality(user.getNationality());
        u.setPassportNumber(user.getPassportNumber());
        u.setPhone(user.getPhone());
        u.setRole(user.getRole());

        // Safe password update
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            u.setPasswordHash(encoder.encode(user.getPassword()));
        }

        userRepository.save(u);

        return convertUserToResonse(u);
    }

    @Override
    public void changePassword(Long id, String password) {
        User user = userRepository.findById(id).orElseThrow(()->new ResouceNotFoundException(ConstantValue.USER_NOT_FOUND_ID+id));
        user.setPasswordHash(encoder.encode(password));
        userRepository.save(user);
    }

    @Override
    public void deactivateAccount(Long id) {
        User user = userRepository.findById(id).orElseThrow(()->new ResouceNotFoundException(ConstantValue.USER_NOT_FOUND_ID+id));
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
    
    public void deleteUserById(Long userId) {
    	userRepository.deleteById(userId);
    }

    private User converToUser(RequestDto requestDto){
        User user = new User();
        user.setIsActive(requestDto.getActive());
        user.setEmail(requestDto.getEmail());
        user.setPhone(requestDto.getPhone());
        user.setNationality(requestDto.getNationality());
        user.setFullName(requestDto.getFullName());
        user.setProvider(requestDto.getProvider());
        user.setRole(requestDto.getRole());
        user.setPasswordHash(requestDto.getPassword());
        user.setPassportNumber(requestDto.getPassportNumber());
        user.setCreatedAt(LocalDate.now());
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
                user.getNationality()
                );
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
