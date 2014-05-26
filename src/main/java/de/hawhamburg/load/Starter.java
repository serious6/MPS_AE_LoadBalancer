package de.hawhamburg.load;

import java.util.ArrayList;
import java.util.List;

public class Starter {

	public static void main(String[] args) {
        final List<MpsInstance> instances = new ArrayList<MpsInstance>();

        Dispatcher dis = new Dispatcher(1337, instances);
        new Monitor(1338, dis);
	}
}
