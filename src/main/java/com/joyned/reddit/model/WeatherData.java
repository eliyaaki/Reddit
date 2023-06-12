package com.joyned.reddit.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeatherData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String location;
    private String temperature;
    private String condition;

//    @OneToOne(mappedBy = "weather")
//    private Post post;

    @Override
    public String toString() {
        return "WeatherData{" +
                "location='" + location + '\'' +
                ", temperature='" + temperature + '\'' +
                ", condition='" + condition + '\'' +
                '}';
    }
}
