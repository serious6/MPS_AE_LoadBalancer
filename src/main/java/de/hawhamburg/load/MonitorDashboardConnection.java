package de.hawhamburg.load;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.*;
import java.net.Socket;
import java.util.Observable;

public class MonitorDashboardConnection extends Observable implements Runnable {
    private Monitor monitor;
    private Socket socket;
    private DataOutputStream outStream;
    private BufferedReader inStream;

    MonitorDashboardConnection(Monitor monitor, Socket socket) {
        this.monitor = monitor;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            outStream = new DataOutputStream(socket.getOutputStream());
            inStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (true) {
                JsonObject json = getRequest();
                String request = json.getString("request");

                if (request.equals("getCompleteList")) {
                    write(getCompleteList());
                } else if (request.equals("add")) {
                    MpsInstance instance = monitor.dispatcher.addInstance(json);
                    write(Json.createObjectBuilder()
                        .add("response", "add")
                        .add("key", instance.name)
                        .add("data", Json.createObjectBuilder()
							.add("status", instance.status)
							.add("uptime", instance.getUptime())
							.add("requests", instance.requests)
							.add("systemLoad", instance.systemLoad)
                        )
                        .build()
                    );
                } else if (request.equals("start")) {
                    monitor.dispatcher.startInstance(json.getString("key"));
                } else if (request.equals("stop")) {
                    monitor.dispatcher.stopInstance(json.getString("key"));
                } else if (request.equals("remove")) {
                    monitor.dispatcher.removeInstance(json.getString("key"));
                }
            }
        } catch (IOException e) {
			System.out.println("dashboard lost");

			setChanged();
            notifyObservers("dashboard lost");
        }
    }

    private JsonObject getRequest() throws IOException {
        String request = inStream.readLine();
        if (request == null) {
            throw new IOException();
        }
		System.out.println("From Dashboard: " + request);
        return Json.createReader(new StringReader(request)).readObject();
    }

    public void write(JsonObject response) throws IOException {
		System.out.println("To Dashboard: " + response.toString());
        outStream.writeBytes(response.toString() + "\n");
    }

    private JsonObject getCompleteList() {
        JsonArrayBuilder instancesBuilder = Json.createArrayBuilder();
		synchronized (monitor.dispatcher.instances) {
			for (MpsInstance instance : monitor.dispatcher.instances) {
				instancesBuilder.add(Json.createObjectBuilder()
					.add("name", instance.name)
					.add("status", instance.status)
					.add("uptime", instance.getUptime())
					.add("requests", instance.requests)
					.add("systemLoad", instance.systemLoad)
				);
			}
		}

        return Json.createObjectBuilder()
            .add("response", "completeList")
            .add("data", instancesBuilder)
            .build();
    }
}
