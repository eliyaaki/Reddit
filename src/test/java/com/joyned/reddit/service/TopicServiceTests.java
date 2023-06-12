package com.joyned.reddit.service;


import com.joyned.reddit.converter.RedditConverter;
import com.joyned.reddit.dto.PostDto;
import com.joyned.reddit.dto.TopicDto;
import com.joyned.reddit.dto.request.AddTopicRequestDto;
import com.joyned.reddit.model.Post;
import com.joyned.reddit.model.Topic;
import com.joyned.reddit.repository.TopicRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class TopicServiceTests {
    @Mock
    private TopicRepository topicRepository;

    @Mock
    private RedditConverter redditConverter;

    @InjectMocks
    private TopicService topicService;

    @Test
    public void testGetTopics() {
        // Arrange
        List<Topic> topics = Arrays.asList(
                Topic.builder().id(1L).title("Topic 1").description("Description 1").build(),
                Topic.builder().id(2L).title("Topic 2").description("Description 2").build()
        );
        List<TopicDto> expectedTopicDtos = Arrays.asList(
                TopicDto.builder().id(1L).title("Topic 1").description("Description 1").build(),
                TopicDto.builder().id(2L).title("Topic 2").description("Description 2").build()
        );
        when(topicRepository.findAll()).thenReturn(topics);
        when(redditConverter.convertToTopicDTO(any(Topic.class))).thenAnswer(invocation -> {
            Topic topic = invocation.getArgument(0);
            return TopicDto.builder()
                    .id(topic.getId())
                    .title(topic.getTitle())
                    .description(topic.getDescription())
                    .build();
        });

        // Act
        List<TopicDto> result = topicService.getTopics();

        // Assert
        assertEquals(expectedTopicDtos, result);
        verify(topicRepository).findAll();
        verify(redditConverter, times(2)).convertToTopicDTO(any(Topic.class));
    }

    @Test
    public void testGetTopics_Failure() {
        // Arrange
        when(topicRepository.findAll()).thenThrow(RuntimeException.class);

        // Act and Assert
        assertThrows(RuntimeException.class, () -> topicService.getTopics());
        verify(topicRepository).findAll();
    }


    @Test
    public void testGetTopicPosts_Success() throws Exception {
        // Arrange
        Long topicId = 1L;
        List<Post> posts = Arrays.asList(
                Post.builder().id(1L).title("Post 1").content("Content 1").build(),
                Post.builder().id(2L).title("Post 2").content("Content 2").build()
        );
        Topic existingTopic = Topic.builder().id(topicId).posts(posts).build();
        List<PostDto> expectedPostDtos = Arrays.asList(
                PostDto.builder().id(1L).title("Post 1").content("Content 1").build(),
                PostDto.builder().id(2L).title("Post 2").content("Content 2").build()
        );
        when(topicRepository.findById(topicId)).thenReturn(Optional.of(existingTopic));
        when(redditConverter.convertToPostDTO(any(Post.class))).thenAnswer(invocation -> {
            Post post = invocation.getArgument(0);
            return PostDto.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .build();
        });

        // Act
        List<PostDto> result = topicService.getTopicPosts(topicId);

        // Assert
        assertEquals(expectedPostDtos, result);
        verify(topicRepository).findById(topicId);
        verify(redditConverter, times(2)).convertToPostDTO(any(Post.class));
    }
    @Test
    public void testGetTopicPosts_Failure() {
        // Arrange
        Long topicId = 1L;
        when(topicRepository.findById(topicId)).thenReturn(Optional.empty());

        // Act and Assert
        Exception exception = assertThrows(Exception.class, () -> topicService.getTopicPosts(topicId));
        assertEquals("Topic not found", exception.getMessage());
        verify(topicRepository).findById(topicId);
    }
    @Test
    public void testAddTopic() {
        // Arrange
        AddTopicRequestDto topicToAdd = AddTopicRequestDto.builder()
                .title("New Topic")
                .description("Topic Description")
                .build();
        Topic expectedTopic = Topic.builder()
                .title("New Topic")
                .description("Topic Description")
                .build();
        when(topicRepository.save(any(Topic.class))).thenReturn(expectedTopic);

        // Act
        topicService.addTopic(topicToAdd);

        // Assert
        ArgumentCaptor<Topic> topicCaptor = ArgumentCaptor.forClass(Topic.class);
        verify(topicRepository).save(topicCaptor.capture());
        Topic savedTopic = topicCaptor.getValue();
        assertEquals("New Topic", savedTopic.getTitle());
        assertEquals("Topic Description", savedTopic.getDescription());
    }
    @Test
    public void testAddTopic_Failure() {
        // Arrange
        AddTopicRequestDto topicToAdd = AddTopicRequestDto.builder()
                .title("New Topic")
                .description("Topic Description")
                .build();
        when(topicRepository.save(any(Topic.class))).thenThrow(RuntimeException.class);

        // Act and Assert
        assertThrows(RuntimeException.class, () -> topicService.addTopic(topicToAdd));
        verify(topicRepository).save(any(Topic.class));
    }

}


