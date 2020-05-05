package client.handle;

import client.Authentication;
import client.Client;
import client.io.ClientMessageReceiver;

import java.nio.channels.SelectionKey;
import java.util.Iterator;
import java.util.concurrent.RecursiveAction;

public class InputHandler extends RecursiveAction {
    @Override
    protected void compute() {
        while (true) {
            try {
                while (Client.channel == null || !Client.channel.isOpen()
                        || Client.selector == null || !Client.selector.isOpen());

                while (Client.selector.select(100000) == 0);

                Iterator<SelectionKey> it = Client.selector.selectedKeys().iterator();

                while (it.hasNext()) {
                    SelectionKey key = it.next();
                    it.remove();

                    if (key.isReadable()) {

                        Client.processor.handle(new ClientMessageReceiver(Client.channel).receiveMessage());
                    }
                }

            } catch (Exception e) {
                Client.establishConnection();
            }
        }
    }
}
