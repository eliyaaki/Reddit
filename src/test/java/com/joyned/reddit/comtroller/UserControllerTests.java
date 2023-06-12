package com.joyned.reddit.comtroller;


import com.joyned.reddit.dto.UserDto;
import com.joyned.reddit.dto.request.LoginRequestDto;
import com.joyned.reddit.dto.request.RegistrationRequestDto;
import com.joyned.reddit.service.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class UserControllerTests {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testLogin_Success() throws Exception {
        // Arrange
        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .email("testuser@gmail.com")
                .password("password")
                .build();


        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginRequestDto)))
                .andExpect(status().isOk());

        // Verify that the userService method was called
        verify(userService).verifyUser(loginRequestDto);
    }

    @Test
    public void testLogin_Failure() throws Exception {
        // Arrange
        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .email("testuser@gmail.com")
                .password("password")
                .build();
        doThrow(new Exception("user not verified successfully")).when(userService).verifyUser(Mockito.any(LoginRequestDto.class));


        // Act and Assert
        Exception exception = assertThrows(Exception.class, () -> {
            userController.login(loginRequestDto);
        });

        // Assert specific exception details if needed
        assertEquals("user not verified successfully", exception.getMessage());

        // Verify that the userService method was called
        verify(userService).verifyUser(loginRequestDto);
    }



    @Test
    public void testRegistration_Success() throws Exception {
        // Arrange
        RegistrationRequestDto registrationRequestDto = RegistrationRequestDto.builder()
                .firstName("testuser")
                .lastName("testuser last name")
                .email("test@example.com")
                .password("password")
                .build();

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/user/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(registrationRequestDto)))
                .andExpect(status().isOk());

        // Verify that the userService method was called
        verify(userService).registerUser(registrationRequestDto);
    }

    @Test
    public void testRegistration_Failure() throws Exception {
        // Arrange
        RegistrationRequestDto registrationRequestDto = RegistrationRequestDto.builder()
                .firstName("testuser")
                .lastName("testuser last name")
                .email("test@example.com")
                .password("password")
                .build();
        doThrow(new Exception("User login attempt been not successfully")).when(userService).registerUser(Mockito.any(RegistrationRequestDto.class));

        // Act and Assert
        Exception exception = assertThrows(Exception.class, () -> {
            userController.registration(registrationRequestDto);
        });

        // Assert specific exception details if needed
        assertEquals("User login attempt been not successfully", exception.getMessage());

        // Verify that the userService method was called
        verify(userService).registerUser(registrationRequestDto);
    }

    @Test
    public void testAddUserToTopic_Success() throws Exception {
        // Arrange
        Long topicId = 1L;
        Long userId = 1L;

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/user/addUserToTopic/{topicId}", topicId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userId)))
                .andExpect(status().isCreated());

        // Verify that the userService method was called
        verify(userService).addUserToTopic(topicId, userId);
    }
    @Test
    public void testAddUserToTopic_Failure() throws Exception {
        // Arrange
        Long topicId = 1L;
        Long userId = 1L;
        doThrow(new RuntimeException("Failed to add user to topic")).when(userService).addUserToTopic(topicId, userId);


        // Act and Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            userController.addUserToTopic(topicId, userId);
        });

        // Assert specific exception details if needed
        assertEquals("Failed to add user to topic", exception.getMessage());

        // Verify that the userService method was called
        verify(userService).addUserToTopic(topicId, userId);
    }

    @Test
    public void testGetUserByEmail_Success() throws Exception {
        // Arrange
        String email = "test@example.com";
        UserDto expectedUserDto = UserDto.builder()
                .id(1L)
                .email(email)
                .firstName("testuser")
                .lastName("testuser last name")
                .build();
        when(userService.getUserByEmail(email)).thenReturn(expectedUserDto);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/user/GetUserByEmail/{email}", email))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(expectedUserDto.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", Matchers.is(expectedUserDto.getEmail())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", Matchers.is(expectedUserDto.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", Matchers.is(expectedUserDto.getLastName())));

        // Verify that the userService method was called
        verify(userService).getUserByEmail(email);
    }

    @Test
    public void testGetUserByEmail_Failure() throws Exception {
        // Arrange
        String email = "test@example.com";
        when(userService.getUserByEmail(email)).thenThrow(new Exception("user not found"));


        // Act and Assert
        Exception exception = assertThrows(Exception.class, () -> {
            userController.GetUserByEmail(email);
        });

        // Assert specific exception details if needed
        assertEquals("user not found", exception.getMessage());


        verify(userService).getUserByEmail(email);
    }

    @Test
    public void testGetUsers_Success() throws Exception {
        // Arrange
        UserDto user1 = UserDto.builder()
                .id(1L)
                .email("user1@example.com")
                .firstName("user1")
                .lastName("user1 last name")
                .build();
        UserDto user2 = UserDto.builder()
                .id(2L)
                .email("user2@example.com")
                .firstName("user2")
                .lastName("user2 last name")
                .build();
        List<UserDto> expectedUsers = Arrays.asList(user1, user2);
        when(userService.getUsers()).thenReturn(expectedUsers);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/user/getUsers"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(expectedUsers.size())));

        // Verify that the userService method was called
        verify(userService).getUsers();
    }
    @Test
    public void testGetUsers_Failure() throws Exception {
        // Arrange
        when(userService.getUsers()).thenThrow(new RuntimeException("Failed to get users"));

        // Act and Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            userController.GetUsers();
        });

        // Assert specific exception details if needed
        assertEquals("Failed to get users", exception.getMessage());

        // Verify that the userService method was called
        verify(userService).getUsers();
    }

}