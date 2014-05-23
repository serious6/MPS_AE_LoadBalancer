package de.hawhamburg.load.tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class TcpHandler {
	private boolean interrupted = false;

	private final ServerSocket serverSocket; // TCP-Server-Socketklasse
	private Socket connectionSocket; // TCP-Standard-Socketklasse

	private final ExecutorService executor;

	public TcpHandler(int port, int threadSize) throws IOException {
		serverSocket = new ServerSocket(port, threadSize);
		executor = Executors.newFixedThreadPool(threadSize + 1);

		executor.execute(new Runnable() {

			public void run() {
				startListening();
			}

			private void startListening() {
				try {
					while (!isInterrupted()) {
						try {
							connectionSocket = serverSocket.accept();

							executor.execute(performOperation(connectionSocket));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} finally {
					executor.shutdown();
				}
			}
		});
	}

	/**
	 * 
	 * @param connectionSocket
	 * @return
	 */
	protected abstract Runnable performOperation(Socket connectionSocket);

	private boolean isInterrupted() {
		return interrupted;
	}

	protected void interrupt() {
		interrupted = true;
	}

	public int getPort() {
		return serverSocket.getLocalPort();
	}
}
