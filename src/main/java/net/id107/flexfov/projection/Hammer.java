package net.id107.flexfov.projection;

import net.id107.flexfov.Reader;

public class Hammer extends Projection {
	public String getFragmentShader() {
		return Reader.read("shaders/hammer.fs");
	}

	public double getFovX() {
		return 360.0;
	}

	public double getFovY() {
		return 180.0;
	}
}
