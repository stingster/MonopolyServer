package com.ea.ja.server.socket;
import java.io.Serializable;

/**
 * @author achesnoiu
 * MessageCodes enum
 * @version 1
 */
public enum MessageCodes implements Serializable{


    USER_MESSAGE,
    PASSWORD_MESSAGE,
    CONNECTION_ACCEPTED,
    CONNECTION_REFUSED,
    CONNECTED_USERS_VECTOR,
    GAME_READY_TO_START,
    USER_POSITION,
    USER_END_TURN,
    TOKEN_ID,
    NUMBER_OF_PLAYERS,
    YOUR_TURN,
    I_AM_READY_TO_START,
    GET_DICE,
    DICE_RESULT,
    INVALID_MOVE,
    DICE_CHANGED;

    private static final long serialVersionUID = 52L;
}
