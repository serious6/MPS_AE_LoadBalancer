package de.hawhamburg.load;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class Monitor implements Observer, Runnable {
	private int index = 0;
	private int port = 0;
	protected Dispatcher dispatcher;
	protected HashSet<MonitorConnection> connections = new HashSet<MonitorConnection>();

	public Monitor(int port, Dispatcher dispatcher) {
        this.port = port;
		this.dispatcher = dispatcher;

        new Thread(this).start();
	}

    @Override
    public void run() {
        try {
            final ServerSocket socket = new ServerSocket(port);
            while (true) {
                Socket connection = socket.accept();

                MonitorConnection mc = new MonitorConnection(this, connection);
                mc.addObserver(this);
                connections.add(mc);
                new Thread(mc).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        connections.remove(o);
    }
}
