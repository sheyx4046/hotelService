package com.example.hotel_thymeleaf_security.service.user.userService;

import com.example.hotel_thymeleaf_security.entity.dtos.AuthDto;
import com.example.hotel_thymeleaf_security.entity.dtos.NewPasswordDto;
import com.example.hotel_thymeleaf_security.entity.dtos.UserDetailsDto;
import com.example.hotel_thymeleaf_security.entity.dtos.UserDto;
import com.example.hotel_thymeleaf_security.entity.dtos.request.UserRequestDto;
import com.example.hotel_thymeleaf_security.entity.user.Role;
import com.example.hotel_thymeleaf_security.entity.user.States;
import com.example.hotel_thymeleaf_security.entity.user.UserEntity;
import com.example.hotel_thymeleaf_security.exception.DataNotFoundException;
import com.example.hotel_thymeleaf_security.exception.UniqueObjectException;
import com.example.hotel_thymeleaf_security.repository.userRepository.UserRepository;
import com.example.hotel_thymeleaf_security.service.mailService.MailServiceImpl;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final MailServiceImpl mailService;

    @Override
    public UserEntity create(UserRequestDto userRequestDto, Boolean sendMessage ) {
        try{
        UserEntity userEntity = modelMapper.map(userRequestDto, UserEntity.class);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userEntity.setState(States.UNVERIFIED);
        UserEntity user = userRepository.save(userEntity);
        if(sendMessage){
            mailService.sendMessageToUser(userEntity.getEmail());
        }
        return user;
        } catch (Exception e){
            System.out.println("UserCreating Exception: "+e);
            return null;
        }
    }

    @Override
    public void sendForgotPassword(String email) throws MessagingException, UnsupportedEncodingException {
        UserEntity userEntity = getByEmail(email);
        mailService.sendForgotPassword(email);
    }


    @Override
    public UserEntity create(UserRequestDto userRequestDto) {
        try{
            UserEntity userEntity = modelMapper.map(userRequestDto, UserEntity.class);
            userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
            userEntity.setState(States.UNVERIFIED);
            return userRepository.save(userEntity);
        } catch (Exception e){
            System.out.println("UserCreating Exception: "+e);
            return null;
        }
    }

    @Override
    public UserEntity getById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("user not found"));
    }

    @Override
    public UserEntity update(UserRequestDto userRequestDto, UUID id) {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new DataNotFoundException("User_ID not found"));
        user.setName(userRequestDto.getName());
        user.setEmail(userRequestDto.getEmail());
        user.setPassword(userRequestDto.getPassword());
        user.setUpdatedDate(LocalDateTime.now());
        return userRepository.save(user);
    }

    @Override
    public void deleteById(UUID id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserEntity login(AuthDto auth) {
        UserEntity userEntity = getByEmail(auth.getUsername());
        if(passwordEncoder.matches(auth.getPassword(), userEntity.getPassword())){
            return userEntity;
        }return null;
    }

    @Override
    public UserEntity getByEmail(String email) {
        Optional<UserEntity> byEmail = userRepository.findByEmail(email);
        return byEmail.orElse(null);
    }


    @Override
    public UserEntity forgotPassword(String username, UUID userId, String newPassword) {
        UserEntity userEntity = getById(userId);
        if(userEntity.getName().equals(username)){
            userEntity.setPassword(passwordEncoder.encode(newPassword));
            userEntity.setUpdatedDate(LocalDateTime.now());
            return userRepository.save(userEntity);
        }
        return null;
    }

    @Override
    public UserEntity forgotPassword(String username, UUID userId, NewPasswordDto newPassword) {
        UserEntity user = getById(userId);
        if((user.getEmail().equals(username) && user.getId().equals(userId) &&
                (newPassword.getNewPassword1().equals(newPassword.getNewPassword2()) && checkPassword(newPassword.getPassword(), userId)))){
                user.setPassword(passwordEncoder.encode(newPassword.getNewPassword2()));
                user.setUpdatedDate(LocalDateTime.now());
                return userRepository.save(user);
        }
        return null;
    }

    @Override
    public UserEntity update(UserDto userDto, UUID userId) {
        UserEntity user = getById(userId);
        try{
        if(!userDto.getPassword().equals(user.getPassword())){
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setState(userDto.getState());
        user.setRole(userDto.getRoles());
        user.setUpdatedDate(LocalDateTime.now());
        return userRepository.save(user);
        } catch (Exception e){
            System.out.println("UserUpdating Exception: "+e);
            return null;
        }
    }

    @Override
    public Boolean verifyCode(UUID userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new DataNotFoundException("User not found"));
        Duration duration = Duration.between(user.getUpdatedDate(), LocalDateTime.now());
        if(duration.toMinutes()<=5){
            user.setState(States.ACTIVE);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public Boolean newVerifyCode(String email) throws MessagingException, UnsupportedEncodingException {
        UserEntity user = getByEmail(email);
        user.setUpdatedDate(LocalDateTime.now());
        userRepository.save(user);
        mailService.sendMessageToUser(user.getEmail());
        return true;
    }

    @Override
    public Page<UserEntity> getAllPage(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();

        List<UserEntity> all = userRepository.findAll();

        int startItem = currentPage * pageSize;
        List<UserEntity> list;

        if (startItem >= all.size()) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, all.size());
            list = all.subList(startItem, toIndex);
        }

        return new PageImpl<>(list, pageable, all.size());
    }

    @Override
    public UserEntity getLastUser() {
        List<UserEntity> all = userRepository.findAll();
        return all.get(all.size()-1);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getByEmail(username);
    }

    @Override
    public UserEntity saveByAdmin(UserRequestDto userRequestDto, String name) {
        UserEntity admin = getByEmail(name);
        switch (admin.getRole()){
            case ADMIN -> {
                if(userRequestDto.getRoles()== Role.SUPER_ADMIN || userRequestDto.getRoles() ==Role.ADMIN){
                    return null;
                } else if (userRequestDto.getRoles()== Role.MANAGER || userRequestDto.getRoles() ==Role.USER) {
                    return create(userRequestDto);
                }
            }
            case SUPER_ADMIN -> {
                return create(userRequestDto);
            }
            default -> {
                return null;
            }
        }
        return null;
    }

    @Override
    public UserEntity update(UserDetailsDto userDto, UUID userId) {
        UserEntity byEmail = getById(userId);
        if(userRepository.findByEmail(userDto.getEmail()).isEmpty() ){
            byEmail.setEmail(userDto.getEmail());
            byEmail.setUsername(userDto.getUsername());
            return userRepository.save(byEmail);
        } else if (byEmail.getEmail().equals(userDto.getEmail())){
            byEmail.setName(userDto.getUsername());
            return userRepository.save(byEmail);
        }
        return null;
    }


    private Boolean checkState(String email){
        UserEntity userEntity = getByEmail(email);
        switch (userEntity.getState()){
            case ACTIVE,BLOCKED ->{
                return true;
            }
            case UNVERIFIED -> {
                return false;
            }
        }
        return false;
    }

    private Boolean checkPassword(String password, UUID userId){
        UserEntity user = getById(userId);
        if(passwordEncoder.matches(password, user.getPassword())){
            return true;
        }
        return false;
    }


    private UserEntity registered(Principal principal){
         return getByEmail(principal.getName());
    }
}
