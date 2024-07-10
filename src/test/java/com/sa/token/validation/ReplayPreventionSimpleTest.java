package com.sa.token.validation;

import com.sa.token.Token;
import com.sa.token.TokenSignature;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.Assert.assertTrue;

/**
 * Simple unit tests for a {@link TokenReplayPrevention} implementation
 */
@RunWith(JUnit4.class)
public class ReplayPreventionSimpleTest {

    private TokenReplayPrevention replayPrevention;

    @Before
    public void setUp() {
        replayPrevention = new TokenReplayPreventionImpl();
    }

    @Test
    public void testReplay() {
        Instant testStartTime = Instant.now();
        System.out.println("Executing ReplayPreventionSimpleTest (Single Threaded) ...");
        // Create a test Token to test the TokenReplayPrevention

        // A dummy token ID
        String tokenID = "dummy-token-ID-1";

        // Some validity dates on the token
        Instant now = Instant.now();
        Instant notBefore = now.minusSeconds(20);
        Instant notAfter = now.plusSeconds(60);

        // For testing, just convert the tokenID to bytes for the raw token value.  A real token might have more stuff
        // but this is sufficient for testing the replay prevention
        byte[] rawToken = tokenID.getBytes();

        // This TokenReplayPrevention class shouldn't even look at the signature so we'll just leave it null
        TokenSignature tokenSignature = null;

        // Create a test Token
        Token token = new Token(tokenID, notBefore, notAfter, tokenSignature, rawToken);

        // Now do some tests

        // The first check shouldn't be a replay because it's the first time TokenReplayPrevention object has seen it
        assertTrue(!replayPrevention.isTokenReplayed(token));

        // The second check should be a replay because we are giving TokenReplayPrevention the same token again
        assertTrue(replayPrevention.isTokenReplayed(token));
        Instant testEndTimeTime = Instant.now();
        // Lots more could be tested...
        System.out.println("ReplayPreventionSimpleTest Completed! Execution Time taken = " + ChronoUnit.MILLIS.between(testStartTime, testEndTimeTime) + "ms.\n");
    }

    @After
    public void tearDown() {
        replayPrevention = null;
    }
}
