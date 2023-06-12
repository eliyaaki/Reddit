package com.joyned.reddit.service;


import com.joyned.reddit.converter.RedditConverter;
import com.joyned.reddit.dto.request.PostRequestDto;
import com.joyned.reddit.model.Post;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class RedditService {

    @Value("${weather.api.location}")
    private String location;
    private final WeatherService weatherService;
    private final RedditConverter redditConverter;

    public RedditService(WeatherService weatherService, RedditConverter redditConverter) {
        this.weatherService = weatherService;
        this.redditConverter = redditConverter;
    }

    public Post changeTracker(PostRequestDto postRequestDto, Post post) {
        try {
            if (postRequestDto.getLocation() == null) {
                var weatherData = weatherService.getWeatherData(Optional.ofNullable(location).orElse("London"));
                post.setWeather(weatherData);
            } else {
                post.setLocation(redditConverter.convertFromLocationDTO(postRequestDto.getLocation()));
            }
            return post;
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while processing the request.", e);
        }
    }

}


