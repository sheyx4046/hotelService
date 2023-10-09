package com.example.hotel_thymeleaf_security.service.user;

import com.example.hotel_thymeleaf_security.dto.request.AuthDto;
import com.example.hotel_thymeleaf_security.dto.request.ForgotDto;
import com.example.hotel_thymeleaf_security.dto.request.UserRequestDto;
import com.example.hotel_thymeleaf_security.entity.user.UserEntity;
import com.example.hotel_thymeleaf_security.entity.user.Role;
import com.example.hotel_thymeleaf_security.exception.DataNotFoundException;
import com.example.hotel_thymeleaf_security.exception.VerificationPassword;
import com.example.hotel_thymeleaf_security.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final Random random = new Random(100000);

    @Override
    public UserEntity create(UserRequestDto userRequestDto) {
        try{
        UserEntity userEntity = modelMapper.map(userRequestDto, UserEntity.class);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userEntity.setRoles(Role.USER);
        return userRepository.save(userEntity);}
        catch (Exception e){
            System.out.println("UserCreating Exception: "+e);
            return null;
        }
    }

    @Override
    public UserEntity getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("user not found"));
    }

    @Override
    public UserEntity update(UserRequestDto userRequestDto, Long id) {

        return null;
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserEntity login(AuthDto auth) {
        try{
        UserEntity userEntity = userRepository.findByEmail(auth.getEmail())
                .orElseThrow(() -> new DataNotFoundException("user not found"));
        if (passwordEncoder.matches(auth.getPassword(), userEntity.getPassword())) {
            return userEntity;
        }}catch (DataNotFoundException e){
            System.out.println("Login password or email uncorrect");
            return null;
        }
        return null;
    }

    @Override
    public UserEntity getByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(()-> new DataNotFoundException("User by email not found"));
    }

    @Override
    public UserEntity forgotPassword(ForgotDto forgotDto) {
        UserEntity userEntity = getByEmail(forgotDto.getEmail());
        userEntity.setPassword(forgotDto.getNewPassword());
        userEntity.setUpdatedDate(LocalDateTime.now());
        return userRepository.save(userEntity);
    }

    private String generateVerificationCode() {
        return String.valueOf(random.nextInt(1000000));
    }

}
