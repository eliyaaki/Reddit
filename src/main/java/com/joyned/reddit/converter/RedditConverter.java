package com.joyned.reddit.converter;

import com.joyned.reddit.dto.*;
import com.joyned.reddit.model.*;
import org.springframework.stereotype.Service;

@Service
public class RedditConverter {
    public PostDto convertToPostDTO(Post post) {
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setContent(post.getContent());
        postDto.setLastUpdated(post.getLastUpdated());
        postDto.setTitle(post.getTitle());
        convertPostLocationOrWeatherData(postDto,post);
        return postDto;
    }

    public TopicDto convertToTopicDTO(Topic topic) {
        TopicDto topicDto = new TopicDto();
        topicDto.setId(topic.getId());
        topicDto.setDescription(topic.getDescription());
        topicDto.setTitle(topic.getTitle());
        return topicDto;
    }

    public UserDto convertToUserDTO(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setLastConnectivity(user.getLastConnectivity());
        return userDto;
    }

    private void convertPostLocationOrWeatherData(PostDto postDto, Post post) {
        if (post.getLocation()!=null){
            var locationDto=convertToLocationDTO(post.getLocation());
            postDto.setLocation(locationDto);
        } else if (post.getWeather()!=null) {
            var weatherDataDto=convertToWeatherDataDTO(post.getWeather());
            postDto.setWeather(weatherDataDto);
        }
    }
    public WeatherDataDto convertToWeatherDataDTO(WeatherData weatherData) {
        WeatherDataDto weatherDataDto = new WeatherDataDto();
        if (weatherData!=null) {
            weatherDataDto.setCondition(weatherData.getCondition());
            weatherDataDto.setLocation(weatherData.getLocation());
            weatherDataDto.setTemperature(weatherData.getTemperature());
        }
        return weatherDataDto;
    }

    public LocationDto convertToLocationDTO(Location location) {
        LocationDto locationDto = new LocationDto();
        if (location!=null) {
            locationDto.setCity(location.getCity());
            locationDto.setCountry(location.getCountry());
        }
        return locationDto;
    }

    public Location convertFromLocationDTO(LocationDto locationDto) {
        Location location = new Location();
        if (locationDto!=null) {
            location.setCity(locationDto.getCity());
            location.setCountry(locationDto.getCountry());
        }
        return location;
    }
}
