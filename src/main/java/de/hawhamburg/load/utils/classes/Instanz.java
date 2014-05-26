package de.hawhamburg.load.utils.classes;

import java.util.Timer;
import java.util.TimerTask;

public class Instanz extends Timer {

	private final String host;
	private final String port;
	private final String name;
	
	private Integer uptime = 100;
	private Integer systemLoad = 0;
	private Integer requests = 0;

	private boolean isAlive = true;

	public Instanz(String host, String port, String name) {
		assert host != null && name != null;
		this.host = host;
		this.name = name;
		this.port = port;
	}

	public String getHost() {
		return host;
	}

	public String getName() {
		return name;
	}

	public boolean isAlive() {
		return isAlive;
	}

	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}

	public String getPort() {
		return port;
	}

	public Integer getUptime() {
		return uptime;
	}

	public void setUptime(Integer uptime) {
		this.uptime = uptime;
	}

	public Integer getSystemLoad() {
		return systemLoad;
	}

	public void setSystemLoad(Integer systemLoad) {
		this.systemLoad = systemLoad;
	}

	public Integer getRequests() {
		return requests;
	}

	public void setRequests(Integer requests) {
		this.requests = requests;
	}

}
