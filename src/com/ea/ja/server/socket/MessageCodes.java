package com.ea.ja.server.socket;

import java.io.Serializable;

/**
 * Created by achesnoiu on 5/3/2016.
 */
public enum MessageCodes implements Serializable{
    USER_MESSAGE,PASSWORD_MESSAGE,CONNECTION_ACCEPTED,CONNECTION_REFUSED,CONECTED_USERS_VECTOR,GAME_READY_TO_START,
    USER_POSITION,USER_END_TURN,TOKEN_ID,NUMBER_OF_PLAYERS,YOUR_TURN;

}
