package com.ea.ja.server.socket;

import java.io.Serializable;

/**
 * Created by achesnoiu on 5/3/2016.
 */
public class SerializablePlayer implements Serializable{
    String username;
    int tokenId;

    public SerializablePlayer(String username, int tokenId) {
        this.tokenId = tokenId;
        this.username = username;
    }
}
