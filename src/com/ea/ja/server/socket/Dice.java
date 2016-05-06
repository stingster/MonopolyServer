package com.ea.ja.server.socket;

/**
 * Dice Class
 * @author achesnoiu
 * @version 1
 */
public final class Dice {

    /**
     *
     * @return result of dice 1
     */
    public static int getDiceResult1(){
        return (int)(Math.random() * 6) + 1;
    }

    /**
     *
     * @return result of dice 2
     */
    public static int getDiceResult2(){
        return (int)(Math.random() * 6) + 1;
    }
}

