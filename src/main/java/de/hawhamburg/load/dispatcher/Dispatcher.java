package de.hawhamburg.load.dispatcher;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.hawhamburg.load.monitor.Monitor;
import de.hawhamburg.load.tcp.TcpHandler;
import de.hawhamburg.load.utils.classes.Instanz;

public class Dispatcher extends TcpHandler {

	private final Logger logger = LogManager.getLogger(Dispatcher.class
			.getName());

	public enum Command {
		ADD, SHUTDOWN
	}

	private Monitor monitor;

	public Dispatcher(int port, int threadSize) throws IOException {
		super(port, threadSize);
		logger.info(String.format("Dispatcher startet @%d max. Con %d", port,
				threadSize));
	}

	@Override
	protected Runnable performOperation(final Socket connectionSocket) {
		return new Runnable() {

			public void run() {
				while (true) {
					try {
						Command input = parseInput(connectionSocket);
						switch (input) {
						case ADD:

							break;
						case SHUTDOWN:
							shutdownInstance(input);
							break;
						default:
							logger.error(String
									.format("Unbekannte Nachricht im Dispatcher %s",
											""));
							break;
						}
					} catch (Exception e) {
						logger.error("Unbekannte Nachricht im Dispatcher", e);
					}
				}
			}

			private Command parseInput(Socket connectionSocket) {
				return null;
			}

			private void shutdownInstance(String input) {

			}
		};
	}

	public void setMonitor(Monitor monitor) {
		this.monitor = monitor;
	}

}
