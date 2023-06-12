package com.joyned.reddit.service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joyned.reddit.model.WeatherData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
class WeatherService {

    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${weather.api.url}")
    private String apiUrl;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    WeatherService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public WeatherData getWeatherData(String location) {
        try {
            String requestUrl = apiUrl + "?key=" + apiKey + "&q=" + location;

            ResponseEntity<String> response = restTemplate.getForEntity(requestUrl, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                String responseBody = response.getBody();
                JsonNode jsonNode = objectMapper.readTree(responseBody);

                // Extract specific weather data
                String temperature = jsonNode.get("current").get("temp_c").asText();
                String condition = jsonNode.get("current").get("condition").get("text").asText();
                // Print the weather information
                log.info("Location: " + location);
                log.info("Temperature: " + temperature + "°C");
                log.info("Condition: " + condition);
                return WeatherData.builder()
                        .location(location)
                        .temperature(temperature)
                        .condition(condition)
                        .build();
            }
        } catch (HttpClientErrorException e) {
            // Handle API error responses
            log.error("API Error: " + e.getMessage());
        } catch (Exception e) {
            // Handle other exceptions
            log.error("Error: " + e.getMessage());
        }
        return null;
    }
}
