package com.joyned.reddit.comtroller;


import com.joyned.reddit.dto.PostDto;
import com.joyned.reddit.dto.TopicDto;
import com.joyned.reddit.dto.request.AddTopicRequestDto;
import com.joyned.reddit.service.TopicService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class TopicControllerTests {
    @Mock
    private TopicService topicService;

    @InjectMocks
    private TopicController topicController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(topicController).build();
    }

    @Test
    public void testGetTopics_Success() throws Exception {
        // Arrange
        TopicDto topic1 = TopicDto.builder()
                .id(12L)
                .title("Phones")
                .description("This topic will discuss and focus on phones")
                .build();
        TopicDto topic2 = TopicDto.builder()
                .id(13L)
                .title("Politics")
                .description("This topic will discuss and focus on politics")
                .build();
        TopicDto topic3 = TopicDto.builder()
                .id(14L)
                .title("Sport")
                .description("This topic will discuss and focus on sports")
                .build();
        List<TopicDto> expectedTopics = Arrays.asList(topic1, topic2, topic3);
        when(topicService.getTopics()).thenReturn(expectedTopics);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/topic/getTopics"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(expectedTopics.size())));

        // Verify that the topicService method was called
        verify(topicService).getTopics();
    }
    @Test
    public void testGetTopics_Failure() {
        // Arrange
        when(topicService.getTopics()).thenThrow(new RuntimeException("Failed to get topics"));

        // Act and Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            topicController.getTopics();
        });

        // Assert specific exception details if needed
        assertEquals("Failed to get topics", exception.getMessage());

        // Verify that the topicService method was called
        verify(topicService).getTopics();
    }
    @Test
    public void testGetTopicPosts_Success() throws Exception {
        // Arrange
        Long topicId = 1L;
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
        List<PostDto> expectedTopicPosts = Arrays.asList(post1, post2, post3);
        when(topicService.getTopicPosts(topicId)).thenReturn(expectedTopicPosts);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/topic/getTopicPosts/{topicId}", topicId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(expectedTopicPosts.size())));

        // Verify that the topicService method was called
        verify(topicService).getTopicPosts(topicId);
    }


    @Test
    public void testGetTopicPosts_Failure() throws Exception {
        // Arrange
        Long topicId = 1L;
        when(topicService.getTopicPosts(topicId)).thenThrow(new RuntimeException("Failed to get topic posts"));

        // Act and Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            topicController.getTopicPosts(topicId);
        });

        // Assert specific exception details if needed
        assertEquals("Failed to get topic posts", exception.getMessage());

        // Verify that the topicService method was called
        verify(topicService).getTopicPosts(topicId);
    }


    @Test
    public void testGetTopicPosts_ValidationErrors() throws Exception {
        // Arrange
        Long topicId = null;

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/topic/getTopicPosts/{topicId}", topicId))
                .andExpect(status().isNotFound());

        // Verify that the postService method was not called
        verify(topicService, Mockito.times(0)).getTopicPosts(Mockito.anyLong());
    }

    @Test
    public void testAddTopic_Success() throws Exception {
        // Arrange
        AddTopicRequestDto topicToAdd = AddTopicRequestDto.builder()
                .title("Politics")
                .description("This topic will discuss and focus on politics")
                .build();

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/topic/addTopic")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(topicToAdd)))
                .andExpect(status().isCreated());

        // Verify that the topicService method was called
        verify(topicService).addTopic(topicToAdd);
    }

    @Test
    public void testAddTopic_ValidationErrors() throws Exception {
        // Arrange
        Long topicId = null;

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/topic/getTopicPosts/{topicId}", topicId))
                .andExpect(status().isNotFound());

        // Verify that the postService method was not called
        verify(topicService, Mockito.times(0)).getTopicPosts(Mockito.anyLong());
    }
    @Test
    public void testAddTopic_Failure() {
        // Arrange
        AddTopicRequestDto topicToAdd = AddTopicRequestDto.builder()
                .title("Politics")
                .description("This topic will discuss and focus on politics")
                .build();
        doThrow(new RuntimeException("Failed to add topic")).when(topicService).addTopic(topicToAdd);

        // Act and Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            topicController.addTopic(topicToAdd);
        });

        // Assert specific exception details if needed
        assertEquals("Failed to add topic", exception.getMessage());

        // Verify that the topicService method was called
        verify(topicService).addTopic(topicToAdd);
    }

}