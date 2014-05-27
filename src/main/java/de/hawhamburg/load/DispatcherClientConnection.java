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
    private DataOutputStream outStream;
    private BufferedReader inStream;

    public DispatcherClientConnection(Dispatcher dispatcher, Socket socket) {
        this.dispatcher = dispatcher;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            outStream = new DataOutputStream(socket.getOutputStream());
            inStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (true) {
                String request = getRequest();
                dispatcher.dispatch(request);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getRequest() throws IOException {
        String request = inStream.readLine();
        if (request == null) {
            throw new IOException();
        }
        return request;
    }

    public void write(String response) {
        try {
            outStream.writeBytes(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
