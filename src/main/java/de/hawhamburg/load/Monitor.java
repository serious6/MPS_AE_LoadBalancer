package de.hawhamburg.load;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Monitor implements Observer, Runnable {
	private int port = 0;
	private int heartbeatPort = 0;
	public Dispatcher dispatcher;
	protected Set<MonitorDashboardConnection> dashboardConnections = Collections.synchronizedSet(new HashSet<MonitorDashboardConnection>());

	public Monitor(int port, int heartbeatPort) {
        this.port = port;
        this.heartbeatPort = heartbeatPort;

        new Thread(this).start();
        new Thread(new MonitorHeartbeat()).start();
	}

    @Override
    public void run() {
        try {
            final ServerSocket socket = new ServerSocket(port);
            while (true) {
                Socket connection = socket.accept();

                MonitorDashboardConnection mc = new MonitorDashboardConnection(this, connection);
                mc.addObserver(this);
                dashboardConnections.add(mc);
                new Thread(mc).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        dashboardConnections.remove(o);
    }

    public class MonitorHeartbeat implements Observer, Runnable {

        @Override
        public void run() {
            try {
                final ServerSocket socket = new ServerSocket(heartbeatPort);
                while (true) {
                    Socket connection = socket.accept();

                    MonitorMpsConnection mmc = new MonitorMpsConnection(Monitor.this);
                    new Thread(mmc).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void update(Observable o, Object arg) {

        }
    }
}
