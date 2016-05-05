package com.ea.ja.server.socket;

import com.ea.ja.server.DAO.Business;
import com.ea.ja.server.domain.Player;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import java.util.stream.Collectors;

/**
 * EA JavaAcademy Monopoly Server Class
 * Stable server for the Monopoly game application
 * running using Java Sockets
 * each client runs on a separate thread
 * all client sockets listens in the same time for requests
 * messages can be sent to all clients, publically or private
 * @author achesnoiu
 * @version 1.0
 */

public final class Server implements Runnable {

    private static int LISTENING_PORT = 8080;
    private static int requiredClients = 1;
    private static int currentConnectedClients = 0;
    private static Server server = new Server();
    private static Thread thread;
    private static boolean isRunning;
    private static Vector<Player> clients = new Vector<>();
    private static Vector<SerializablePlayer> serializablePlayers = new Vector<>();
    private static int indexOfTheCurrentPlayerTurn;

    /**
     * @author achesnoiu
     * private constructor for respecting the
     * singleton pattern
     */
    private Server(){
        thread = new Thread(this);
    }


    /**
     * @author achesnoiu
     * sets the requires clients number
     * @param requiredClients must be an integer in [2,8]
     */
    public static void setRequiredClients(int requiredClients) {
        Server.requiredClients = requiredClients;
    }

    /**
     * @author achesnoiu
     * start the server
     * @see com.ea.ja.server.gui.GUI
     */
    public static void startServer(){
        isRunning = true;
        thread.start();
    }

    /**
     * @author achesnoiu
     * sets the listening port
     * @param listeningPort must be an integer between 8000 and 9999
     */
    public static void setListeningPort(int listeningPort) throws Exception {
        if(listeningPort < 8000 || listeningPort > 9999)
            throw new Exception("Invalid port!");
        LISTENING_PORT = listeningPort;
    }

    /**
     * @author achesnoiu
     * stops the server, and exista the application
     */
    public static void stopServer(){
        isRunning = false;
        System.out.println("Server stopped.");
        System.exit(0);
    }

    /**
     * @author achesnoiu
     * generates serializable vector with all the current connected players
     */
    private static void generateSerializablePlayerVector(){
        serializablePlayers.addAll(clients.stream().map(player -> new SerializablePlayer(player.getUsername(), player.getToken())).collect(Collectors.toList()));
        System.out.println("SerializablePlayer vector generated!");
    }

    /**
     * @author achesnoiu
     * gives permission to turn to next player
     */
    public static void nextPlayerTurn(){
        System.out.println("NEXT PLAYER TURN");
        indexOfTheCurrentPlayerTurn++;
        indexOfTheCurrentPlayerTurn %= requiredClients;
        try {
            clients.elementAt(indexOfTheCurrentPlayerTurn).sendMessage(MessageCodes.YOUR_TURN, null);
        } catch (InvalidRequestedCode | IOException invalidRequestedCode) {
            invalidRequestedCode.printStackTrace();
        }
    }

    /**
     * @author achesnoiu
     * sends MessageCodes.GAME_READY_TO_START to all the connected players
     * sends NUMBER_OF_PLAYERS to all the connected players
     * sends CONNECTED_USERS_VECTOR to all the connected players
     * sends YOUR_TURN to the first player connected only
     */
    private static void startGame(){
        System.out.println("GAME STARTED");
        generateSerializablePlayerVector();
        for(Player player : clients) {
            try {
                player.sendMessage(MessageCodes.NUMBER_OF_PLAYERS, requiredClients);
                player.sendMessage(MessageCodes.CONECTED_USERS_VECTOR, serializablePlayers);
                player.sendMessage(MessageCodes.GAME_READY_TO_START, null);
            } catch (InvalidRequestedCode | IOException invalidRequestedCode) {
                invalidRequestedCode.printStackTrace();
            }
        }

        try {
            // RANDUL PRIMULUI JUCATOR
            Thread thread = new Thread(()->{
                try {
                    Thread.sleep(5000);

                    try {
                        clients.elementAt(0).sendMessage(MessageCodes.YOUR_TURN,null);
                    } catch (InvalidRequestedCode | IOException invalidRequestedCode) {
                        invalidRequestedCode.printStackTrace();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(clients.elementAt(0).getUsername() + " a primit mesaj your turn.");
            });
            thread.start();
            indexOfTheCurrentPlayerTurn = 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @author achesnoiu
     * informs all players about username's player location's update
     * @param username username of the user
     * @param newPosition new position of the user
     */
    public static void updateUserPostion(String username, int newPosition){
        System.out.println(username + " s-a mutat la pozitia " + newPosition);
        for(Player player : clients)
            try {
                player.sendMessage(MessageCodes.USER_POSITION, new SerializablePlayer(username,newPosition));
            } catch (InvalidRequestedCode | IOException invalidRequestedCode) {
                invalidRequestedCode.printStackTrace();
            }
    }

    /**
     * @author achesnoiu
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
                if(currentConnectedClients < requiredClients)
//                  if(true){
                    if (Business.dao.logIn(username, password) != null) {
                        // if credentials are ok
                        currentConnectedClients++;
                        objectOutputStream.writeObject(new Message(MessageCodes.CONNECTION_ACCEPTED, "You have connected."));
                        // ID TOKEN
                        objectOutputStream.writeObject(new Message(MessageCodes.TOKEN_ID,currentConnectedClients));
                        System.out.println("TOKEN ID SENT TO " + username + ": " + currentConnectedClients);
                        clients.add(new Player(username,socket,objectInputStream,objectOutputStream));
                        clients.lastElement().setToken(currentConnectedClients);
                        System.out.println(username + " connected.");
                        if(currentConnectedClients == requiredClients)
                            startGame();
                    } else {
                        objectOutputStream.writeObject(new Message(MessageCodes.CONNECTION_REFUSED, "Username / password invalid!"));
                        objectInputStream.close();
                        objectOutputStream.close();
                        socket.close();
                    }
                else{
                    objectOutputStream.writeObject(new Message(MessageCodes.CONNECTION_REFUSED, "Maximum connexions reached."));
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
        System.err.println("SERVER STOPPED");
    }
}
