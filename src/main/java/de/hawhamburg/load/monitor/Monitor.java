package de.hawhamburg.load.monitor;

import java.io.IOException;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.hawhamburg.load.dispatcher.Dispatcher;
import de.hawhamburg.load.tcp.TcpHandler;

public class Monitor extends TcpHandler {

	private final Logger logger = LogManager.getLogger(Dispatcher.class
			.getName());

	public Monitor(int port, int threadSize) throws IOException {
		super(port, threadSize);
		logger.info(String.format("Monitor startet @%d max. Con %d", port,
				threadSize));
	}

	@Override
	protected Runnable performOperation(Socket connectionSocket) {
		return null;
	}

}
