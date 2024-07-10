package com.sa.token.validation;

/**
 * TokenPlayResponse to wrap the result after the requests play
 */
public class TokenPlayResponse {

    private String tokenId;
    private Boolean isTokenReplayed;
    private String threadName;

    public TokenPlayResponse(String tokenId, Boolean isTokenReplayed, String threadName) {
        this.tokenId = tokenId;
        this.isTokenReplayed = isTokenReplayed;
        this.threadName = threadName;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public Boolean getTokenReplayed() {
        return isTokenReplayed;
    }

    public void setTokenReplayed(Boolean tokenReplayed) {
        isTokenReplayed = tokenReplayed;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    @Override
    public String toString() {
        return "TokenRelayResult={" +
                "tokenId='" + tokenId + '\'' +
                ", isTokenReplayed=" + isTokenReplayed +
                ", threadName='" + threadName + '\'' +
                '}';
    }
}
