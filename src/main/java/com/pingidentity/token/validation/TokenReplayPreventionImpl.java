package com.pingidentity.token.validation;

import com.pingidentity.token.Token;

public class TokenReplayPreventionImpl implements TokenReplayPrevention
{
    /**
     * A no-arg constructor used to evaluate the implementation with automated tests.
     *
     * If your implementation requires tuning parameters, this constructor should
     * assign reasonable defaults. Please feel free to create additional constructors to
     * allow any tests you choose to implement to control these parameters.
     */
    public TokenReplayPreventionImpl()
    {
    }

    public boolean isTokenReplayed(Token token)
    {
        throw new UnsupportedOperationException("IMPLEMENT ME!");
    }
}
