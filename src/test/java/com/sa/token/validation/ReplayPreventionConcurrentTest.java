package com.sa.token.validation;

import com.sa.token.Token;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.IntStream;

/**
 * Concurrency test for replaying same token using multiple threads
 */
@RunWith(JUnit4.class)
public class ReplayPreventionConcurrentTest {

    private static final String TOKEN_ID = "dummy-token-ID-1";
    private static final int CONCURRENCY_LEVEL = 5;
    private static final int NO_OF_SAME_TOKENS_TO_PLAY = 10;
    private static final int TOKEN_VALID_BEFORE_CURRENT_TIME_IN_SEC = 20;
    private static final int TOKEN_VALID_AFTER_CURRENT_TIME_IN_SEC = 60;
    private Map<String, Token> tokenStore;
    private List<TokenPlayRequest> tokenPlayRequests;
    private ExecutorService executorService;
    private TokenReplayPrevention replayPrevention;

    @Before
    public void setUp() {
        tokenStore = new ConcurrentHashMap<>();
        executorService = Executors.newFixedThreadPool(CONCURRENCY_LEVEL);
        tokenPlayRequests = new ArrayList<>();
        replayPrevention = new TokenReplayPreventionImpl(tokenStore);
    }

    @Test
    public void testReplayWithConcurrency() {
        Instant testStartTime = Instant.now();
        System.out.println("\nExecuting ReplayPreventionConcurrentTest (Multi Threaded) ...");
        System.out.println(NO_OF_SAME_TOKENS_TO_PLAY + " concurrent token plays with " + CONCURRENCY_LEVEL + " threads having the same tokenID '" + TOKEN_ID + "'");
        IntStream.range(1, NO_OF_SAME_TOKENS_TO_PLAY).forEach(i -> {
            TokenPlayRequest request = new TokenPlayRequest(
                    replayPrevention,
                    TOKEN_ID,
                    Instant.now().minusSeconds(TOKEN_VALID_BEFORE_CURRENT_TIME_IN_SEC),
                    Instant.now().plusSeconds(TOKEN_VALID_AFTER_CURRENT_TIME_IN_SEC)
            );
            tokenPlayRequests.add(request);
        });

        try {
            System.out.println("\n --- Token Play & Replay Execution Concurrently ---");
            List<Future<TokenPlayResponse>> tokenPlayResponses = executorService.invokeAll(tokenPlayRequests);
            System.out.println("\n --- Token Play & Replay Responses ---");
            tokenPlayResponses.forEach(future -> {
                try {
                    TokenPlayResponse tokenPlayResponse = future.get();
                    System.out.println(tokenPlayResponse.toString());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
        Assert.assertEquals(tokenStore.size(), 1);
        System.out.println("\nNumber of Unique tokens in the tokenStore after test = " + tokenStore.size());
        Instant testEndTime = Instant.now();
        System.out.println("ReplayPreventionConcurrentTest Completed! Execution Time taken = " + ChronoUnit.MILLIS.between(testStartTime, testEndTime) + "ms.\n");
    }

    @After
    public void tearDown() {
        tokenStore = null;
        executorService = null;
        tokenPlayRequests = null;
        replayPrevention = null;
    }
}
