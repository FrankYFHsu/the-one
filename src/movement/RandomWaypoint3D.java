/*
 * Copyright 2010 Aalto University, ComNet
 * Released under GPLv3. See LICENSE.txt for details.
 * 
 * Copyright 2018 National Central University, NCLab
 * Released under GPLv3. See LICENSE.txt for details.
 */
package movement;

import core.Coord;
import core.Settings;

/**
 * Random waypoint 3D movement model. Creates zig-zag paths within the
 * simulation area.
 */
public class RandomWaypoint3D extends MovementModel {
	/** how many waypoints should there be per path */
	private static final int PATH_LENGTH = 1;
	private Coord lastWaypoint;

	public RandomWaypoint3D(Settings settings) {
		super(settings);
	}

	protected RandomWaypoint3D(RandomWaypoint3D rwp) {
		super(rwp);
	}

	/**
	 * Returns a possible (random) placement for a host
	 * @return Random position on the map
	 */
	@Override
	public Coord getInitialLocation() {
		assert rng != null : "MovementModel not initialized!";
		Coord c = randomCoord();

		this.lastWaypoint = c;
		return c;
	}

	@Override
	public Path getPath() {
		Path p;
		p = new Path(generateSpeed());
		p.addWaypoint(lastWaypoint.clone());
		Coord c = lastWaypoint;

		for (int i=0; i<PATH_LENGTH; i++) {
			c = randomCoord();
			p.addWaypoint(c);
		}

		this.lastWaypoint = c;
		return p;
	}

	@Override
	public RandomWaypoint3D replicate() {
		return new RandomWaypoint3D(this);
	}

	protected Coord randomCoord() {
		return new Coord(rng.nextDouble() * getMaxX(),
				rng.nextDouble() * getMaxY(), rng.nextDouble()*getMaxZ());
	}
}
