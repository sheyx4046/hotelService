package com.example.hotel_thymeleaf_security.service.user;

import com.example.hotel_thymeleaf_security.dto.request.AuthDto;
import com.example.hotel_thymeleaf_security.dto.request.ForgotDto;
import com.example.hotel_thymeleaf_security.dto.request.UserRequestDto;
import com.example.hotel_thymeleaf_security.entity.user.UserEntity;
import com.example.hotel_thymeleaf_security.entity.user.Role;
import com.example.hotel_thymeleaf_security.exception.DataNotFoundException;
import com.example.hotel_thymeleaf_security.exception.VerificationPassword;
import com.example.hotel_thymeleaf_security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


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
        UserEntity userEntity = modelMapper.map(userRequestDto, UserEntity.class);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userEntity.setRoles(Role.USER);
        return userRepository.save(userEntity);
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
        UserEntity userEntity = userRepository.findByEmail(auth.getEmail())
                .orElseThrow(() -> new DataNotFoundException("user not found"));
        if (passwordEncoder.matches(auth.getPassword(), userEntity.getPassword())) {
            return userEntity;
        }
        throw new DataNotFoundException("username/password is wrong");
    }

    @Override
    public UserEntity getByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(()-> new DataNotFoundException("User by email not found"));
    }

    public UserEntity forgotPassowrd (ForgotDto forgotDto) {
        UserEntity userEntity = userEmail(forgotDto.getEmail());
        userEntity.setVerificationCode(generateVerificationCode());
        if (forgotDto.getCode().equals(userEntity.getVerificationCode())){
            if(forgotDto.getNewPas().equals(forgotDto.getNewPas2())){
                userEntity.setPassword(passwordEncoder.encode(forgotDto.getNewPas()));
            } else {
                new VerificationPassword("passwords are not equal");
            }
        } else {
            new VerificationPassword("verification is not equal");
        }
        mailService.sendMessage(userEntity);
        return userRepository.save(userEntity);
    }

    private String generateVerificationCode() {
        return String.valueOf(random.nextInt(1000000));
    }

    private UserEntity userEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new DataNotFoundException("user not found"));
    }
}
