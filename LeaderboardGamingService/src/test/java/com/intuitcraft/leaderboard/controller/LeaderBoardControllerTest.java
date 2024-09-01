package com.intuitcraft.leaderboard.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intuitcraft.leaderboard.entity.LeaderBoard;
import com.intuitcraft.leaderboard.entity.PlayerScore;
import com.intuitcraft.leaderboard.services.LeaderBoardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Arrays;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class LeaderBoardControllerTest {

  @Mock
  private LeaderBoardService leaderBoardService;

  @InjectMocks
  private LeaderBoardController leaderBoardController;

  private MockMvc mockMvc;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(leaderBoardController).build();
  }

  @Test
  public void testGetTopScorers_Success() throws Exception {
    PlayerScore player1 = new PlayerScore("player1", 100);
    PlayerScore player2 = new PlayerScore("player2", 200);
    List<PlayerScore> topScorers = Arrays.asList(player1, player2);
    when(leaderBoardService.getTopNPlayers()).thenReturn(topScorers);
    mockMvc.perform(get("/getTopScorers")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].playerId").value("player1"))
        .andExpect(jsonPath("$[0].score").value(100))
        .andExpect(jsonPath("$[1].playerId").value("player2"))
        .andExpect(jsonPath("$[1].score").value(200));
  }



}
