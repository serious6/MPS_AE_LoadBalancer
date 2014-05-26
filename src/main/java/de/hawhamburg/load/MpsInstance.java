package de.hawhamburg.load;

public class MpsInstance {
	public final String name;
    public String status;
    public int uptime = 0;
    public int systemLoad = 0;
    public int requests = 0;

    public DispatcherMpsConnection connection;

	public MpsInstance(String name) {
        this.name = name;
        status = "off";
    }

    public boolean equals(Object obj) {
        return obj instanceof MpsInstance && ((MpsInstance) obj).name.equals(name);
    }
}