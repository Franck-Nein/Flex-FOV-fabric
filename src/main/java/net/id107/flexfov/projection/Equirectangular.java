package net.id107.flexfov.projection;

import org.lwjgl.opengl.GL30;

import net.id107.flexfov.Reader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;

public class Equirectangular extends Projection {
	public static boolean drawCircle = false;
	public static boolean stabilizePitch = false;
	public static boolean stabilizeYaw = false;

	public String getFragmentShader() {
		return Reader.read("shaders/equirectangular.fs");
	}

	public void loadUniforms() {
		super.loadUniforms();
		MinecraftClient mc = MinecraftClient.getInstance();
		int shaderProgram = shaderManager.getShaderProgram();
		int circleUniform = GL30.glGetUniformLocation(shaderProgram, "drawCircle");
		GL30.glUniform1i(circleUniform, drawCircle && mc.currentScreen == null ? 1 : 0);
		Entity entity = mc.getCameraEntity() == null ? mc.player : mc.getCameraEntity();
		float pitch = 0.0F;
		float yaw = 0.0F;
		if (stabilizePitch) {
			pitch = entity.prevPitch + (entity.getPitch() - entity.prevPitch) * mc.getTickDelta();
		}

		if (stabilizeYaw) {
			yaw = entity.prevYaw + (entity.getYaw() - entity.prevYaw) * mc.getTickDelta();
		}

		if (mc.options.getPerspective().isFrontView()) {
			pitch = -pitch;
		}

		int angleUniform = GL30.glGetUniformLocation(shaderProgram, "rotation");
		GL30.glUniform2f(angleUniform, yaw, pitch);
	}

	public double getFovX() {
		return 360;
	}

	public double getFovY() {
		return 180;
	}
}
