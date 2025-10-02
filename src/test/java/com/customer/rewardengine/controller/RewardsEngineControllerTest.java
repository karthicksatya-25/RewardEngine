package com.customer.rewardengine.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.customer.rewardengine.service.RewardsService;
import com.customer.rewardengine.model.Rewards;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;

@ExtendWith(MockitoExtension.class)
public class RewardsEngineControllerTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private RewardsService rewardsService;

    @InjectMocks
    private RewardsEngineController rewardsEngineController;

    private Rewards rewards;

    @BeforeEach
    public void setup() {

        rewards = new Rewards();
        rewards.setCustomerId(1001L);
        rewards.setTotalRewards(130);
        mockMvc = MockMvcBuilders.standaloneSetup(rewardsEngineController).build();
    }

    @Test
    public void testGetRewardsByCustomerId_Success() throws Exception {
        when(rewardsService.getRewardsByCustomerId(1L)).thenReturn(rewards);
        mockMvc.perform(get("/customers/1/rewards"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.customerId").value(1001L))
                .andExpect(jsonPath("$.totalRewards").value(130));
    }
}
