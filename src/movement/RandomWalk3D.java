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
 * Random Walk 3D movement model
 *
 */
public class RandomWalk3D extends MovementModel implements SwitchableMovement {

	private Coord lastWaypoint;
	private double minDistance;
	private double maxDistance;

	public RandomWalk3D(Settings settings) {
		super(settings);
		minDistance = 0;
		maxDistance = 50;
	}

	private RandomWalk3D(RandomWalk3D rwp) {
		super(rwp);
		minDistance = rwp.minDistance;
		maxDistance = rwp.maxDistance;
	}

	/**
	 * Returns a possible (random) placement for a host
	 * @return Random position on the map
	 */
	@Override
	public Coord getInitialLocation() {
		assert rng != null : "MovementModel not initialized!";
		double x = rng.nextDouble() * getMaxX();
		double y = rng.nextDouble() * getMaxY();
		double z = rng.nextDouble() * getMaxZ();
		Coord c = new Coord(x,y,z);

		this.lastWaypoint = c;
		return c;
	}

	@Override
	public Path getPath() {
		Path p;
		p = new Path(generateSpeed());
		p.addWaypoint(lastWaypoint.clone());
		double maxX = getMaxX();
		double maxY = getMaxY();
		double maxZ = getMaxZ();
		Coord c = null;
		while (true) {

			double angle = rng.nextDouble() * 2 * Math.PI;
			double angleOfElevation = rng.nextDouble() * (Math.PI) -0.5 * Math.PI;
//			System.out.println(angleOfElevation);
			double distance = minDistance + rng.nextDouble() *
				(maxDistance - minDistance);
			double dZ = distance * Math.sin(angleOfElevation);
			double projection = distance * Math.cos(angleOfElevation);
//			System.out.println("dz="+dZ);
//			System.out.println("projection="+projection);
//			System.out.println("original z="+lastWaypoint.getZ());
			double z = lastWaypoint.getZ() + dZ;
			double x = lastWaypoint.getX() + projection * Math.cos(angle);
			double y = lastWaypoint.getY() + projection * Math.sin(angle);

			c = new Coord(x,y,z);
			if (x > 0 && y > 0 && x < maxX && y < maxY && z>0 && z <maxZ) {
				break;
			}
		}

		p.addWaypoint(c);

		this.lastWaypoint = c;
		return p;
	}

	@Override
	public RandomWalk3D replicate() {
		return new RandomWalk3D(this);
	}

	public Coord getLastLocation() {
		return lastWaypoint;
	}

	public void setLocation(Coord lastWaypoint) {
		this.lastWaypoint = lastWaypoint;
	}

	public boolean isReady() {
		return true;
	}
}
