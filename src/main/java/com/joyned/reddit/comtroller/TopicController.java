package com.joyned.reddit.comtroller;


import com.joyned.reddit.dto.PostDto;
import com.joyned.reddit.dto.TopicDto;
import com.joyned.reddit.dto.request.AddTopicRequestDto;
import com.joyned.reddit.service.TopicService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/topic")
@RequiredArgsConstructor
@Slf4j
public class TopicController {
    private final TopicService topicService;

    @GetMapping("/getTopics")
    public List<TopicDto> getTopics() {
        return topicService.getTopics();
    }

    @GetMapping("/getTopicPosts/{topicId}")
    public List<PostDto> getTopicPosts(@PathVariable @Valid Long topicId) throws Exception {
            return topicService.getTopicPosts(topicId);
    }
    @PostMapping("/addTopic")
    @ResponseStatus(HttpStatus.CREATED)
    public void addTopic(@RequestBody @Valid AddTopicRequestDto topicToAdd) throws Exception{
        log.info("topicToAdd:  " + topicToAdd);
        topicService.addTopic(topicToAdd);
    }
}