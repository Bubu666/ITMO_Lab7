package network.message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public interface MessageSender {

    void sendMessage(Message message) throws IOException;

    default byte[] serializeMessage(Message message) throws IOException {
        final ByteArrayOutputStream byteOut = new ByteArrayOutputStream();

        try (final ObjectOutputStream objOut = new ObjectOutputStream(byteOut)) {
            objOut.writeObject(message);
        }

        return byteOut.toByteArray();
    }
}
