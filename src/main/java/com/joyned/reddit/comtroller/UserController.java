package com.joyned.reddit.comtroller;

import com.joyned.reddit.dto.UserDto;
import com.joyned.reddit.dto.request.LoginRequestDto;
import com.joyned.reddit.dto.request.RegistrationRequestDto;
import com.joyned.reddit.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@CrossOrigin
@Slf4j
public class UserController {
    private final UserService userService;


    @PostMapping("/login")
    public void login(@RequestBody @Valid LoginRequestDto req) throws Exception {
            userService.verifyUser(req);
    }

    @PostMapping("/registration")
    public void registration(@RequestBody @Valid RegistrationRequestDto req) throws Exception {
            userService.registerUser(req);
    }

    @PutMapping("/addUserToTopic/{topicId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void addUserToTopic(@PathVariable @Valid Long topicId, @RequestBody @Valid Long userId) {
        log.info("topicId:  " + topicId);
        log.info("userId:  " + userId);
        userService.addUserToTopic(topicId, userId);
    }
    @GetMapping("/GetUserByEmail/{email}")
    public UserDto GetUserByEmail(@PathVariable @Valid String email) throws Exception {
            return userService.getUserByEmail(email);
    }

    @GetMapping("/getUsers")
    public List<UserDto> GetUsers() {
        return userService.getUsers();
    }

}