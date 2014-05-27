package de.hawhamburg.load;

public class MpsInstance {
	public final String name;
    public String status;
    private int uptime = 0;
    public int systemLoad = 0;
    public int requests = 0;

    public DispatcherMpsConnection connection;

	public MpsInstance(String name) {
        this.name = name;
        status = "off";
    }

    public void heartbeat() {

    }

    public int getUptime() {
        return uptime;
    }
}
