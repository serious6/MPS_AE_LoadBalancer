package de.hawhamburg.load;

import java.util.concurrent.TimeUnit;

public class MpsInstance {
	public final String name;
    public String status;
    private int uptime = 0;
    public int systemLoad = 0;
    public int requests = 0;

	private long started = 0;
	private long heartbeats = 0;

    public DispatcherMpsConnection connection;

	public MpsInstance(String name) {
        this.name = name;
        status = "off";

		started = now();
    }

    public void heartbeat() {
		heartbeats++;
    }

    public int getUptime() {
		final long now = now();
		final long diff = now - started;
        return (int)Math.min((heartbeats / (double)diff) * 100, 100);
    }

	private long now() {
		return TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
	}
}
