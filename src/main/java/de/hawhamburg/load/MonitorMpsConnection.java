package de.hawhamburg.load;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.List;

public class MonitorMpsConnection implements Runnable {
    private Monitor monitor;
    private Socket socket;
    private BufferedReader inStream;

    MonitorMpsConnection(Monitor monitor, Socket socket) {
        this.monitor = monitor;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            inStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (true) {
                String request = getRequest();

                List<MpsInstance> instances = monitor.dispatcher.instances;
                int index = instances.indexOf(request);
                if (index != -1) {
                    instances.get(index).heartbeat();
                }
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
}
