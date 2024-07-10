package com.sa.token.validation;

import com.sa.token.Token;

import java.time.Instant;
import java.util.concurrent.Callable;

public class TokenPlayRequest implements Callable<TokenPlayResponse> {

    private final String tokenID;
    private final Instant notValidBeforeTime;
    private final Instant notValidAfterTime;
    private final TokenReplayPrevention tokenReplayPrevention;

    public TokenPlayRequest(TokenReplayPrevention tokenReplayPrevention, String tokenID, Instant notValidBeforeTime, Instant notValidAfterTime) {
        this.tokenReplayPrevention = tokenReplayPrevention;
        this.tokenID = tokenID;
        this.notValidBeforeTime = notValidBeforeTime;
        this.notValidAfterTime = notValidAfterTime;
    }

    @Override
    public TokenPlayResponse call() {
        Token token = new Token(tokenID, notValidBeforeTime, notValidAfterTime, null, tokenID.getBytes());
        return new TokenPlayResponse(tokenID, tokenReplayPrevention.isTokenReplayed(token), Thread.currentThread().getName());
    }
}
