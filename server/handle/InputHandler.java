package server.handle;

import network.message.Message;
import server.Server;
import server.io.ServerMessageReceiver;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class InputHandler extends Handler {
    private final Executor pool;

    public InputHandler(int nThreads) {
        pool = Executors.newFixedThreadPool(nThreads);
    }

    @Override
    public void run() {
        try {
            while (!interrupted) {
                Request request = requestQueue.take();

                pool.execute(() -> {
                    try {
                        ServerMessageReceiver receiver
                                = new ServerMessageReceiver(request.socket.getInputStream(), request.user.login());

                        Server.log.info(() -> "waiting for client message from user " + request.user.login());
                        Message message = receiver.receiveMessage();

                        Server.requestHandler.add(request.set(message));

                    } catch (IOException | ClassNotFoundException e) {
                        Server.log.warning(e::toString);
                        Server.log.info("disconnected by InputHandler");
                        Server.online.disconnect(request.user);
                    }
                });
            }
        } catch (InterruptedException ignored) { }
    }
}