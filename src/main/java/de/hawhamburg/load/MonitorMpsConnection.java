package de.hawhamburg.load;

import javax.json.Json;
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

				synchronized (monitor.dispatcher.instances) {
					int index = monitor.dispatcher.findMpsInstance(request);
					if (index != -1) {
						MpsInstance instance = monitor.dispatcher.instances.get(index);
						instance.heartbeat();
						monitor.publish(Json.createObjectBuilder()
							.add("response", "update")
							.add("key", instance.name)
							.add("data", Json.createObjectBuilder()
								.add("uptime", instance.getUptime()))
							.build()
						);
					}
				}
            }
        } catch (IOException e) {
			// who cares?
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
