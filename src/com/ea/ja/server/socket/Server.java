package com.ea.ja.server.socket;

import com.ea.ja.server.domain.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

final class Server implements Runnable {

	//comentariu adaugat de alex
	//eu eu eu eu e
    //comentariu adaugat tot de ales comentariu nou afsasfgfaesdgaes
    // al doilea comentariu adaugat

    private static final int MINIMUM_NUMBER_OF_CLIENTS = 2;
    private static int LISTENING_PORT = 8080;
    private static int MAXIMUM_NUMBER_OF_CLIENTS = 8;
    private static int requiredClients = 4;
    private static int currentConnectedClients = 0;
    private static Server server = new Server();
    private static Thread thread;
    private static boolean isRunning;
    private static Vector<Player> clients = new Vector<>();
    /**
     * private constructor
     * singleton pattern
     */
    private Server(){
        thread = new Thread(this);
    }

    public static int getRequiredClients() {
        return requiredClients;
    }

    public static void setRequiredClients(int requiredClients) {
        Server.requiredClients = requiredClients;
    }

    public static void startServer(){
        isRunning = true;
        thread.start();
    }

    /**
     * sets the maximum number of clients
     * @param maximumNumberOfClients
     */
    public static void setMaximumNumberOfClients(int maximumNumberOfClients) throws Exception {
        if(maximumNumberOfClients < 2)
            throw new Exception("Invalid maximum number of clients!");
        MAXIMUM_NUMBER_OF_CLIENTS = maximumNumberOfClients;
    }

    /**
     * set the listening port
     * @param listeningPort
     */
    public static void setListeningPort(int listeningPort) throws Exception {
        if(listeningPort < 8000 || listeningPort > 9999)
            throw new Exception("Invalid port!");
        LISTENING_PORT = listeningPort;
    }

    /**
     * stops the server
     */
    public static void stopServer(){
        isRunning = false;
    }

    public static void startGame(){

    }

    /**
     * server thread run method
     */
    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(LISTENING_PORT);
            while (isRunning) {
                System.out.println("Server listening for a new client");
                Socket socket = serverSocket.accept();
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                String username = (String) ((Message)objectInputStream.readObject()).getSerializableObject();
                String password = (String) ((Message)objectInputStream.readObject()).getSerializableObject();
                if(currentConnectedClients < MAXIMUM_NUMBER_OF_CLIENTS && currentConnectedClients < requiredClients)
                    if (true) {
                        // if credentials are ok
                        currentConnectedClients++;
                        objectOutputStream.writeObject(new Message(3, "You have connected."));
                        clients.add(new Player(username,socket,objectInputStream,objectOutputStream));
                        System.out.println(username + " connected.");
                        if(currentConnectedClients == requiredClients)
                            startGame();
                    } else {
                        objectOutputStream.writeObject(new Message(4, "Username / password invalid!"));
                        objectInputStream.close();
                        objectOutputStream.close();
                        socket.close();
                    }
                else{
                    objectOutputStream.writeObject(new Message(4, "Maximum connexions reached."));
                    objectInputStream.close();
                    objectOutputStream.close();
                    socket.close();
                }
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found exception.");
            e.printStackTrace();
        } catch (InvalidRequestedCode invalidRequstedCode) {
            System.out.println("Invalid Requested Code");
            invalidRequstedCode.printStackTrace();
        } catch (IOException e) {
            System.out.println("IOException");
            e.printStackTrace();
        }
    }
}
