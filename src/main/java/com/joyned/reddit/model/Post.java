package com.joyned.reddit.model;

import com.joyned.reddit.listener.AuditingListener;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingListener.class)
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "title is a required field")
    private String title;
    @NotBlank(message = "content is a required field")
    private String content;
    private LocalDate lastUpdated;

    @OneToOne(cascade = CascadeType.ALL)
    private Location location;
    @OneToOne(cascade = CascadeType.ALL)
    private WeatherData weather;

    @ManyToMany(mappedBy="posts")
    private List<Topic> topics;

    @OneToMany(
            mappedBy="post",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderBy(value="whenLiked Desc")
    private List<Like> likes;

}
