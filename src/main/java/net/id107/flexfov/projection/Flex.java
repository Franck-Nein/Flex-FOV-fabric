package net.id107.flexfov.projection;

import net.id107.flexfov.Reader;

public class Flex extends Projection {

	public String getFragmentShader() {
		return Reader.read("shaders/flex.fs");
	}

	public double getPassFOV(double fovIn) {
		double fov = getFovX();
		if (fov <= 90.0) {
			return fov == 0.0 ? 1.0E-4 : fov;
		} else {
			return super.getPassFOV(fovIn);
		}
	}

	public boolean getResizeGui() {
		return false;
	}

	public boolean getShowHand() {
		return true;
	}
}
