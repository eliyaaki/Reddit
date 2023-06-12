package com.joyned.reddit.repository;


import com.joyned.reddit.model.Like;
import com.joyned.reddit.model.PostWeatherLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AuditLogRepository extends JpaRepository<PostWeatherLog, Long> {

}
