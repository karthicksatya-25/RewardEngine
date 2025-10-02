package com.customer.rewardengine.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Rewards {

    private Long customerId;
    private String customerName;
    private Integer totalRewards;
    private Integer lastMonthRewards;
    private Integer secondLastMonthRewards;
    private Integer thirdLastMonthRewards;

}
