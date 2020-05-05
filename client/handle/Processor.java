package client.handle;

import client.Authentication;
import client.Client;
import client.ConsoleParser;
import network.message.CommandMessage;
import network.message.Message;
import network.command.Command;
import network.storageCommands.Exit;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Processor implements Runnable {
    public static final BlockingQueue<Message> queue = new LinkedBlockingQueue<>();

    public void handle(Message message) {
        try {
            queue.put(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                String answer = queue.take().content;

                if (answer.startsWith("code")) {
                    if (!Authentication.checkReturnedCode(answer.charAt(4))) {
                        Client.out.send(Authentication.authenticate());
                    }
                } else {
                    System.out.print("\r" + answer + "\n> ");
                }
            }
        } catch (InterruptedException e) {
            Client.log.warning(e::toString);
        }
    }
}
