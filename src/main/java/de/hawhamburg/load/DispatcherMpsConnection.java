package de.hawhamburg.load;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Observable;

public class DispatcherMpsConnection extends Observable {
    private Dispatcher dispatcher;
    private Socket socket;
    private DataOutputStream outStream;

    public DispatcherMpsConnection(Dispatcher dispatcher, Socket socket) {
        this.dispatcher = dispatcher;
        this.socket = socket;

        try {
            outStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            setChanged();
            notifyObservers("mps lost");
            e.printStackTrace();
        }
    }

    public void write(String response) {
        try {
            outStream.writeBytes(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
