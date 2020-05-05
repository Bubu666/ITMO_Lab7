package server;

import network.User;

import java.util.Scanner;

public class ServerConsole implements Runnable {
    private Scanner input = new Scanner(System.in);

    @Override
    public void run() {
        while (true) {
            String[] command = input.nextLine().split(" ");

            if (command.length < 1)
                continue;

            switch (command[0]) {
                case "exit":
                case "quit":
                    Server.quit();
                    return;
                case "online":
                    Server.online.users().stream().map(User::login).forEach(System.out::println);
                    break;
            }
        }
    }
}