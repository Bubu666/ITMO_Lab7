package client.io;

import client.Client;
import network.message.Message;
import network.message.MessageReceiver;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Field;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.List;

public class ClientMessageReceiver implements MessageReceiver {
    private final SocketChannel channel;

    private static Field position;
    private static Field limit;

    static {
        try {
            position = Buffer.class.getDeclaredField("position");
            limit = Buffer.class.getDeclaredField("limit");
            position.setAccessible(true);
            limit.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

    }

    public ClientMessageReceiver(SocketChannel channel) {
        this.channel = channel;
    }

    private byte[] readAllBytes() throws IOException {

        try (ByteArrayOutputStream byteOut = new ByteArrayOutputStream()) {

            ByteBuffer messageBuf = ByteBuffer.allocate(1024 * 32);

            //Client.log.info("waiting for reading");
            while (channel.read(messageBuf) == 0);

            //Client.log.info("start reading");
            do {
                flip(messageBuf);

                while (messageBuf.hasRemaining()) {
                    byteOut.write(messageBuf.get());
                }

                clear(messageBuf);

            } while (channel.read(messageBuf) > 0);

            return byteOut.toByteArray();
        }
    }

    private void compact(ByteBuffer buffer) {
        try {
            int pos = buffer.position();
            int lim = buffer.limit();
            byte[] buf = buffer.array();

            int rem = lim - pos;

            for (int i = 0; i < rem; ++i) {
                buf[i] = buf[pos + i];
            }

            position.setInt(buffer, buffer.limit());
            limit.setInt(buffer, buffer.capacity());

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void flip(ByteBuffer buffer) {
        try {
            limit.setInt(buffer, buffer.position());
            position.setInt(buffer, 0);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void clear(ByteBuffer buffer) {
        try {
            position.setInt(buffer, 0);
            limit.setInt(buffer, buffer.capacity());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Message receiveMessage() throws IOException, ClassNotFoundException {
        return deserializeMessage(readAllBytes());
    }

    public List<Message> receiveMessages() throws IOException {
        return deserializeMessages(readAllBytes());
    }

    private List<Message> deserializeMessages(byte[] bytes) {
        List<Message> messages = new LinkedList<>();
        try (ObjectInputStream objIn = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
            while (true) {
                messages.add((Message) objIn.readObject());
            }
        } catch (Exception e) {
            Client.log.warning(e::toString);
            return messages;
        }
    }
}
