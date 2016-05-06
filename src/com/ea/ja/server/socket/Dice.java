package com.ea.ja.server.socket;

/**
 * Dice Class
 * @author achesnoiu
 * @version 1
 */
public final class Dice {

    private static int lastDiceResult1Static;
    private static int lastDiceResult2Static;

    synchronized public static int getLastDiceResult1() {
        return lastDiceResult1Static;
    }

    private static void setLastDiceResult1(int lastDiceResult1Static) {
        Dice.lastDiceResult1Static = lastDiceResult1Static;
    }

    synchronized public static int getLastDiceResult2() {
        return lastDiceResult2Static;
    }

    private static void setLastDiceResult2(int lastDiceResult2Static) {
        Dice.lastDiceResult2Static = lastDiceResult2Static;
    }

    /**
     *
     * @return result of dice 1
     */
    synchronized public static int getDiceResult1(){
        int result = (int)(Math.random() * 6) + 1;
        setLastDiceResult1(result);
        return result;
    }

    /**
     *
     * @return result of dice 2
     */
    synchronized public static int getDiceResult2(){
        int result = (int)(Math.random() * 6) + 1;
        setLastDiceResult2(result);
        return result;
    }

}

