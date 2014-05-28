package de.hawhamburg.load;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Observable;

public class DispatcherClientConnection extends Observable implements Runnable {
    private Dispatcher dispatcher;
    private Socket socket;
    private BufferedReader inStream;

    public DispatcherClientConnection(Dispatcher dispatcher, Socket socket) {
        this.dispatcher = dispatcher;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            inStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (true) {
                dispatcher.dispatch(getRequest());
            }
        } catch (IOException e) {
            // client disconnect
        }
    }

    private String getRequest() throws IOException {
        String request = inStream.readLine();
        if (request == null) {
            throw new IOException();
        }
        return request;
    }
}
