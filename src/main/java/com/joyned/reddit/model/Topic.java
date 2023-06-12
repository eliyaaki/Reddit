package com.joyned.reddit.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "topic title is a required field")
    private String title;
    @NotBlank(message = "topic description is a required field")
    private String description;

    @ManyToMany
    @JoinTable(name = "topics_posts", joinColumns = @JoinColumn(name = "topic_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "post_id", referencedColumnName = "id"))
    @OrderBy(value="lastUpdated Desc")
    private List<Post> posts;

    @ManyToMany
    @JoinTable(name = "topics_users", joinColumns = @JoinColumn(name = "topic_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private List<User> users;
}
