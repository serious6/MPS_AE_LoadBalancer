package de.hawhamburg.load;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.hawhamburg.load.dispatcher.Dispatcher;
import de.hawhamburg.load.monitor.Monitor;

public class Starter {

	private final Logger logger = LogManager.getLogger(this.getClass()
			.getName());

	public static void main(String[] args) {
		try {
			Monitor monitor = new Monitor(1338, 10);
			Dispatcher dispatcher = new Dispatcher(1337, 10);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
