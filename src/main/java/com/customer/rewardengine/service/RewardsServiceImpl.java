package com.customer.rewardengine.service;

import com.customer.rewardengine.constants.RewardsEngineConstants;
import com.customer.rewardengine.entity.Customer;
import com.customer.rewardengine.entity.Transaction;
import com.customer.rewardengine.model.Rewards;
import com.customer.rewardengine.repository.CustomerRepository;
import com.customer.rewardengine.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RewardsServiceImpl implements RewardsService{

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Override
    public Rewards getRewardsByCustomerId(Long customerId) {

        Customer customer = customerRepository.findByCustomerId(customerId);
        if(customer == null)
        {
            throw new RuntimeException("Invalid / Missing customer Id ");
        }

        Timestamp lastMonthTimestamp = getDateBasedOnOffSetDays(RewardsEngineConstants.daysInMonths);
        Timestamp lastSecondMonthTimestamp = getDateBasedOnOffSetDays(2*RewardsEngineConstants.daysInMonths);
        Timestamp lastThirdMonthTimestamp = getDateBasedOnOffSetDays(3*RewardsEngineConstants.daysInMonths);

        List<Transaction> lastMonthTransactions = transactionRepository.findAllByCustomerIdAndTransactionDateBetween(
                customerId, lastMonthTimestamp, Timestamp.from(Instant.now()));
        List<Transaction> lastSecondMonthTransactions = transactionRepository
                .findAllByCustomerIdAndTransactionDateBetween(customerId, lastSecondMonthTimestamp, lastMonthTimestamp);
        List<Transaction> lastThirdMonthTransactions = transactionRepository
                .findAllByCustomerIdAndTransactionDateBetween(customerId, lastThirdMonthTimestamp,
                        lastSecondMonthTimestamp);

        Integer lastMonthRewardPoints = getRewardsPerMonth(lastMonthTransactions);
        Integer lastSecondMonthRewardPoints = getRewardsPerMonth(lastSecondMonthTransactions);
        Integer lastThirdMonthRewardPoints = getRewardsPerMonth(lastThirdMonthTransactions);

        Rewards customerRewards = new Rewards();
        customerRewards.setCustomerId(customerId);
        customerRewards.setCustomerName(customer.getCustomerName());
        customerRewards.setLastMonthRewards(lastMonthRewardPoints);
        customerRewards.setSecondLastMonthRewards(lastSecondMonthRewardPoints);
        customerRewards.setThirdLastMonthRewards(lastThirdMonthRewardPoints);
        customerRewards.setTotalRewards(lastMonthRewardPoints + lastSecondMonthRewardPoints + lastThirdMonthRewardPoints);
        return customerRewards;

    }

    private Integer getRewardsPerMonth(List<Transaction> transactions) {
        return transactions.stream().map(transaction -> calculateRewards(transaction))
                .collect(Collectors.summingInt(r -> r.intValue()));
    }

    private Integer calculateRewards(Transaction t) {
        if (t.getTransactionAmount() > RewardsEngineConstants.firstRewardLimit && t.getTransactionAmount() <= RewardsEngineConstants.secondRewardLimit) {
            return (int) Math.round(t.getTransactionAmount() - RewardsEngineConstants.firstRewardLimit);
        } else if (t.getTransactionAmount() > RewardsEngineConstants.secondRewardLimit) {
            return (int) (Math.round(t.getTransactionAmount() - RewardsEngineConstants.secondRewardLimit) * 2
                    + (RewardsEngineConstants.secondRewardLimit - RewardsEngineConstants.firstRewardLimit));
        } else
            return 0;

    }

    public Timestamp getDateBasedOnOffSetDays(int days) {
        return Timestamp.valueOf(LocalDateTime.now().minusDays(days));
    }
}
