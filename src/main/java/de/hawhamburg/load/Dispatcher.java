package de.hawhamburg.load;

import javax.json.JsonObject;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class Dispatcher implements Observer, Runnable {
    private int port;
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

            new Thread(dmc).start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return instance;
    }

    public void startInstance(String key) {
        instances.get(instances.indexOf(key)).connection.write("start");
    }

    public void stopInstance(String key) {

    }

    public void removeInstance(String key) {

    }

    @Override
    public void run() {
        try {
            final ServerSocket socket = new ServerSocket(port);
            while (true) {
                // Incoming client requests
                Socket connection = socket.accept();
                new Thread(new DispatcherConnection(this, connection)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
