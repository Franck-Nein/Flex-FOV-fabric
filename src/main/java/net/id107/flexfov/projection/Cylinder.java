package net.id107.flexfov.projection;

import net.id107.flexfov.Reader;

public class Cylinder extends Projection {
	public static double fovy = 90;

	public String getFragmentShader() {
		return Reader.read("shaders/cylinder.fs");
	}

	public double getFovY() {
		return fovy == 180.0 ? 179.9999 : fovy;
	}
}
