package com.joyned.reddit.service;



import com.joyned.reddit.exception.BadRequestException;
import com.joyned.reddit.exception.NotFoundException;
import com.joyned.reddit.converter.RedditConverter;
import com.joyned.reddit.dto.UserDto;
import com.joyned.reddit.dto.request.LoginRequestDto;
import com.joyned.reddit.dto.request.RegistrationRequestDto;
import com.joyned.reddit.model.User;
import com.joyned.reddit.repository.TopicRepository;
import com.joyned.reddit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final TopicRepository topicRepository;
    private final RedditConverter redditConverter;
    public List<UserDto> getUsers() {
        return userRepository.findAll().stream().map(user -> redditConverter.convertToUserDTO(user)).toList();
    }

    public UserDto getUserByEmail(String email) throws Exception {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("user not found"));
        return redditConverter.convertToUserDTO(user);
    }
    public void registerUser(RegistrationRequestDto userToAdd) throws Exception {
        var user = userRepository.findByEmail(userToAdd.getEmail());
        if (!user.isPresent()){
            addUser(userToAdd);
        }
        else{
            log.error("User login attempt hasn't been successfully");
            throw new BadRequestException("User login attempt been not successfully");
        }
    }

    public void addUser(RegistrationRequestDto userToAdd) {
        var user = User.builder()
                .firstName(userToAdd.getFirstName())
                .lastName(userToAdd.getLastName())
                .email(userToAdd.getEmail())
                .password(hashPassword(userToAdd.getPassword()))
                .lastConnectivity(LocalDate.now())
                .build();
        userRepository.save(user);
    }

    public void addUserToTopic(Long topicId, Long userId) throws RuntimeException {
    try {
        var existingUser=userRepository.findById(userId).orElseThrow(()->new NotFoundException("User not found"));
        existingUser.setLastConnectivity(LocalDate.now());
        var existingTopic=topicRepository.findById(topicId).orElseThrow(()->new NotFoundException("Topic not found"));
        if (existingTopic.getUsers()==null){
            existingTopic.setUsers(new ArrayList<User>());
        }
        existingTopic.getUsers().add(existingUser);
        topicRepository.save(existingTopic);
    } catch (Exception e) {
        log.error("Failed to add user to topic", e);
        throw new RuntimeException("Failed to add user to topic", e);
    }
    }
    public void verifyUser(LoginRequestDto userToVerify) throws Exception {
       var user = userRepository.findByEmail(userToVerify.getEmail()).orElseThrow(()->new NotFoundException("User not found"));
       if (verifyPassword(user, userToVerify.getPassword())){
           user.setLastConnectivity(LocalDate.now());
           log.info("user verified: ", user);
       }
       else{
           log.error("user not verified successfully");
           throw new BadRequestException("user not verified successfully");
       }
    }

    public String hashPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        var encodedPassword= encoder.encode(password);
        return encodedPassword;
    }

    public boolean verifyPassword(User user, String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(password, user.getPassword());
    }
}


