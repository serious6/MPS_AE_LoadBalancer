package de.hawhamburg.load;

import javax.json.JsonObject;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Monitor implements Observer, Runnable {
	private int port = 0;
	private int heartbeatPort = 0;
	public Dispatcher dispatcher;
	protected final Set<MonitorDashboardConnection> dashboardConnections = Collections.synchronizedSet(new HashSet<MonitorDashboardConnection>());

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

				System.out.println("Dashboard connected to monitor from: " + connection.getRemoteSocketAddress().toString());

                MonitorDashboardConnection mdc = new MonitorDashboardConnection(this, connection);
                mdc.addObserver(this);
                dashboardConnections.add(mdc);
                new Thread(mdc).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        String msg = (String)arg;
        if (msg.equals("dashboard lost")) {
            dashboardConnections.remove(o);
        }

    }

    public void publish(JsonObject response) {
        synchronized (dashboardConnections) {
            Iterator<MonitorDashboardConnection> i = dashboardConnections.iterator();
            while (i.hasNext()) {
                try {
                    i.next().write(response);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class MonitorHeartbeat implements Observer, Runnable {

        @Override
        public void run() {
            try {
                final ServerSocket socket = new ServerSocket(heartbeatPort);
                while (true) {
                    Socket connection = socket.accept();

                    MonitorMpsConnection mmc = new MonitorMpsConnection(Monitor.this, connection);
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
