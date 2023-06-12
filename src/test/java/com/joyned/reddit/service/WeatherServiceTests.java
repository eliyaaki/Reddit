package com.joyned.reddit.service;

import com.joyned.reddit.converter.RedditConverter;
import com.joyned.reddit.dto.PostDto;
import com.joyned.reddit.dto.request.PostRequestDto;
import com.joyned.reddit.model.*;
import com.joyned.reddit.repository.PostRepository;
import com.joyned.reddit.repository.TopicRepository;
import com.joyned.reddit.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class WeatherServiceTests {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private WeatherService weatherService;

    @Test
    void testGetWeatherData_Success() {
        // Arrange
        String location = "London";
        String responseBody = "{\"current\":{\"temp_c\":25,\"condition\":{\"text\":\"Sunny\"}}}";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(responseBody, HttpStatus.OK);
        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(responseEntity);

        // Act
        WeatherData result = weatherService.getWeatherData(location);

        // Assert
        assertNull(result);
        verify(restTemplate).getForEntity(anyString(), eq(String.class));
    }

    @Test
    void testGetWeatherData_ApiError() {
        // Arrange
        String location = "London";
        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        // Act
        WeatherData result = weatherService.getWeatherData(location);

        // Assert
        assertNull(result);
        verify(restTemplate).getForEntity(anyString(), eq(String.class));
    }

    @Test
    void testGetWeatherData_ExceptionThrown() {
        // Arrange
        String location = "London";
        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenThrow(new RuntimeException("Internal Server Error"));

        // Act
        WeatherData result = weatherService.getWeatherData(location);

        // Assert
        assertNull(result);
        verify(restTemplate).getForEntity(anyString(), eq(String.class));
    }
}
