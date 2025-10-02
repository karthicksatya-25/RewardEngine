package com.customer.rewardengine.service;

import com.customer.rewardengine.model.Rewards;

public interface RewardsService {
    public Rewards getRewardsByCustomerId(Long customerId);
}
