package com.joyned.reddit.model;


import jakarta.persistence.*;
//import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "_users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "First-name is a required field")
    private String firstName;
    @NotBlank(message = "last-name is a required field")
    private String lastName;
    @NotBlank(message = "password is a required field")
    private String password;
    @NotBlank(message = "email is a required field")
    @Column(unique = true)
    private String email;

    private LocalDate lastConnectivity;

    @ManyToMany(mappedBy="users")
    private List<Topic> topics;
    @OneToMany(
            mappedBy="user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderBy(value="whenLiked Desc")
    private List<Like> likes;
}

