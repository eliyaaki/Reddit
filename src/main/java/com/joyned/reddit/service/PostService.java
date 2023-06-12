package com.joyned.reddit.service;




import com.joyned.reddit.converter.RedditConverter;
import com.joyned.reddit.dto.PostDto;
import com.joyned.reddit.dto.request.PostRequestDto;
import com.joyned.reddit.exception.NotFoundException;
import com.joyned.reddit.model.Like;
import com.joyned.reddit.model.Post;
import com.joyned.reddit.repository.PostRepository;
import com.joyned.reddit.repository.TopicRepository;
import com.joyned.reddit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PostService {
    private final PostRepository postRepository;
    private final TopicRepository topicRepository;
    private final UserRepository userRepository;
    private final RedditService redditService;

    private final RedditConverter redditConverter;

    public List<PostDto> getPosts() {
        try{
        return postRepository.findAll().stream().map(post -> redditConverter.convertToPostDTO(post)).toList();
        }catch (Exception e) {
            log.error("Failed to get posts", e);
            throw new RuntimeException("Failed to get posts", e);
        }
    }


    public void addPostToTopic(Long topicId, PostRequestDto postToAdd) throws Exception {
        var existingTopic= topicRepository.findById(topicId).orElseThrow(()->new NotFoundException("Topic not found"));
        var existingPost =  addPost(postToAdd);
        if (existingPost!=null) {
            if (existingTopic.getPosts()==null){
                existingTopic.setPosts(new ArrayList<Post>());
            }
            existingTopic.getPosts().add(existingPost);
            topicRepository.save(existingTopic);
            log.info("Post added successfully to topic: ", existingTopic);
        }else{
            log.error("something went wrong with adding post to topic: ", existingTopic);
        }
    }

    public Post addPost(PostRequestDto postRequestDto) throws Exception {
        try{
        var post = Post.builder()
                .title(postRequestDto.getTitle())
                .content(postRequestDto.getContent())
                .lastUpdated(LocalDate.now())
                .build();
         return changeTracker(post, postRequestDto);
        }catch (Exception e) {
            log.error("Failed to add post", e);
            throw new RuntimeException("Failed to add post", e);
        }
    }

    public void updatePost(Long id, PostRequestDto postToUpdate) throws Exception {
        var existingPost = postRepository.findById(id).orElseThrow(() -> new NotFoundException("Post not found"));
       try{
        existingPost.setTitle(postToUpdate.getTitle());
        existingPost.setContent(postToUpdate.getContent());
        existingPost.setLastUpdated(LocalDate.now());
        changeTracker(existingPost, postToUpdate);
       }catch (Exception e) {
           log.error("Failed to update post", e);
           throw new RuntimeException("Failed to update post", e);
       }
    }
    public Post changeTracker(Post post, PostRequestDto postRequestDto) throws Exception {
        var updatedPost= redditService.changeTracker(postRequestDto,post);
        return postRepository.save(updatedPost);
    }
    public void likedPost(Long postId, Long userId) throws Exception {
        var existingPost=postRepository.findById(postId).orElseThrow(()->new NotFoundException("Post not found"));
        var existingUser=userRepository.findById(userId).orElseThrow(()->new NotFoundException("User not found"));
     try{
        var likeToAdd = Like.builder()
                .user(existingUser)
                .post(existingPost)
                .whenLiked(LocalDate.now())
                .build();
        if (existingPost.getLikes()==null){
            existingPost.setLikes(new ArrayList<Like>());
        }
        existingPost.getLikes().add(likeToAdd);
        postRepository.save(existingPost);
     }catch (Exception e) {
         log.error("something went wrong with liking the post", e);
         throw new RuntimeException("something went wrong with liking the post", e);
     }
    }


}


