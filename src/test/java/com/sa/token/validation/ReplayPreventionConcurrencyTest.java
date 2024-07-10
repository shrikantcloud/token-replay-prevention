package com.sa.token.validation;

import com.sa.token.Token;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@RunWith(JUnit4.class)
public class ReplayPreventionConcurrencyTest {
    private final Map<String, Token> tokenStore = new ConcurrentHashMap<>();

    @Test
    public void testReplayWithConcurrency() {
        ExecutorService executorService = Executors.newFixedThreadPool(8);
        List<TokenPlayRequest> tokenPlayRequests = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            TokenReplayPrevention replayPrevention = new TokenReplayPreventionImpl(tokenStore);
            TokenPlayRequest request = new TokenPlayRequest(
                    replayPrevention,
                    "dummy-token-ID-1",
                    Instant.now().minusSeconds(20),
                    Instant.now().plusSeconds(60)
            );
            tokenPlayRequests.add(request);
        }

        List<Future<TokenPlayResponse>> tokenPlayResponses = null;
        try {
            tokenPlayResponses = executorService.invokeAll(tokenPlayRequests);
            for (Future<TokenPlayResponse> response : tokenPlayResponses) {
                TokenPlayResponse tokenPlayResponse = response.get();
                System.out.println(tokenPlayResponse.toString());
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
        Assert.assertEquals(tokenStore.size(), 1);
    }
}
