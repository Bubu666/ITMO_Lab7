package server.io;

import network.message.Message;
import network.message.MessageReceiver;
import server.Server;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerMessageReceiver implements MessageReceiver {
    private final InputStream sockIn;
    private final String clientName;
    private final Logger logger = Server.log;

    public ServerMessageReceiver(InputStream sockIn, String clientName) {
        this.sockIn = sockIn;
        this.clientName = clientName;
    }

    @Override
    public Message receiveMessage() throws IOException, ClassNotFoundException {
        Message receivedMessage = deserializeMessage(readAllBytes());
        logger.log(Level.INFO, "Client: " + clientName + ": received object: " + receivedMessage);
        return receivedMessage;
    }

    public byte[] readAllBytes() throws IOException {
        final int DEFAULT_BUFFER_SIZE = 1024 * 512;
        final ArrayList<byte[]> buffs = new ArrayList<>();

        byte[] buf = new byte[DEFAULT_BUFFER_SIZE];

        int num;
        while ((num = sockIn.read(buf)) > 0) {
            buffs.add(buf);

            if (num < buf.length)
                break;

            buf = new byte[DEFAULT_BUFFER_SIZE];
        }

        byte[] result = new byte[buffs.stream().mapToInt(b -> b.length).sum()];

        int position = 0;

        for (byte[] b: buffs) {
            for (byte val : b) {
                result[position++] = val;
            }
        }

        return result;
    }
}