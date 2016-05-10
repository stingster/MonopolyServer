package com.ea.ja.server.socket;


import com.ea.ja.server.gui.GUI;

/**
 * @author achesnoiu
 */
public class Main {

    /**
     * loading libraries
     * @param args args
     */
    public static void main(String[] args){


        System.loadLibrary("serverlib");
        new GUI();
    }
}
