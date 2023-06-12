package com.joyned.reddit.service;




import com.joyned.reddit.converter.RedditConverter;
import com.joyned.reddit.dto.PostDto;
import com.joyned.reddit.dto.TopicDto;
import com.joyned.reddit.dto.request.AddTopicRequestDto;
import com.joyned.reddit.exception.NotFoundException;
import com.joyned.reddit.model.Post;
import com.joyned.reddit.model.Topic;
import com.joyned.reddit.model.User;
import com.joyned.reddit.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TopicService {
    private final TopicRepository topicRepository;
    private final RedditConverter redditConverter;
    public List<TopicDto> getTopics() {
        try{
        return topicRepository.findAll().stream().map(topic -> redditConverter.convertToTopicDTO(topic)).toList();
        }catch (Exception e) {
            log.error("Failed to get topics", e);
            throw new RuntimeException("Failed to get topics", e);
        }
    }

    public List<PostDto> getTopicPosts(Long topicId) throws Exception {
        var existingTopic=topicRepository.findById(topicId).orElseThrow(()->new NotFoundException("Topic not found"));
       try{
        return existingTopic.getPosts().stream().map(post -> redditConverter.convertToPostDTO(post)).toList();
       }catch (Exception e) {
           log.error("Failed to get topic posts", e);
           throw new RuntimeException("Failed to get topic posts", e);
       }
    }
    public void addTopic(AddTopicRequestDto topicToAdd) {
        try {
            var topic = Topic.builder()
                    .title(topicToAdd.getTitle())
                    .description(topicToAdd.getDescription())
                    .build();
            topicRepository.save(topic);
        }catch (Exception e) {
            log.error("Failed to add topic", e);
            throw new RuntimeException("Failed to add topic", e);
        }
    }
}


