package server.io;

import network.message.Message;
import network.message.MessageSender;
import server.Server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerMessageSender implements MessageSender {
    private OutputStream sockOut;
    private String clientName;
    private final Logger logger = Server.log;

    public ServerMessageSender(OutputStream sockOut, String clientName) throws IOException {
        this.sockOut = sockOut;
        this.clientName = clientName;
    }

    public void sendMessages(Message[] messages) throws IOException {
        final ByteArrayOutputStream byteOut = new ByteArrayOutputStream();

        try (final ObjectOutputStream objOut = new ObjectOutputStream(byteOut)) {
            for (Message m : messages) {
                objOut.writeObject(m);
                sockOut.write(byteOut.toByteArray());
                sockOut.flush();
                byteOut.reset();
            }
        }

        logger.log(Level.INFO, "Server: sent messages to client " + clientName);
    }


    @Override
    public void sendMessage(Message message) throws IOException {
        sockOut.write(serializeMessage(message));
        sockOut.flush();

        logger.log(Level.INFO, "Server: sent message to client " + clientName);
    }
}
