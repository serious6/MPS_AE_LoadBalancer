package de.hawhamburg.load;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Observable;

public class DispatcherMpsConnection extends Observable implements Runnable {
    private Dispatcher dispatcher;
    private Socket socket;
    private DataOutputStream outStream;
	private BufferedReader inStream;
	private String key;

    public DispatcherMpsConnection(Dispatcher dispatcher, Socket socket, String key) {
        this.dispatcher = dispatcher;
        this.socket = socket;
        this.key = key;
    }

	@Override
	public void run() {
		try {
			outStream = new DataOutputStream(socket.getOutputStream());
			inStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			inStream.readLine();
		} catch (IOException e) {
			setChanged();
			notifyObservers(key);
		}
	}

    public void write(String response) {
        try {
            outStream.writeBytes(response);
        } catch (IOException e) {
			setChanged();
			notifyObservers(key);
        }
    }

	public void close() {
		try {
			socket.close();
		} catch (IOException e) {
			// ?
		}
	}
}
