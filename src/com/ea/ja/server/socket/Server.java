package com.ea.ja.server.socket;

import com.ea.ja.server.DAO.Business;
import com.ea.ja.server.DAO.DAO;
import com.ea.ja.server.DAO.DAOImpl;
import com.ea.ja.server.domain.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public final class Server implements Runnable {

    private static int LISTENING_PORT = 8080;
    private static int requiredClients = 1;
    private static int currentConnectedClients = 0;
    private static Server server = new Server();
    private static Thread thread;
    private static boolean isRunning;
    private static Vector<Player> clients = new Vector<>();
    private static DAO dao;
    private static Vector<SerializablePlayer> serializablePlayers = new Vector<>();
    private static int indexOfTheCurrentPlayerTurn;

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
        thread.interrupt();
        System.out.println("Server stopped.");
    }

    /**
     * generates serializabel player vector
     */
    private static void generateSerializablePlayerVector(){
        for(Player player : clients)
            serializablePlayers.add(new SerializablePlayer(player.getUsername(),player.getToken()));
        System.out.println("SerializablePlayer vector generated!");
    }

    /**
     * gives permission to turn to next player
     */
    public static void nextPlayerTurn(){
        System.out.println("NEXT PLAYER TURN");
        indexOfTheCurrentPlayerTurn++;
        indexOfTheCurrentPlayerTurn %= requiredClients;
        try {
            clients.elementAt(indexOfTheCurrentPlayerTurn).sendMessage(MessageCodes.YOUR_TURN, null);
        } catch (InvalidRequestedCode invalidRequestedCode) {
            invalidRequestedCode.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * sends the start game message
     */
    private static void startGame(){
        System.out.println("GAME STARTED");
        generateSerializablePlayerVector();
        for(Player player : clients) {
            try {
                player.sendMessage(MessageCodes.NUMBER_OF_PLAYERS, requiredClients);
                player.sendMessage(MessageCodes.CONECTED_USERS_VECTOR, serializablePlayers);
                player.sendMessage(MessageCodes.GAME_READY_TO_START, null);
            } catch (InvalidRequestedCode invalidRequestedCode) {
                invalidRequestedCode.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            // RANDUL PRIMULUI JUCATOR
            Thread thread = new Thread(()->{

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    clients.elementAt(0).sendMessage(MessageCodes.YOUR_TURN,null);
                } catch (InvalidRequestedCode invalidRequestedCode) {
                    invalidRequestedCode.printStackTrace();
                } catch (IOException e) {
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
     * informs all player about a player location update
     * @param username username of the user
     * @param newPosition new position of the user
     */
    public static void updateUserPostion(String username, int newPosition){
        System.out.println(username + " s-a mutat la pozitia " + newPosition);
        for(Player player : clients)
            try {
                player.sendMessage(MessageCodes.USER_POSITION, new SerializablePlayer(username,newPosition));
            } catch (InvalidRequestedCode invalidRequestedCode) {
                invalidRequestedCode.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    /**
     * server thread run method
     */
    @Override
    public void run() {
//        dao = new DAOImpl();
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
    }
}
