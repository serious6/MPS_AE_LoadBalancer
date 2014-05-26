package de.hawhamburg.load;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Observable;

public class DispatcherMpsConnection extends Observable implements Runnable {

    private Socket socket;
    private DataOutputStream outStream;
    private BufferedReader inStream;

    public DispatcherMpsConnection(Dispatcher dispatcher, Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            outStream = new DataOutputStream(socket.getOutputStream());
            inStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(String response) {

    }
}
