package net.id107.flexfov.projection;

import net.id107.flexfov.Reader;

public class Panini extends Projection {
	public String getFragmentShader() {
		return Reader.read("shaders/panini.fs");
	}
}
