package net.id107.flexfov.projection;

import org.lwjgl.opengl.GL30;

import net.id107.flexfov.Reader;

public class Fisheye extends Projection {
	public static boolean fullFrame = false;
	public static int fisheyeType = 3;

	public String getFragmentShader() {
		return Reader.read("shaders/fisheye.fs");
	}

	public void loadUniforms() {
		super.loadUniforms();
		int shaderProgram = shaderManager.getShaderProgram();
		int fullFrameUniform = GL30.glGetUniformLocation(shaderProgram, "fullFrame");
		GL30.glUniform1i(fullFrameUniform, fullFrame ? 1 : 0);
		int fisheyeTypeUniform = GL30.glGetUniformLocation(shaderProgram, "fisheyeType");
		GL30.glUniform1i(fisheyeTypeUniform, fisheyeType);
	}
}
