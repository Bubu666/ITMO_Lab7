package server.handle;

import server.Server;
import server.io.ServerMessageSender;

public class OutputHandler extends Handler {
    @Override
    public void run() {
        try {
            while (!interrupted) {
                Request request = requestQueue.take();

                new Thread(() -> {
                    try {
                        ServerMessageSender sender
                                = new ServerMessageSender(request.socket.getOutputStream(), request.user.login());

                        Server.log.info(() -> "sending a message to user " + request.user.login());

                        sender.sendMessage(request.message);

                        Server.inputHandler.add(request);

                    } catch (Exception e) {
                        Server.log.warning(e::getLocalizedMessage);
                        Server.log.info("disconnected by OutputHandler");
                        Server.online.disconnect(request.user);
                    }
                }).start();
            }
        } catch (InterruptedException e) {
            Server.log.warning(e::toString);
        }
    }
}
