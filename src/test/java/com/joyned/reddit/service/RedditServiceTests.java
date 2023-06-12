package com.joyned.reddit.service;


import com.joyned.reddit.converter.RedditConverter;
import com.joyned.reddit.dto.LocationDto;
import com.joyned.reddit.dto.request.PostRequestDto;
import com.joyned.reddit.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mockito.Mockito;
import org.springframework.stereotype.Service;

import com.joyned.reddit.converter.RedditConverter;
import com.joyned.reddit.dto.PostDto;
import com.joyned.reddit.dto.request.PostRequestDto;
import com.joyned.reddit.model.Post;
import com.joyned.reddit.repository.PostRepository;
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
public class RedditServiceTests {
    @Mock
    private WeatherService weatherService;

    @Mock
    private RedditConverter redditConverter;

    @InjectMocks
    private RedditService redditService;


    @Test
    public void testChangeTracker_NullLocation_Success() throws Exception {
        // Arrange
        PostRequestDto postRequestDto = new PostRequestDto();
        Post post = new Post();
        WeatherData weatherData = WeatherData.builder()
                .location("London")
                .build();

        when(weatherService.getWeatherData(anyString())).thenReturn(weatherData);

        // Act
        Post result = redditService.changeTracker(postRequestDto, post);

        // Assert
        assertEquals("London", result.getWeather().getLocation());
        verify(weatherService).getWeatherData("London");
        verify(redditConverter, never()).convertFromLocationDTO(any());
    }

    @Test
    public void testChangeTracker_NonNullLocation_Success() throws Exception {
        // Arrange
        PostRequestDto postRequestDto = new PostRequestDto();
        postRequestDto.setLocation(new LocationDto());
        Post post = new Post();
        Location location= Location.builder()
                .country("Israel")
                .city("Tel-Aviv")
                .build();

        when(redditConverter.convertFromLocationDTO(any())).thenReturn(location);

        // Act
        Post result = redditService.changeTracker(postRequestDto, post);

        // Assert
        assertEquals(location, result.getLocation());
        verify(weatherService, never()).getWeatherData(anyString());
        verify(redditConverter).convertFromLocationDTO(postRequestDto.getLocation());
    }

    @Test
    public void testChangeTracker_ExceptionThrown() throws Exception {
        // Arrange
        PostRequestDto postRequestDto = new PostRequestDto();
        postRequestDto.setLocation(new LocationDto());
        Post post = new Post();

        Mockito.when(redditConverter.convertFromLocationDTO(Mockito.any())).thenAnswer(invocation -> {
            throw new Exception("Error occurred while processing the request.");
        });

        // Act and Assert
        Exception exception = assertThrows(Exception.class, () ->
                redditService.changeTracker(postRequestDto, post));

        assertEquals("Error occurred while processing the request.", exception.getMessage());
        verify(weatherService, never()).getWeatherData(Mockito.anyString());
        verify(redditConverter).convertFromLocationDTO(postRequestDto.getLocation());
    }

}


