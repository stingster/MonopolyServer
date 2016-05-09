package com.ea.ja.server.socket;

import com.ea.ja.server.DAO.Business;
import com.ea.ja.server.domain.Player;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Collections;
import java.util.Stack;
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
 * @version 1.0 (user story 1 FINAL)
 */

public final class Server implements Runnable {

    private static int LISTENING_PORT;
    private static int requiredClients;
    private static int indexOfTheCurrentPlayerTurn;
    private static int currentConnectedClients;
    private static boolean isRunning;
    private static Thread thread;
    private static Vector<Player> clients;
    private static Vector<SerializablePlayer> serializablePlayers;
    private static Stack<Integer> tokenIds;

    /**
     * static initializer
     */
    static{
        LISTENING_PORT = 8080;
        requiredClients = 1;
        indexOfTheCurrentPlayerTurn = 0;
        currentConnectedClients = 0;
        isRunning = false;
        clients = new Vector<>();
        serializablePlayers = new Vector<>();
        tokenIds = new Stack<>();
        thread = new Thread(new Server());
        for(int i = 1; i <= 8; i++)
            tokenIds.add(i);
    }

    /**
     *
     * private constructor for respecting the
     * singleton pattern, preventing instantiation
     */
    private Server(){
    }

    /**
     *
     * sets the requires clients number
     * @param requiredClients must be an integer in [2,8]
     * @throws Exception if requiredClient is not valid
     */
    public static void setRequiredClients(int requiredClients) throws Exception {
        if(requiredClients < 2 || requiredClients > 8)
            throw new Exception("Invalid Required Clients");
        Server.requiredClients = requiredClients;
    }

    /**
     *
     * start the server
     * @see com.ea.ja.server.gui.GUI
     */
    public static void startServer(){
        isRunning = true;
        thread.start();
    }

    /**
     *
     * sets the listening port
     * @param listeningPort must be an integer between 8000 and 9999
     */
    public static void setListeningPort(int listeningPort) throws Exception {
        if(listeningPort < 8000 || listeningPort > 9999)
            throw new Exception("Invalid port!");
        LISTENING_PORT = listeningPort;
    }

    /**
     *
     * stops the server, and exista the application
     */
    public static void stopServer(){
        isRunning = false;
        System.out.println("Server stopped.");
        System.exit(0);
    }

    /**
     *
     * generates serializable vector with all the current connected players
     */
    private static void generateSerializablePlayersVector(){
        serializablePlayers.addAll(clients.stream().map(player -> new SerializablePlayer(player.getUsername(), player.getToken())).collect(Collectors.toList()));
        System.out.println("SerializablePlayer vector generated!");
    }

    /**
     *
     * gives permission to turn to next player
     * @see Player
     */
    synchronized public static void nextPlayerTurn(){
        System.out.println("NEXT PLAYER TURN");
        indexOfTheCurrentPlayerTurn++;
        indexOfTheCurrentPlayerTurn %= requiredClients;
        try {
            clients.elementAt(indexOfTheCurrentPlayerTurn).sendMessage(MessageCodes.YOUR_TURN);
        } catch (InvalidRequestedCode | IOException invalidRequestedCode) {
            invalidRequestedCode.printStackTrace();
        }
    }

    /**
     *
     * sends MessageCodes.GAME_READY_TO_START to all the connected players
     * sends NUMBER_OF_PLAYERS to all the connected players
     * sends CONNECTED_USERS_VECTOR to all the connected players
     * sends YOUR_TURN to the first player connected only
     */
    private static void startGame(){
        // generate serializablePlayers
        generateSerializablePlayersVector();

        // SOUT
        System.out.println("Serializable Players:");
        for(SerializablePlayer serializablePlayer : serializablePlayers)
            System.out.println(serializablePlayer.getUsername());

        // reset all players positions in DB
        Business.dao.resetBoard();

        // SENDS VITAL INFORMATIONS TO ALL PLAYERS
        for(Player player : clients) {
            try {
                player.sendMessage(MessageCodes.NUMBER_OF_PLAYERS, requiredClients);
                player.sendMessage(MessageCodes.CONECTED_USERS_VECTOR, serializablePlayers);
                player.sendMessage(MessageCodes.GAME_READY_TO_START);
            } catch (InvalidRequestedCode | IOException invalidRequestedCode) {
                invalidRequestedCode.printStackTrace();
            }
        }

        // GIVES FIRST PLAYER PERMISSION TO MOVE
        try {
            Thread thread = new Thread(()->{
                try {
                    // ALIN'S FAULT
                    Thread.sleep(5000);
                    try {
                        clients.elementAt(0).sendMessage(MessageCodes.YOUR_TURN);
                    } catch (InvalidRequestedCode | IOException invalidRequestedCode) {
                        invalidRequestedCode.printStackTrace();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(clients.elementAt(0).getUsername() + " a primit mesaj your turn.");
            });
            thread.start();

            // SETS FIRST PLAYER TURN
            indexOfTheCurrentPlayerTurn = 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        // SOUT
        System.out.println("GAME STARTED");
    }

    /**
     *
     * informs all players, SIMULATANEOUS NOW(NO LATENCY), about username's player location's update
     * @param username username of the user
     * @param newPosition new position of the user
     * @see Player
     */
    synchronized public static void updateUserPostion(String username, int newPosition){
        // creats a vector of threads
        Vector<Thread> threads = new Vector<>();

        // using a filter, creates a thread for every player, except USERNAME, that sends newPostion
        clients.stream().filter(player -> !player.getUsername().equals(username)).forEach(player -> {
            Thread sendThread = new Thread(() -> {
                try {
                    player.sendMessage(MessageCodes.USER_POSITION, new SerializablePlayer(username, newPosition));
                } catch (InvalidRequestedCode | IOException invalidRequestedCode) {
                    invalidRequestedCode.printStackTrace();
                }
            });
            threads.add(sendThread);
        });

        // starts all the created threads
        threads.forEach(Thread::start);

        // SOUT
        System.out.println(username + " s-a mutat la pozitia " + newPosition);
    }

    /**
     * removes disconnected username from clients vector
     * @param username username of the dissconected client
     */
    synchronized public static void onUserDisconnect(String username){
        // gets player's index in clients
        int index = 0;
        for(Player player : clients)
            if(!player.getUsername().equals( username))
                index++;
            else
                break;
        if(index >= 0) {
            Player player = clients.elementAt(index);

            // add the player's token back to the tokens stack
            tokenIds.add(player.getToken());

            // closes player's resources
            try {
                player.closeResources();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // removes player from clients vector
            clients.removeElementAt(index);
        }
        System.out.println("Dissconected player " + username + " has index " + index);

        // search player in serializablePlayers
        index = 0;
        for(SerializablePlayer player : serializablePlayers)
            if(!player.getUsername().equals(username))
                index++;
            else
                break;

        // removes vector from serializablePlayers, if it's present
        if(index >= 0 && serializablePlayers.size() != 0)
            serializablePlayers.removeElementAt(index);

        // updates currentConnectedClients number
        currentConnectedClients--;
    }

    /**
     * checks if username is already connected
     * @param username username
     * @return true | false
     */
    private static boolean isUserConnected(String username){
        return Collections.binarySearch(clients,new Player(username)) >= 0;
    }

    /**
     * closes clients resources
     * @param socket socket ref
     * @param objectInputStream in ref
     * @param objectOutputStream out ref
     * @throws IOException
     */
    private static void closeResources(Socket socket,ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream) throws IOException {
        socket.close();
        objectInputStream.close();
        objectOutputStream.close();
    }

    /**
     * rejects the player
     * @param reason reason
     * @param socket socket
     * @param objectInputStream in
     * @param objectOutputStream out
     */
    private static void rejectPlayer(String reason, Socket socket, ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream) throws InvalidRequestedCode, IOException {
        objectOutputStream.writeObject(new Message(MessageCodes.CONNECTION_REFUSED, reason));
        closeResources(socket,objectInputStream,objectOutputStream);
    }

    /**
     *
     * server thread run method
     */
    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(LISTENING_PORT);
            while (isRunning) {
                System.out.println("Server listening for a new client...");
                try {
                    // creates the socket and gets credentials
                    Socket socket = serverSocket.accept();

                    ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

                    String username = (String) ((Message) objectInputStream.readObject()).getSerializableObject();
                    String password = (String) ((Message) objectInputStream.readObject()).getSerializableObject();

                    if (currentConnectedClients < requiredClients)
                        if(!isUserConnected(username))
                            if (Business.dao.logIn(username, password) != null) {
                                // pop tokenId
                                int tokenId = tokenIds.pop();

                                // CREATE THE PLAYER, ADS TO clients
                                clients.add(new Player(username, socket, objectInputStream, objectOutputStream));

                                // send / set vital informations to / about client
                                Player currentPlayer = clients.lastElement();
                                currentPlayer.sendMessage(MessageCodes.CONNECTION_ACCEPTED,"Connected. Wait for other clients!");

                                currentPlayer.sendMessage(MessageCodes.TOKEN_ID, tokenId);
                                currentPlayer.setToken(tokenId);

                                // SOUT
                                System.out.println(username + " connected.");
                                System.out.println("Token send to " + username + ": " + tokenId);

                                // game ready to start check
                                currentConnectedClients++;
                                if (currentConnectedClients == requiredClients)
                                    startGame();
                            } else
                                rejectPlayer("Invalid credentials!", socket, objectInputStream, objectOutputStream);
                        else
                            rejectPlayer("Username already connected!", socket, objectInputStream, objectOutputStream);
                    else
                        rejectPlayer("Maximum connections reached!", socket, objectInputStream, objectOutputStream);
                }catch (SocketException e){
                    System.out.println("Unknown user disconected");
                }catch (ClassNotFoundException e) {
                    System.out.println("Class not found exception.");
                    e.printStackTrace();
                }catch (InvalidRequestedCode invalidRequstedCode) {
                    System.out.println("Invalid Requested Code");
                    invalidRequstedCode.printStackTrace();
                }
            }
        }catch (IOException e) {
            System.out.println("IOException");
            e.printStackTrace();
        }
        System.err.println("SERVER STOPPED");
    }
}

