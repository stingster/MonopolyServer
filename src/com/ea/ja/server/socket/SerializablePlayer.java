package com.ea.ja.server.socket;

import java.io.Serializable;

/**
 * SerializablePlayer class - EA JavaAcademy Monopoly
 * Serializable class for send informations about specific player around the LAN
 * @author achesnoiu
 * @version 1.0 (user story 1 FINAL)
 */
public class SerializablePlayer implements Serializable{

    private String username;
    private int tokenId;

    SerializablePlayer(String username, int tokenId) {
        setTokenId(tokenId);
        setUsername(username);
    }

    public int getTokenId() {
        return tokenId;
    }

    public String getUsername() {
        return username;
    }

    private void setTokenId(int tokenId) {
        this.tokenId = tokenId;
    }

    private void setUsername(String username) {
        this.username = username;
    }
}
