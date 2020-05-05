package client.command;

import client.Authentication;
import client.Client;
import network.User;
import network.command.Cmd;
import network.command.HelpInfo;
import network.message.AuthorizationMessage;

import java.lang.reflect.Method;

public class ClientCommandWorker {
    private static String helpInfo;

    static {
        StringBuilder info = new StringBuilder();
        for (Method m : ClientCommandWorker.class.getDeclaredMethods()) {
            if (m.isAnnotationPresent(HelpInfo.class)) {
                info.append(m.getAnnotation(HelpInfo.class).info());
            }
        }
        helpInfo = info.toString();
    }

    @Cmd
    @HelpInfo(info = LogOut.helpInfo)
    public static void log_out() {
        Authentication.isAuthorized = false;
        User lastAccount = Client.account;
        AuthorizationMessage message = Authentication.authenticate();
        Client.out.send(message.withLastAccount(lastAccount));
    }

    @Cmd
    @HelpInfo(info = Help.helpInfo)
    public static void help() {
        System.out.print(helpInfo);
    }

    @Cmd
    @HelpInfo(info = Exit.helpInfo)
    public static void exit() {
        Client.mainPool.shutdown();
        System.exit(0);
    }
}