package com.ea.ja.server.socket;

import java.io.IOException;
import java.io.ObjectOutputStream;


public interface MessageThread {

    static void sendOnAnotherThread(ObjectOutputStream objectOutputStream, Message message){
        Thread thread = new Thread(() -> {
            try {
                objectOutputStream.writeObject(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }
}
