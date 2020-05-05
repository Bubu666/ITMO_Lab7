package server.database;

import network.User;

public class Users {
    private final static DataBase database = DataBase.getInstance();

    public static boolean authenticate(User user) {
        return database.checkUser(user);
    }

    public static boolean registerUser(User user) {
        return database.addUser(user);
    }

    public static User unknown() {
        return new User("unknown", "unknown");
    }
}
