package com.ea.ja.server.socket;


import com.ea.ja.server.domain.MessageThread;
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
        for(int i=1;i<=100;i++)
            MessageThread.sendOnAnotherThread();

//        System.loadLibrary("serverlib");
//        new GUI();
    }
}
