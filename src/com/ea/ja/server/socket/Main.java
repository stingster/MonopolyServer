package com.ea.ja.server.socket;


import com.ea.ja.server.gui.GUI;

/**
 * Created by achesnoiu on 4/28/2016.
 */
public class Main {

    /**
     * loading libraries
     * @param args
     */
    public static void main(String[] args){

        System.loadLibrary("serverlib");
        new GUI();
    }
}
