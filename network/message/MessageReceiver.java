package network.message;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public interface MessageReceiver {

    Message receiveMessage() throws IOException, ClassNotFoundException;

    default Message deserializeMessage(byte[] byteMessage) throws IOException, ClassNotFoundException {
        try (ObjectInputStream objIn = new ObjectInputStream(new ByteArrayInputStream(byteMessage))) {
            return (Message) objIn.readObject();
        }
    }
}
