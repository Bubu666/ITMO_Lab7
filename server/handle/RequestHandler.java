package server.handle;

import network.User;
import network.command.Command;
import network.message.AuthorizationMessage;
import network.message.CommandMessage;
import network.message.Message;
import server.BlockingStorage;
import server.Server;
import server.database.Users;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.function.Consumer;
import java.util.function.Function;

public class RequestHandler extends Handler {
    private static final BlockingStorage storage = BlockingStorage.getInstance();
    private static final ForkJoinPool taskPool = new ForkJoinPool();

    @Override
    public void run() {
        try {
            while (!interrupted) {
                Request request = requestQueue.take();

                taskPool.execute(() -> {
                    Message msg = request.message;

                    if (msg instanceof CommandMessage) {
                        Server.log.info(() -> "command");

                        if (!Server.online.isOnline(request.user)) {
                            request.set(new Message("code4"));

                        } else {
                            storage.lock.lock();

                            try {
                                Command cmd = ((CommandMessage) msg).command;
                                String response = storage.apply(cmd, true);
                                Server.online.addCommand(request.user, cmd);
                                request.set(new Message(response));
                            } finally {
                                storage.lock.unlock();
                            }
                        }

                    } else if (msg instanceof AuthorizationMessage) {
                        AuthorizationMessage message = (((AuthorizationMessage) msg));

                        if (message.changingAccount) {
                            Server.log.info("disconnected by RequestHandler");
                            Server.online.disconnect(message.lastAccount);
                        }

                        Server.log.info(() -> "authorization");
                        User user = message.user;

                        if (Server.online.isOnline(user)) {
                            Server.log.info(() -> "code 2");
                            request.set(Users.unknown()).set(new Message("code2"));

                        } else if (message.registration) {

                            if (Users.registerUser(user)) {
                                Server.online.addUser(user);
                                Server.log.info(() -> "code 0");
                                request.set(user).set(new Message("code0"));

                            } else {
                                Server.log.info(() -> "code 1");
                                request.set(Users.unknown()).set(new Message("code3"));
                            }

                        } else {
                            if (Users.authenticate(((AuthorizationMessage) msg).user)) {
                                Server.log.info(() -> "code 0");
                                request.set(user).set(new Message("code0"));
                                Server.online.addUser(user);

                            } else {
                                Server.log.info(() -> "code 1");
                                request.set(Users.unknown()).set(new Message("code1"));
                            }
                        }
                    }

                    Server.log.info("Request handler adds request to send");
                    Server.outputHandler.add(request);
                });
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}