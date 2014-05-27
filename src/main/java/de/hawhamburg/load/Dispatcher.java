package de.hawhamburg.load;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class Dispatcher implements Observer, Runnable {
    public Monitor monitor;
    private int port;
    private int roundRobin;
    protected List<MpsInstance> instances;

	public Dispatcher(int port, List<MpsInstance> instances) {
        this.port = port;
        this.instances = instances;

        new Thread(this).start();
	}

    public MpsInstance addInstance(JsonObject json) {
        String key = json.getString("key");
        MpsInstance instance = new MpsInstance(key);
        instances.add(instance);

        String[] info = key.split(":");
        try {
            Socket socket = new Socket(info[0], Integer.parseInt(info[1]));
            DispatcherMpsConnection dmc = new DispatcherMpsConnection(this, socket);
            dmc.addObserver(this);
            instance.connection = dmc;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return instance;
    }

    public void dispatch(String request) {
        if (!instances.isEmpty()) {
            MpsInstance instance = instances.get(roundRobin);
            instance.requests++;
            instance.connection.write(request);

            monitor.publish(Json.createObjectBuilder()
                    .add("response", "update")
                    .add("data", Json.createObjectBuilder()
                        .add("requests", instance.requests)
                    )
                    .build());
        }
    }

    public void startInstance(String key) {
        MpsInstance instance = instances.get(instances.indexOf(key));
        if (instance.status.equals("stopped")) {
            instance.status = "on";

            monitor.publish(Json.createObjectBuilder()
                    .add("response", "update")
                    .add("data", Json.createObjectBuilder()
                        .add("status", instance.status)
                    )
                    .build());
        }
    }

    public void stopInstance(String key) {
        MpsInstance instance = instances.get(instances.indexOf(key));
        if (instance.status.equals("on")) {
            instance.status = "stopped";

            monitor.publish(Json.createObjectBuilder()
                    .add("response", "update")
                    .add("data", Json.createObjectBuilder()
                        .add("status", instance.status)
                    )
                    .build());
        }
    }

    public void removeInstance(String key) {
        instances.remove(instances.indexOf(key));
        roundRobin %= instances.size();
    }

    @Override
    public void run() {
        try {
            final ServerSocket socket = new ServerSocket(port);
            while (true) {
                // Incoming client requests
                Socket connection = socket.accept();
                new Thread(new DispatcherClientConnection(this, connection)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
