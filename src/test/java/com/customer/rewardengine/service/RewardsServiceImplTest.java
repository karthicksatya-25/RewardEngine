package com.customer.rewardengine.service;


import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import com.customer.rewardengine.entity.Customer;
import com.customer.rewardengine.entity.Transaction;
import com.customer.rewardengine.model.Rewards;
import com.customer.rewardengine.repository.CustomerRepository;
import com.customer.rewardengine.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class RewardsServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private RewardsServiceImpl rewardsService;

    @Test
    public void testGetRewardsByCustomerId() {
        Long customerId = 1L;
        Customer customer = new Customer();
        customer.setCustomerId(customerId);
        customer.setCustomerName("John Doe");
        when(customerRepository.findByCustomerId(customerId)).thenReturn(customer);
        List<Transaction> lastMonthTransactions = Arrays.asList(
                createTransaction(customerId, 120.0),
                createTransaction(customerId, 80.0)
        );
        List<Transaction> lastSecondMonthTransactions = Arrays.asList(
                createTransaction(customerId, 60.0)
        );
        List<Transaction> lastThirdMonthTransactions = Arrays.asList(
                createTransaction(customerId, 40.0)
        );
        when(transactionRepository.findAllByCustomerIdAndTransactionDateBetween(
                eq(customerId), any(Timestamp.class), any(Timestamp.class)))
                .thenReturn(lastMonthTransactions)
                .thenReturn(lastSecondMonthTransactions)
                .thenReturn(lastThirdMonthTransactions);

        Rewards rewards = rewardsService.getRewardsByCustomerId(customerId);

        assertNotNull(rewards);
        assertEquals(customerId, rewards.getCustomerId());
        assertEquals("John Doe", rewards.getCustomerName());
        assertEquals(130, rewards.getTotalRewards());
        assertEquals(130, rewards.getLastMonthRewards() + rewards.getSecondLastMonthRewards() + rewards.getThirdLastMonthRewards());
    }

    private Transaction createTransaction(Long customerId, Double amount) {
        Transaction t = new Transaction();
        t.setCustomerId(customerId);
        t.setTransactionAmount(amount);
        t.setTransactionDate(Timestamp.from(Instant.now()));
        return t;
    }
}