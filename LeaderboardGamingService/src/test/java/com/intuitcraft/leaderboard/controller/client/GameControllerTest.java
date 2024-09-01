package com.intuitcraft.leaderboard.controller.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intuitcraft.leaderboard.entity.PlayerScore;
import com.intuitcraft.leaderboard.services.ScoreIngestionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GameControllerTest {

  @Mock
  private ScoreIngestionService scoreIngestor;

  @InjectMocks
  private GameController gameController;

  private MockMvc mockMvc;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(gameController).build();
  }

  @Test
  public void testUpdateScore_Success() throws Exception {
    PlayerScore playerScore = new PlayerScore("player1", 100L);

    mockMvc.perform(post("/updateScore")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(playerScore)))
        .andExpect(status().isOk());

    verify(scoreIngestor).publish(playerScore);
  }

  @Test
  public void testUpdateScore_Failure() throws Exception {
    PlayerScore playerScore = new PlayerScore("player1", 100L);
    doThrow(new RuntimeException("Update failed")).when(scoreIngestor).publish(playerScore);
    mockMvc.perform(post("/updateScore")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(playerScore)))
        .andExpect(status().isInternalServerError());
    verify(scoreIngestor).publish(playerScore);
  }
}
