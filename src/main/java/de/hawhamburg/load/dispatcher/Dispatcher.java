package de.hawhamburg.load.dispatcher;

import java.io.IOException;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.hawhamburg.load.tcp.TcpHandler;

public class Dispatcher extends TcpHandler {

	private final Logger logger = LogManager.getLogger(Dispatcher.class
			.getName());

	public Dispatcher(int port, int threadSize) throws IOException {
		super(port, threadSize);
		logger.info(String.format("Dispatcher startet @%d max. Con %d", port,
				threadSize));
	}

	@Override
	protected Runnable performOperation(Socket connectionSocket) {
		return null;
	}

}
