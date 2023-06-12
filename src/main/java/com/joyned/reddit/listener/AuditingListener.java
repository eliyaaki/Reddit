package com.joyned.reddit.listener;

import com.joyned.reddit.model.Post;
import com.joyned.reddit.model.PostWeatherLog;
import com.joyned.reddit.repository.AuditLogRepository;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class AuditingListener {
    private static AuditLogRepository auditLogRepository;

    @Autowired
    public void setAuditLogRepository(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @PostUpdate
    @PostPersist
    public void onPostPersistAndUpdate(Post post) {
        log.info("getting to AuditingListener");
        if (isWeatherUpdated(post)) {
            PostWeatherLog auditLog = new PostWeatherLog();
            auditLog.setPost(post);
            auditLog.setTimestamp(LocalDateTime.now());
            auditLogRepository.save(auditLog);
        }
    }

    private boolean isWeatherUpdated(Post post) {
        return post.getWeather()!=null;
    }
}
