package de.hawhamburg.load.dispatcher;

import java.io.IOException;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.hawhamburg.load.monitor.Monitor;
import de.hawhamburg.load.tcp.TcpHandler;
import de.hawhamburg.load.utils.classes.Instanz;

public class Dispatcher extends TcpHandler {

	private final Logger logger = LogManager.getLogger(Dispatcher.class
			.getName());

	private Monitor monitor;

	public Dispatcher(int port, int threadSize) throws IOException {
		super(port, threadSize);
		logger.info(String.format("Dispatcher startet @%d max. Con %d", port,
				threadSize));
	}

	@Override
	protected Runnable performOperation(Socket connectionSocket) {
		return new Runnable() {

			public void run() {
				Instanz instanz = monitor.findInstanzRR();
			}
		};
	}

	public void setMonitor(Monitor monitor) {
		this.monitor = monitor;
	}

}
