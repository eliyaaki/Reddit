package com.joyned.reddit.repository;


import com.joyned.reddit.model.Topic;
import com.joyned.reddit.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {

}
