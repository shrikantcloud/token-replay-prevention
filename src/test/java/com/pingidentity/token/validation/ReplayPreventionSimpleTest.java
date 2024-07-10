package com.pingidentity.token.validation;

import com.pingidentity.token.Token;
import com.pingidentity.token.TokenSignature;
import org.junit.Test;

import java.time.Instant;

import static org.junit.Assert.assertTrue;

/**
 * Simple unit tests for a {@link TokenReplayPrevention} implementation
 */
public class ReplayPreventionSimpleTest
{
    @Test
    public void testReplay()
    {
        TokenReplayPrevention replayPrevention = new TokenReplayPreventionImpl();

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

        // Lots more could be tested...
    }
}
