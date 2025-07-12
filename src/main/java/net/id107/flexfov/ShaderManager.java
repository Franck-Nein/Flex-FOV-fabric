package net.id107.flexfov;

import org.lwjgl.opengl.GL30;
import net.id107.flexfov.projection.Projection;

public class ShaderManager {

	private int shaderProgram;
	private int vertexShader;
	private int fragmentShader;

	public void createShaderProgram(Projection projection) {
		if (shaderProgram == 0) {
			shaderProgram = GL30.glCreateProgram();
			vertexShader = createShader(projection.getVertexShader(), GL30.GL_VERTEX_SHADER);
			fragmentShader = createShader(projection.getFragmentShader(), GL30.GL_FRAGMENT_SHADER);
			GL30.glAttachShader(shaderProgram, vertexShader);
			GL30.glAttachShader(shaderProgram, fragmentShader);
			GL30.glBindAttribLocation(shaderProgram, 0, "vertex");
			GL30.glBindFragDataLocation(shaderProgram, 0, "color");
			GL30.glLinkProgram(shaderProgram);
			if (GL30.glGetProgrami(shaderProgram, GL30.GL_LINK_STATUS) == GL30.GL_FALSE) {
				throw new RuntimeException(getLogInfo(shaderProgram));
			} else {
				GL30.glValidateProgram(shaderProgram);
				if (GL30.glGetProgrami(shaderProgram, GL30.GL_VALIDATE_STATUS) == GL30.GL_FALSE) {
					throw new RuntimeException(getLogInfo(shaderProgram));
				}
			}
		}
	}

	private int createShader(String source, int type) {
		int shader = GL30.glCreateShader(type);
		if (shader == 0) {
			throw new RuntimeException("Could not create shader");
		} else {
			GL30.glShaderSource(shader, source);
			GL30.glCompileShader(shader);
			if (GL30.glGetShaderi(shader, GL30.GL_COMPILE_STATUS) == GL30.GL_FALSE) {
				throw new RuntimeException("Error creating shader: " + getLogInfo(shader));
			} else {
				return shader;
			}
		}
	}

	private String getLogInfo(int shader) {
		return GL30.glGetShaderInfoLog(shader, GL30.glGetShaderi(shader, GL30.GL_INFO_LOG_LENGTH));
	}

	public void deleteShaderProgram() {
		if (shaderProgram != 0) {
			GL30.glDetachShader(shaderProgram, vertexShader);
			GL30.glDetachShader(shaderProgram, fragmentShader);
			GL30.glDeleteShader(vertexShader);
			GL30.glDeleteShader(fragmentShader);
			GL30.glDeleteProgram(shaderProgram);
			vertexShader = 0;
			fragmentShader = 0;
			shaderProgram = 0;
		}
	}
	
	public int getShaderProgram() {
		return shaderProgram;
	}
}
