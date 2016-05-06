package com.ea.ja.server.socket;

import java.io.Serializable;

/**
 * @author achesnoiu
 * MessageCodes enum
 * @version 1
 */
public enum MessageCodes implements Serializable{

    CONNECTION_ACCEPTED,
    CONNECTION_REFUSED,
    CONECTED_USERS_VECTOR,
    GAME_READY_TO_START,
    USER_POSITION,
    USER_END_TURN,
    TOKEN_ID,
    NUMBER_OF_PLAYERS,
    YOUR_TURN,
    I_AM_READY_TO_START,
    GET_DICE,
    DICE_RESULT,
    INVALID_MOVE
}
