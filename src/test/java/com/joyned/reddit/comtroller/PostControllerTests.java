package com.joyned.reddit.comtroller;


import com.joyned.reddit.dto.PostDto;
import com.joyned.reddit.dto.request.PostRequestDto;
import com.joyned.reddit.service.PostService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostControllerTests {
    @Mock
    private PostService postService;

    @InjectMocks
    private PostController postController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(postController).build();
    }

    @Test
    public void testGetPosts_Success() throws Exception {
        // Arrange
        PostDto post1 = PostDto.builder()
                .id(12L)
                .title("Phones")
                .content("Iphone 14")
                .build();
        PostDto post2 = PostDto.builder()
                .id(13L)
                .title("Phones")
                .content("Iphone 15")
                .build();
        PostDto post3 = PostDto.builder()
                .id(14L)
                .title("Phones")
                .content("Samsung 12")
                .build();
        List<PostDto> expectedPosts = Arrays.asList(post1, post2, post3);
        when(postService.getPosts()).thenReturn(expectedPosts);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/post/getPosts"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(expectedPosts.size())));

        // Verify that the postService method was called
        verify(postService).getPosts();
    }

    @Test
    public void testAddPostToTopic_Success() throws Exception {
        // Arrange
        Long topicId = 1L;
        PostRequestDto postToAdd = PostRequestDto.builder()
                .title("phones")
                .content("iphone 14")
                .build();

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/post/addPostToTopic/{topicId}", topicId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(postToAdd)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        verify(postService).addPostToTopic(topicId, postToAdd);
    }
    @Test
    public void testAddPostToTopic_ValidationErrors() throws Exception {
        // Arrange
        Long topicId = 1L;
        PostRequestDto postToAdd = PostRequestDto.builder()
                .title("")
                .content("")
                .build();// Empty title and content

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/post/addPostToTopic/{topicId}", topicId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(postToAdd)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // Verify that the postService method was not called
        verify(postService, Mockito.times(0)).addPostToTopic(Mockito.anyLong(), Mockito.any(PostRequestDto.class));
    }
    @Test
    public void testUpdatePost_Success() throws Exception {
        // Arrange
        Long postId = 1L;
        PostRequestDto postToUpdate = PostRequestDto.builder()
                .title("phones")
                .content("iphone 14")
                .build();

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/post/updatePost/{postId}", postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(postToUpdate)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(postService).updatePost(postId, postToUpdate);
    }
    @Test
    public void testUpdatePost_ValidationErrors() throws Exception {
        // Arrange
        Long postId = 1L;
        PostRequestDto postToUpdate = PostRequestDto.builder()
                .title("")
                .content("")
                .build();


        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/post/updatePost/{postId}", postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(postToUpdate)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // Verify that the postService method was not called
        verify(postService, Mockito.times(0)).updatePost(Mockito.anyLong(), Mockito.any(PostRequestDto.class));
    }

    @Test
    public void testLikedPost_Success() throws Exception {
        // Arrange
        Long postId = 1L;
        Long userId = 1L;

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/post/likedPost/{postId}", postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userId)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(postService).likedPost(postId, userId);
    }
    @Test
    public void testLikedPost_ValidationErrors() throws Exception {
        // Arrange
        Long postId = null;
        Long userId = 1L;


        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/post/likedPost/{postId}", postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userId)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        // Verify that the postService method was not called
        verify(postService, Mockito.times(0)).likedPost(Mockito.anyLong(), Mockito.anyLong());
    }
}