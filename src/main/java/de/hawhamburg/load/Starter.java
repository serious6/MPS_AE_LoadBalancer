package de.hawhamburg.load;

import java.util.ArrayList;
import java.util.List;

public class Starter {

	public static void main(String[] args) {
        final List<MpsInstance> instances = new ArrayList<MpsInstance>();

        Dispatcher dispatcher = new Dispatcher(1337, instances);
        Monitor monitor = new Monitor(1338, 1339);

        dispatcher.monitor = monitor;
        monitor.dispatcher = dispatcher;
	}
}
