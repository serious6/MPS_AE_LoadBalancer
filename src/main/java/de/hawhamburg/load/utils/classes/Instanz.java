package de.hawhamburg.load.utils.classes;

import java.util.Timer;
import java.util.TimerTask;

public class Instanz extends Timer {

	private final String path;
	private final String host;
	private final String name;

	private boolean isAlive = true;

	public Instanz(String path, String host, String name) {
		assert path != null && host != null && name != null;
		this.path = path;
		this.host = host;
		this.name = name;

		schedule(new Task(), 10000);
	}

	public String getPath() {
		return path;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((host == null) ? 0 : host.hashCode());
		result = prime * result + (isAlive ? 1231 : 1237);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((path == null) ? 0 : path.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Instanz other = (Instanz) obj;
		if (host == null) {
			if (other.host != null)
				return false;
		} else if (!host.equals(other.host))
			return false;
		if (isAlive != other.isAlive)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		return true;
	}

	class Task extends TimerTask {
		@Override
		public void run() {
			setAlive(false);
		}
	}

}
