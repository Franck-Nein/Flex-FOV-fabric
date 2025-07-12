package net.id107.flexfov.projection;

import net.id107.flexfov.Reader;

public class Cubic extends Projection {
	public String getFragmentShader() {
		return Reader.read("shaders/cubic.fs");
	}
	
	public double getFovX() {
		return 360;
	}
	
	public double getFovY() {
		return 180;
	}
}
