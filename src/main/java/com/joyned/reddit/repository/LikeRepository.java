package com.joyned.reddit.repository;


import com.joyned.reddit.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

}
