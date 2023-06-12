package com.joyned.reddit.service;


import com.joyned.reddit.converter.RedditConverter;
import com.joyned.reddit.dto.PostDto;
import com.joyned.reddit.dto.request.PostRequestDto;
import com.joyned.reddit.model.Like;
import com.joyned.reddit.model.Post;
import com.joyned.reddit.model.Topic;
import com.joyned.reddit.model.User;
import com.joyned.reddit.repository.PostRepository;
import com.joyned.reddit.repository.TopicRepository;
import com.joyned.reddit.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class PostServiceTests {
    @Mock
    private PostRepository postRepository;

    @Mock
    private TopicRepository topicRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RedditService redditService;

    @Mock
    private RedditConverter redditConverter;

    @InjectMocks
    private PostService postService;

    @Test
    public void testGetPosts() {
        // Arrange
        List<Post> posts = Arrays.asList(
                Post.builder().id(1L).title("Post 1").content("Content 1").build(),
                Post.builder().id(2L).title("Post 2").content("Content 2").build()
        );
        when(postRepository.findAll()).thenReturn(posts);
        when(redditConverter.convertToPostDTO(any(Post.class))).thenAnswer(invocation -> {
            Post post = invocation.getArgument(0);
            return PostDto.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .build();
        });

        // Act
        List<PostDto> result = postService.getPosts();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Post 1", result.get(0).getTitle());
        assertEquals("Content 1", result.get(0).getContent());
        assertEquals("Post 2", result.get(1).getTitle());
        assertEquals("Content 2", result.get(1).getContent());

        // Verify that the postRepository method was called
        verify(postRepository).findAll();
    }

    @Test
    public void testAddPostToTopic_Success() throws Exception {
        // Arrange
        Long topicId = 1L;
        List<Post> existingPosts = new ArrayList<>(Arrays.asList(
                Post.builder().id(1L).title("Post 1").content("Content 1").build(),
                Post.builder().id(2L).title("Post 2").content("Content 2").build()
        ));
        Topic existingTopic = Topic.builder().id(topicId).posts(existingPosts).build();
        PostRequestDto postToAdd = PostRequestDto.builder()
                .title("New Post")
                .content("Post Content")
                .build();

//        existingTopic.getPosts().add(postToAdd);
        when(topicRepository.findById(topicId)).thenReturn(Optional.of(existingTopic));
        when(postService.addPost(postToAdd)).thenReturn(Post.builder().id(1L).build());

        // Act
        postService.addPostToTopic(topicId, postToAdd);

        // Assert
        assertEquals(3, existingTopic.getPosts().size());
        verify(topicRepository).save(existingTopic);
    }

    @Test
    public void testAddPostToTopic_ThrowsException_WhenTopicNotFound() {
        // Arrange
        Long topicId = 1L;
        PostRequestDto postToAdd = PostRequestDto.builder()
                .title("New Post")
                .content("Post Content")
                .build();
        when(topicRepository.findById(topicId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(Exception.class, () -> postService.addPostToTopic(topicId, postToAdd));
        verify(topicRepository).findById(topicId);
    }



    @Test
    public void testAddPost_Success() throws Exception {
        // Arrange
        PostRequestDto postRequestDto = PostRequestDto.builder()
                .title("New Post")
                .content("Post Content")
                .build();
        Post post = Post.builder()
                .id(1L)
                .title("New Post")
                .content("Post Content")
                .lastUpdated(LocalDate.now())
                .build();
        when(redditService.changeTracker(eq(postRequestDto), any(Post.class))).thenReturn(post);
        when(postRepository.save(post)).thenReturn(post);

        // Act
        Post result = postService.addPost(postRequestDto);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("New Post", result.getTitle());
        assertEquals("Post Content", result.getContent());
        assertEquals(LocalDate.now(), result.getLastUpdated());

        // Verify that the postRepository method was called
        verify(postRepository).save(post);
    }

    @Test
    public void testUpdatePost_Success() throws Exception {
        // Arrange
        Long postId = 1L;
        PostRequestDto postToUpdate = PostRequestDto.builder()
                .title("Updated Post")
                .content("Updated Content")
                .build();
        Post existingPost = Post.builder()
                .id(postId)
                .title("Existing Post")
                .content("Existing Content")
                .lastUpdated(LocalDate.now())
                .build();
        when(postRepository.findById(postId)).thenReturn(Optional.of(existingPost));
        when(redditService.changeTracker(eq(postToUpdate), any(Post.class))).thenReturn(existingPost);
        when(postRepository.save(existingPost)).thenReturn(existingPost);

        // Act
        postService.updatePost(postId, postToUpdate);

        // Assert
        assertEquals("Updated Post", existingPost.getTitle());
        assertEquals("Updated Content", existingPost.getContent());
        assertEquals(LocalDate.now(), existingPost.getLastUpdated());
        verify(postRepository).save(existingPost);
    }


    @Test
    public void testUpdatePost_ThrowsException_WhenPostNotFound() {
        // Arrange
        Long postId = 1L;
        PostRequestDto postToUpdate = PostRequestDto.builder()
                .title("Updated Post")
                .content("Updated Content")
                .build();
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(Exception.class, () -> postService.updatePost(postId, postToUpdate));
        verify(postRepository).findById(postId);
    }

    @Test
    public void testLikedPost_Success() throws Exception {
        // Arrange
        Long postId = 1L;
        Long userId = 1L;
        List<Like> likes = new ArrayList<>(Arrays.asList(
                Like.builder().id(1L).whenLiked(LocalDate.now()).build(),
                Like.builder().id(2L).whenLiked(LocalDate.now()).build()
        ));
        Post existingPost = Post.builder()
                .id(postId)
                .title("Existing Post")
                .content("Existing Content")
                .lastUpdated(LocalDate.now())
                .likes(likes)
                .build();
        User existingUser = User.builder()
                .id(userId)
                .firstName("John")
                .lastName("Daw")
                .build();
        when(postRepository.findById(postId)).thenReturn(Optional.of(existingPost));
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        // Act
        postService.likedPost(postId, userId);

        // Assert
        assertEquals(3, existingPost.getLikes().size());
        assertEquals(existingUser, existingPost.getLikes().get(2).getUser());
        assertEquals(existingPost, existingPost.getLikes().get(2).getPost());
        assertEquals(LocalDate.now(), existingPost.getLikes().get(2).getWhenLiked());
        verify(postRepository).save(existingPost);
    }
    @Test
    public void testLikedPost_ThrowsException_WhenPostNotFound() {
        // Arrange
        Long postId = 1L;
        Long userId = 1L;
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(Exception.class, () -> postService.likedPost(postId, userId));
        verify(postRepository).findById(postId);
    }

    @Test
    public void testLikedPost_ThrowsException_WhenUserNotFound() {
        // Arrange
        Long postId = 1L;
        Long userId = 1L;
        Post existingPost = Post.builder()
                .id(postId)
                .title("Existing Post")
                .content("Existing Content")
                .lastUpdated(LocalDate.now())
                .build();
        when(postRepository.findById(postId)).thenReturn(Optional.of(existingPost));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(Exception.class, () -> postService.likedPost(postId, userId));
        verify(userRepository).findById(userId);
    }


}


