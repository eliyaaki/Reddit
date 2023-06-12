package com.joyned.reddit.service;


import com.joyned.reddit.converter.RedditConverter;
import com.joyned.reddit.dto.UserDto;
import com.joyned.reddit.dto.request.LoginRequestDto;
import com.joyned.reddit.dto.request.RegistrationRequestDto;
import com.joyned.reddit.model.Topic;
import com.joyned.reddit.model.User;
import com.joyned.reddit.repository.TopicRepository;
import com.joyned.reddit.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    @Mock
    private UserRepository userRepository;

    @Mock
    private TopicRepository topicRepository;

    @Mock
    private RedditConverter redditConverter;

    @InjectMocks
    private UserService userService;

    @Test
    public void testGetUsers_Success() {
        // Arrange
        List<User> userList = Arrays.asList(
                User.builder().id(1L).firstName("John").lastName("Doe").email("john@example.com").build(),
                User.builder().id(2L).firstName("Jane").lastName("Smith").email("jane@example.com").build()
        );
        when(userRepository.findAll()).thenReturn(userList);
        when(redditConverter.convertToUserDTO(any(User.class))).thenReturn(new UserDto());

        // Act
        List<UserDto> result = userService.getUsers();

        // Assert
        assertEquals(2, result.size());
        verify(userRepository).findAll();
        verify(redditConverter, times(2)).convertToUserDTO(any(User.class));
    }
    @Test
    public void testGetUserByEmail_Success() throws Exception {
        // Arrange
        String email = "john@example.com";
        User user = User.builder().id(1L).firstName("John").lastName("Doe").email(email).build();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        UserDto userDto = UserDto.builder().id(1L).firstName("John").lastName("Doe").email(email).build();
        when(redditConverter.convertToUserDTO(user)).thenReturn(userDto);

        // Act
        UserDto result = userService.getUserByEmail(email);

        // Assert
        assertNotNull(result);
        assertEquals(email, result.getEmail());
        verify(userRepository).findByEmail(email);
        verify(redditConverter).convertToUserDTO(user);
    }

    @Test
    public void testRegisterUser_UserNotExists_UserAddedSuccessfully() throws Exception {
        // Arrange
        String email = "john@example.com";
        RegistrationRequestDto userToAdd = new RegistrationRequestDto("John", "Doe", email, "password");
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act
        userService.registerUser(userToAdd);

        // Assert
        verify(userRepository).findByEmail(email);
        verify(userRepository).save(any(User.class));
    }

    @Test
    public void testRegisterUser_UserAlreadyExists_ExceptionThrown() throws Exception {
        // Arrange
        String email = "john@example.com";
        RegistrationRequestDto userToAdd = new RegistrationRequestDto("John", "Doe", email, "password");
        User existingUser = User.builder()
                .firstName("John")
                .lastName("Doe")
                .email(email)
                .password("hashedPassword")
                .build();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(existingUser));

        // Act and Assert
        assertThrows(Exception.class, () -> userService.registerUser(userToAdd));

        // Verify
        verify(userRepository).findByEmail(email);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testAddUser_Success() {
        // Arrange
        RegistrationRequestDto userToAdd = new RegistrationRequestDto("John", "Doe", "john@example.com", "password");
        when(userRepository.save(any(User.class))).thenReturn(new User());

        // Act
        userService.addUser(userToAdd);

        // Assert
        verify(userRepository).save(any(User.class));
    }

    @Test
    public void testAddUser_Failure() {
        // Arrange
        RegistrationRequestDto userToAdd = new RegistrationRequestDto(null, null, null, null);

        // Act and Assert
        assertThrows(Exception.class, () -> userService.addUser(userToAdd));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testAddUserToTopic_Success() throws Exception {
        // Arrange
        Long topicId = 1L;
        Long userId = 1L;
        List<User> users = new ArrayList<>(Arrays.asList(
                User.builder().id(1L).firstName("gvadg").lastName("agdga").email("asgasgas@gmail.com").build(),
                User.builder().id(2L).id(1L).firstName("jrtjr").lastName("tuluyu").email("kghkghj@gmail.com").build()
        ));
        User existingUser = User.builder().id(userId).build();
        Topic existingTopic = Topic.builder().id(topicId).users(users).build();
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(topicRepository.findById(topicId)).thenReturn(Optional.of(existingTopic));

        // Act
        userService.addUserToTopic(topicId, userId);

        // Assert
        assertTrue(existingTopic.getUsers().contains(existingUser));
        verify(userRepository).findById(userId);
        verify(topicRepository).findById(topicId);
        verify(topicRepository).save(existingTopic);
    }

    @Test
    public void testAddUserToTopic_UserNotFound_ExceptionThrown() {
        // Arrange
        Long topicId = 1L;
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act and Assert
        Exception exception = assertThrows(Exception.class, () -> userService.addUserToTopic(topicId, userId));
        assertEquals("Failed to add user to topic", exception.getMessage());
        verify(userRepository).findById(userId);
        verify(topicRepository, never()).findById(topicId);
        verify(topicRepository, never()).save(any(Topic.class));
    }

    @Test
    public void testVerifyUser_Success() throws Exception {
        // Arrange
        String email = "john@example.com";
        String password = "password";
        User user = User.builder().email(email).password(userService.hashPassword(password)).build();
        LoginRequestDto userToVerify = new LoginRequestDto(email, password);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act
        userService.verifyUser(userToVerify);

        // Assert
        verify(userRepository).findByEmail(email);
        assertTrue(user.getLastConnectivity().isEqual(LocalDate.now()));
    }

    @Test
    public void testVerifyUser_UserNotFound_ExceptionThrown() {
        // Arrange
        String email = "john@example.com";
        String password = "password";
        LoginRequestDto userToVerify = new LoginRequestDto(email, password);
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act and Assert
        Exception exception = assertThrows(Exception.class, () -> userService.verifyUser(userToVerify));
        assertEquals("User not found", exception.getMessage());
        verify(userRepository).findByEmail(email);
    }

    @Test
    public void testVerifyUser_IncorrectPassword_ExceptionThrown() {
        // Arrange
        String email = "john@example.com";
        String password = "password";
        User user = User.builder().email(email).password(userService.hashPassword("incorrect")).build();
        LoginRequestDto userToVerify = new LoginRequestDto(email, password);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act and Assert
        Exception exception = assertThrows(Exception.class, () -> userService.verifyUser(userToVerify));
        assertEquals("user not verified successfully", exception.getMessage());
        verify(userRepository).findByEmail(email);
    }


}


