package client.handle;

import client.Authentication;
import client.Client;
import client.io.ClientMessageSender;
import network.message.Message;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class OutputHandler implements Runnable {
    private static final BlockingQueue<Message> queue = new LinkedBlockingQueue<>();

    public void send(Message message) {
        try {
            //Client.log.info("put message to send");
            queue.put(message);
            //Client.log.info("after putting");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            Message message;

            try {
                message = queue.take();
                try {

                    new ClientMessageSender(Client.channel).sendMessage(message);

                } catch (Exception e) {
                    Client.establishConnection();
                }

            } catch (InterruptedException e) {
                Client.log.warning(e::toString);
            }
        }
    }
}
