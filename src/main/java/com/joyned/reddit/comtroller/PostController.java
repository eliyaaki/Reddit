package com.joyned.reddit.comtroller;


import com.joyned.reddit.dto.PostDto;
import com.joyned.reddit.dto.request.PostRequestDto;
import com.joyned.reddit.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
@Slf4j
public class PostController {
    private final PostService postService;

    @GetMapping("/getPosts")
    public List<PostDto> GetPosts() {
        return postService.getPosts();
    }

    @PutMapping("/addPostToTopic/{topicId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void addPostToTopic(@PathVariable @Valid Long topicId, @RequestBody @Valid PostRequestDto postToAdd) throws Exception {
        log.info("topicId:  " + topicId);
        log.info("postBody:  " + postToAdd);
        postService.addPostToTopic(topicId, postToAdd);
    }
    @PutMapping("/updatePost/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public void updatePost(@PathVariable @Valid Long postId, @RequestBody @Valid PostRequestDto postToUpdate) throws Exception {
        log.info("postId:  "+postId);
        log.info("postBody:  "+postToUpdate);
        postService.updatePost(postId, postToUpdate);
    }
    @PutMapping("/likedPost/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public void likedPost(@PathVariable @Valid Long postId, @RequestBody @Valid Long userId) throws Exception {
        log.info("postId:  "+postId);
        log.info("userId:  "+userId);
        postService.likedPost(postId, userId);
    }

}