package client;

import client.animation.WaitingForServerAnimation;
import client.handle.ConsoleHandler;
import client.handle.InputHandler;
import client.handle.OutputHandler;
import client.handle.Processor;
import network.User;
import network.message.AuthorizationMessage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

public class Client {
    public static final ForkJoinPool mainPool = ForkJoinPool.commonPool();
    public static SocketChannel channel;

    private static WaitingForServerAnimation waitingAnimation = new WaitingForServerAnimation();

    public static final ReentrantLock consoleLock = new ReentrantLock();

    public static ConsoleHandler console = new ConsoleHandler();

    public static User account;

    public static final OutputHandler out = new OutputHandler();
    public static final Processor processor = new Processor();
    public static final InputHandler in = new InputHandler();

    public static Selector selector;

    public static final Logger log = Logger.getLogger(Client.class.getName());

    public static void main(String[] args) {
        mainPool.execute(out);
        mainPool.execute(processor);
        establishConnection();
        mainPool.execute(console);
        mainPool.invoke(in);
    }

    public static void establishConnection() {

        mainPool.invoke(new Connection());

        if (Authentication.isAuthorized) {
            out.send(new AuthorizationMessage(account.login(), account.password(), false));
        } else {
            out.send(Authentication.authenticate());
        }

        System.out.print("\r> ");
        selector.wakeup();
    }

    private static class Connection extends RecursiveAction {
        @Override
        protected void compute() {
            int times = 0;
            while (true) {
                try {
                    if (channel != null) {
                        channel.close();
                    }

                    if (selector != null) {
                        selector.close();
                    }

                    channel = SocketChannel.open();
                    selector = Selector.open();
                    channel.configureBlocking(false);
                    channel.register(selector, SelectionKey.OP_READ);

                    channel.connect(new InetSocketAddress("localhost", 13338));

                    if (!channel.finishConnect()) {
                        ++times;

                        if (times > 1 && waitingAnimation.isInterrupted()) {
                            mainPool.execute(waitingAnimation);
                        }

                        if (times == 5) {
                            waitingAnimation.interrupt();

                            if (console.askQuit()) {
                                System.exit(0);
                            }

                            times = 0;
                            mainPool.execute(waitingAnimation);
                        }

                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        continue;
                    }

                    waitingAnimation.interrupt();
                    System.out.print("\r                                     ");

                    break;

                } catch (IOException e) {}
            }
        }
    }
}