package server.database;

import network.User;
import network.human.HumanBeing;
import server.Server;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.LinkedList;

public class DataBase {
    private static final String URL = "jdbc:postgresql://host:5432/name";
    private static final String USER = "user";
    private static final String PASSWORD = "password";

    private static DataBase dataBase;

    private static Connection connection;

    static {
        try {
            dataBase = new DataBase();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private DataBase() throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        connection = DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public void interrupt() throws SQLException {
        connection.close();
    }

    public static DataBase getInstance() {
        return dataBase;
    }

    public static void main(String[] args) {
        User admin = new User("admin", "admin1337");
        dataBase.insertHuman(admin, HumanBeing.getHuman());
        dataBase.insertHuman(admin, HumanBeing.getHuman());
        dataBase.getAllPeople().forEach(System.out::println);
    }

    public LinkedList<HumanBeing> getAllPeople() {
        LinkedList<HumanBeing> collection = new LinkedList<>();

        try (Statement statement = connection.createStatement()) {
            String sql = "select * from collection";

            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                collection.add(
                        HumanBeing.rebuild(
                                rs.getString(1), rs.getInt(2), rs.getString(3),
                                rs.getInt(4), rs.getDouble(5), rs.getBoolean(6),
                                rs.getBoolean(7), rs.getInt(8), rs.getString(9),
                                rs.getInt(10), rs.getString(11), rs.getBoolean(12),
                                rs.getTimestamp(13)
                        )
                );
            }

        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return collection;
    }

    public HumanBeing insertHuman(User user, HumanBeing human) {
        String sql = null;

        try (Statement statement = connection.createStatement()) {
            sql = "select nextval ('users_objects_id_seq');";

            ResultSet rs = statement.executeQuery(sql);
            rs.next();
            int id = rs.getInt(1);

            human.init(user.login(), id);

            sql = "insert into collection " + human.dbProperties() + " " + human.dbValues() + ";";

            if (statement.executeUpdate(sql) == 1)
                return human;

        } catch (SQLException throwable) {
            Server.log.warning(sql);
            throwable.printStackTrace();
        }
        return null;
    }

    public boolean deleteHuman(User user, HumanBeing human) {
        try (Statement statement = connection.createStatement()) {

            String sql = "delete from collection where id = " + human.getId() + ";";

            return statement.executeUpdate(sql) == 1;

        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return false;
    }

    public boolean checkUser(User user) {
        try (Statement statement = connection.createStatement()) {
            String login = user.login();
            String password = user.password();

            final String sql = "select password from users where login = '" + login + "';";

            final ResultSet rs = statement.executeQuery(sql);

            if (rs.next() && rs.getString("password").equals(password)) {
                return true;
            }

        } catch (SQLException th) {
            th.printStackTrace();
        }
        return false;
    }

    public boolean isThere(User user) {
        try (Statement statement = connection.createStatement()) {
            String login = user.login();

            final String sql = "select * from users where login = '" + login + "';";

            final ResultSet rs = statement.executeQuery(sql);

            if (rs.next()) {
                return true;
            }

        } catch (SQLException th) {
            th.printStackTrace();
        }
        return false;
    }

    public boolean addUser(User user) {
        try (Statement statement = connection.createStatement()) {
            if (!isThere(user)) {
                final String sql = "insert into users (login, password) values " +
                        "('" + user.login() + "', '" + user.password() + "');";

                return statement.executeUpdate(sql) == 1;
            }

        } catch (SQLException th) {
            th.printStackTrace();
        }
        return false;
    }
}
