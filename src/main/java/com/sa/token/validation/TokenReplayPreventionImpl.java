package com.sa.token.validation;

import com.sa.token.Token;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class TokenReplayPreventionImpl implements TokenReplayPrevention {

    private final ReentrantLock lock;
    private final Map<String, Token> tokenStore;

    /**
     * A no-arg constructor used to evaluate the implementation with automated tests.
     * <p>
     * If your implementation requires tuning parameters, this constructor should
     * assign reasonable defaults. Please feel free to create additional constructors to
     * allow any tests you choose to implement to control these parameters.
     */
    public TokenReplayPreventionImpl() {
        tokenStore = new ConcurrentHashMap<>();
        lock = new ReentrantLock(true);
    }

    /**
     * Parameterized constructor added for Multi threaded tests
     */
    public TokenReplayPreventionImpl(final Map<String, Token> tokenStore) {
        this.tokenStore = tokenStore;
        lock = new ReentrantLock(true);
    }

    public boolean isTokenReplayed(Token token) {
        lock.lock();
        try {
            String tokenID = token.getTokenID();
            System.out.println("TokenID played : " + tokenID + " , threadName : " + Thread.currentThread().getName());
            if (tokenStore.containsKey(tokenID)) {
                return true;
            }
            tokenStore.put(tokenID, token);
            cleanupStaleTokens();
        } finally {
            lock.unlock();
        }
        return false;
    }

    private void cleanupStaleTokens() {
        for (String tokenId : tokenStore.keySet()) {
            Token token = tokenStore.get(tokenId);
            if (token.getNotValidAfter().isBefore(Instant.now())) {
                tokenStore.remove(tokenId);
            }
        }
    }
}
