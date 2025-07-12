package net.id107.flexfov.projection;

import net.id107.flexfov.Reader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.GameRenderer;

public class Rectilinear extends Projection {
	public String getFragmentShader() {
		return Reader.read("shaders/error.fs");
	}

	public void renderWorld(GameRenderer gameRenderer, float tickDelta, long limitTime, MatrixStack matrices) {
		gameRenderer.renderWorld(tickDelta, limitTime, matrices);
	}

	public void rotateCamera(MatrixStack matrixStack) {}

	public void saveRenderPass() {}

	public void loadUniforms() {}

	public void runShader() {}

	public boolean getResizeGui() {
		return MinecraftClient.getInstance().world != null;
	}

	public boolean getShowHand() {
		return true;
	}

	public boolean shouldRotateParticles() {
		return false;
	}

	public double getPassFOV(double fovIn) {
		return fovIn;
	}
}
