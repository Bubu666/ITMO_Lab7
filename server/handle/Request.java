package server.handle;

import network.User;
import network.message.Message;

import java.net.Socket;
import java.util.Objects;

public class Request {
    public final Socket socket;
    public User user;
    public Message message;

    public Request(User user, Socket socket, Message message) {
        this.socket = socket;
        this.user = user;
        this.message = message;
    }

    public Request set(Message message) {
        this.message = message;
        return this;
    }

    public Request set(User user) {
        this.user = user;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Request)) return false;
        Request request = (Request) o;
        return socket.equals(request.socket);
    }

    @Override
    public int hashCode() {
        return Objects.hash(socket);
    }
}