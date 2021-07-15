import handle.HandClient;
import server.IStateListener;
import server.SocketService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    public static void main(String[] args) {

        SocketService ss = new SocketService(new IStateListener() {

            @Override
            public synchronized void onReceive(HandClient client, byte[] bytes) {


            }

            @Override
            public synchronized void onSend(HandClient client, byte[] bytes) {

            }

            @Override
            public synchronized void onDisconnect(HandClient client) {

            }

            @Override
            public synchronized void onError(HandClient client) {

            }

            @Override
            public synchronized void onParseError(HandClient client, String err) {

            }

            @Override
            public synchronized void onSendError(HandClient client, String err) {

            }
        });
        ss.startServe();
    }
}
