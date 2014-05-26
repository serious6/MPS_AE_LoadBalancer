package de.hawhamburg.load.monitor;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.hawhamburg.load.dispatcher.Dispatcher;
import de.hawhamburg.load.tcp.TcpHandler;
import de.hawhamburg.load.utils.classes.Instanz;

public class Monitor extends TcpHandler {

	private final Logger logger = LogManager.getLogger(Dispatcher.class
			.getName());
	private Dispatcher dispachter;
	private int index = 0;

	private final ArrayList<Instanz> aktiveInstanzen = new ArrayList<Instanz>();

	public Monitor(int port, int threadSize) throws IOException {
		super(port, threadSize);
		logger.info(String.format("Monitor startet @%d max. Con %d", port,
				threadSize));
	}

	@Override
	protected Runnable performOperation(Socket connectionSocket) {
		return new Runnable() {
			public void run() {
				
			}
			
			private String getCompleteInstanceList() {
				ArrayList<Instanz> result = getAllInstances();

				JsonObjectBuilder model = Json.createObjectBuilder()
						.add("response", "completeList")
						.add("data", getArrayBuilder(result));
				JsonObject theObject = model.build();
				return theObject.toString();
			}

			private JsonArrayBuilder getArrayBuilder(ArrayList<Instanz> result) {
				JsonArrayBuilder builder = Json.createArrayBuilder();
				for (Instanz instanz : result) {
					builder.add(Json
							.createObjectBuilder()
							.add("name",
									instanz.getHost() + ":" + instanz.getPort())
							.add("status", (instanz.isAlive()) ? "on" : "off")
							.add("uptime", instanz.getUptime())
							.add("requests", instanz.getRequests())
							.add("systemLoad", instanz.getSystemLoad()));
				}
				return builder;
			}
		};
	}

	/**
	 * Ermitteln einer aktiven Instanz mit Round Robin
	 * 
	 * @return
	 */
	public synchronized Instanz findInstanzRR() {
		Instanz instanz = aktiveInstanzen.get((index++)
				% aktiveInstanzen.size());
		while (!instanz.isAlive()) {
			instanz = aktiveInstanzen.get((index++) % aktiveInstanzen.size());
			logger.debug("Index @" + index);
		}
		logger.debug(String.format("Found Instanz: %s", instanz.toString()));
		return instanz;
	}
	
	public synchronized ArrayList<Instanz> getAllInstances(){
		ArrayList<Instanz> result = new ArrayList<Instanz>();
		Collections.copy(new ArrayList<Instanz>(), aktiveInstanzen);
		return result;
	}

	public synchronized void rrReset() {
		this.index = 0;
	}

	public void setDispatcher(Dispatcher dispatcher) {
		this.dispachter = dispatcher;
	}

}
