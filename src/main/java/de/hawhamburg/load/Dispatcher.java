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
    private int roundRobin = 0;
    protected final List<MpsInstance> instances;

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
            DispatcherMpsConnection dmc = new DispatcherMpsConnection(this, socket, key);
            dmc.addObserver(this);
            instance.status = "on";
            instance.connection = dmc;

			new Thread(dmc).start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return instance;
    }

    public void dispatch(String request) {
		MpsInstance instance = getNextInstanceRR();
		instance.requests++;
		instance.connection.write(request);

		monitor.publish(Json.createObjectBuilder()
			.add("response", "update")
			.add("key", instance.name)
			.add("data", Json.createObjectBuilder()
				.add("requests", instance.requests)
			)
			.build()
		);
    }

	private MpsInstance getNextInstanceRR() {
		int length = instances.size();
		if (length > 0) {
			for (int i = 0; i < length; i++) {
				MpsInstance instance = instances.get(roundRobin);
				roundRobin = (roundRobin + 1) % instances.size();
				if (instance.status.equals("on")) {
					return instance;
				}
			}
		}
		return null;
	}

    public void startInstance(String key) {
        MpsInstance instance = instances.get(findMpsInstance(key));
        if (instance.status.equals("stopped")) {
            instance.status = "on";

            monitor.publish(Json.createObjectBuilder()
				.add("response", "update")
				.add("key", key)
				.add("data", Json.createObjectBuilder()
					.add("status", instance.status)
				)
				.build()
			);
        }
    }

    public void stopInstance(String key) {
        MpsInstance instance = instances.get(findMpsInstance(key));
        if (instance.status.equals("on")) {
            instance.status = "stopped";

            monitor.publish(Json.createObjectBuilder()
				.add("response", "update")
				.add("key", key)
				.add("data", Json.createObjectBuilder()
					.add("status", instance.status)
				)
				.build()
			);
        }
    }

    public void removeInstance(String key) {
        instances.remove(findMpsInstance(key));
        roundRobin %= instances.size();

		monitor.publish(Json.createObjectBuilder()
			.add("response", "remove")
			.add("key", key)
			.build()
		);
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
		String key = (String)arg;

    }

	public int findMpsInstance(String key) {
		synchronized (instances) {
			int length = instances.size();
			for (int i = 0; i < length; i++) {
				if (instances.get(i).name.equals(key)) {
					return i;
				}
			}
			return -1;
		}
	}
}
