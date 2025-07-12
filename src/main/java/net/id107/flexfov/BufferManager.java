package net.id107.flexfov;

import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL30;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.util.Window;
import net.minecraft.client.gl.WindowFramebuffer;
import org.lwjgl.system.MemoryStack;

public class BufferManager {
	private static BufferManager instance;
	public static boolean inConstructor = false;
	public static int minX;
	public static int maxX;
	public static int minY;
	public static int maxY;
	public static float passFov;
	private final Framebuffer framebuffer;
	private int vaoId;
	private int vboId;

	public static BufferManager getInstance() {
		if (instance == null) {
			instance = new BufferManager();
		}

		return instance;
	}

	private BufferManager() {
		inConstructor = true;
		Window window = MinecraftClient.getInstance().getWindow();
		int width = window.getFramebufferWidth();
		int height = window.getFramebufferHeight();
		framebuffer = new WindowFramebuffer(width, height);
		framebuffer.setClearColor(0.0F, 0.0F, 0.0F, 0.0F);
		framebuffer.clear(MinecraftClient.IS_SYSTEM_MAC);
		createVBO();
		inConstructor = false;
	}

	private void createVBO() {
		MemoryStack stack = MemoryStack.stackPush();

		try {
			float[] vertexData = new float[]{-1.0F, -1.0F, 0.0F, 0.0F, 1.0F, -1.0F, 1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, -1.0F, 1.0F, 0.0F, 1.0F, -1.0F, -1.0F, 0.0F, 0.0F};
			FloatBuffer vertexBuffer = stack.mallocFloat(vertexData.length);
			vertexBuffer.put(vertexData).flip();
			vaoId = GL30.glGenVertexArrays();
			GL30.glBindVertexArray(vaoId);
			vboId = GL30.glGenBuffers();
			GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vboId);
			GL30.glBufferData(GL30.GL_ARRAY_BUFFER, vertexBuffer, GL30.GL_STATIC_DRAW);
			GL30.glVertexAttribPointer(0, 2, GL30.GL_FLOAT, false, 16, 0L);
			GL30.glVertexAttribPointer(1, 2, GL30.GL_FLOAT, false, 16, 8L);
			GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, 0);
			GL30.glBindVertexArray(0);
		} catch (Throwable var5) {
			if (stack != null) {
				try {
					stack.close();
				} catch (Throwable var4) {
					var5.addSuppressed(var4);
				}
			}

			throw var5;
		}

		if (stack != null) {
			stack.close();
		}

	}

	public void draw() {
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vboId);
		GL30.glBindVertexArray(vaoId);
		GL30.glEnableVertexAttribArray(0);
		GL30.glEnableVertexAttribArray(1);
		GL30.glDrawArrays(GL30.GL_TRIANGLES, 0, 6);
		GL30.glDisableVertexAttribArray(1);
		GL30.glDisableVertexAttribArray(0);
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, 0);
		GL30.glBindVertexArray(0);
	}

	public Framebuffer getFramebuffer() {
		return framebuffer;
	}
}
