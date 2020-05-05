package server;

import network.User;
import network.command.Command;
import server.database.DataBase;
import server.database.Users;
import server.handle.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ForkJoinPool;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    public final ServerSocket serverSocket;

    public final static Logger log = Logger.getLogger(Server.class.getName());

    public static final InputHandler inputHandler = new InputHandler(32);
    public static final OutputHandler outputHandler = new OutputHandler();
    public static final RequestHandler requestHandler = new RequestHandler();
    public static final Online online = new Online();
    public static final DataBase database = DataBase.getInstance();

    public Server() throws IOException {
        serverSocket = new ServerSocket(13338);
        log.log(Level.INFO, "Server: " + InetAddress.getLocalHost());
    }

    public void start() {
        ForkJoinPool.commonPool().execute(new ServerConsole());
        log.log(Level.INFO, "Server: start working");
        try {
            while (true) {
                Socket socket = serverSocket.accept();

                log.log(Level.INFO, "client: " + socket.getInetAddress().getHostName() + ": connection established");

                inputHandler.add(new Request(Users.unknown(), socket, null));
            }
        } catch (IOException e) {
            log.log(Level.WARNING, e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            new Server().start();
        } catch (IOException e) {
            log.log(Level.WARNING, e.getMessage());
        }
    }

    public static class Online {
        private final ConcurrentMap<User, LinkedList<Command>> users = new ConcurrentHashMap<>();

        public boolean addUser(User user) {
            if (users.get(user) == null) {
                users.put(user, new LinkedList<>());
                return true;
            }
            return false;
        }

        public Set<User> users() {
            return users.keySet();
        }

        public boolean isOnline(User user) {
            return users.get(user) != null;
        }

        public void disconnect(User user) {
            if (user == null)
                throw new NullPointerException("Null param");

            if (users.remove(user) != null)
                log.info(() -> "Server: user " + user.login() + " disconnected");
        }

        public boolean addCommand(User user, Command command) {
            if (user == null || command == null)
                throw new NullPointerException("Null param");

            LinkedList<Command> history = users.get(user);
            if (history == null)
                return false;

            history.addFirst(command);
            if (history.size() > 5)
                history.removeLast();

            return true;
        }

        public LinkedList<Command> getHistoryFor(User user) {
            return users.get(user);
        }
    }

    public static void quit() {
        try {
            Handler.interrupt();
            database.interrupt();
        } catch (Exception e) {
            Server.log.warning(e::toString);
        }
        System.exit(0);
    }
}