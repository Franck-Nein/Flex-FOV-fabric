package net.id107.flexfov.projection;

import org.lwjgl.opengl.GL30;

import com.mojang.blaze3d.systems.RenderSystem;
import net.id107.flexfov.BufferManager;
import net.id107.flexfov.Reader;
import net.id107.flexfov.ShaderManager;
import net.id107.flexfov.ConfigManager;
import net.id107.flexfov.mixinHelpers.FramebufferAdditions;
import net.id107.flexfov.mixinHelpers.GameRendererAdditions;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Quaternion;
import net.minecraft.client.render.GameRenderer;

public abstract class Projection {
	public static final float[] backgroundColor = new float[3];
	public static double fov = 140.0;

	public static int antialiasing = 1;
	public static boolean skyBackground = true;
	public static float zoom = 0.0F;
	public static boolean resizeGui = false;
	public static boolean showHand = true;
	protected ShaderManager shaderManager;
	public int renderPass;
	private static Projection instance;
	private static Projections projection;
	public static Projections defaultProjection;

	public static Projection getInstance() {
		if (instance == null) {
			setProjection(defaultProjection);
		}

		return instance;
	}

	public static void setProjection(Projections projection) {
		Projection.projection = projection;
		switch (projection.ordinal()) {
			case 0:
				new Cubic();
				break;
			case 1:
				new Cylinder();
				break;
			case 2:
				new Equirectangular();
				break;
			case 3:
				new Fisheye();
				break;
			case 4:
				new Flex();
				break;
			case 5:
				new Hammer();
				break;
			case 6:
				new Panini();
				break;
			case 7:
				new Rectilinear();
		}

		ConfigManager.saveConfig();
	}

	public static Projections getProjection() {
		return projection;
	}

	protected Projection() {
		if (instance != null) {
			deleteProjection();
		}

		shaderManager = new ShaderManager();
		shaderManager.createShaderProgram(this);
		instance = this;
	}

	public static void deleteProjection() {
		instance.shaderManager.deleteShaderProgram();
		instance.shaderManager = null;
		instance = null;
	}

	public String getVertexShader() {
		return Reader.read("shaders/quad.vs");
	}

	public abstract String getFragmentShader();

	public void renderWorld(GameRenderer gameRenderer, float tickDelta, long limitTime, MatrixStack matrices) {
		MinecraftClient mc = MinecraftClient.getInstance();

		for(renderPass = 5; renderPass >= 0; --renderPass) {
			mc.worldRenderer.scheduleTerrainUpdate();
			gameRenderer.renderWorld(tickDelta, limitTime, matrices);
			if (getResizeGui() && renderPass == 0) {
				return;
			}

			saveRenderPass();
		}

		loadUniforms();
		runShader();
	}

	public static Matrix4f degfToQuat(float angle, float x, float y, float z) {
		float halfAngleRadians = (float) Math.toRadians(angle / 2.0);

		float w = (float) Math.cos(halfAngleRadians);
		float qx = x * (float) Math.sin(halfAngleRadians);
		float qy = y * (float) Math.sin(halfAngleRadians);
		float qz = z * (float) Math.sin(halfAngleRadians);

		return new Matrix4f (new Quaternion(qx, qy, qz, w));
  }

	public void rotateCamera(MatrixStack matrixStack) {
		//Matrix4f matrix;
		switch (renderPass) {
			case 0://Front
			default:
				matrixStack.peek().getPositionMatrix().multiply(degfToQuat(360, 1, 0, 0));
				break;
			case 1: //Rigth
				//matrix = new Matrix4f(new Quaternion(0.0F, 0.70710677F, 0.0F, 0.70710677F));
				matrixStack.peek().getPositionMatrix().multiply(degfToQuat(90, 0, 1, 0));
				//GL30.glRotatef(-90, 0, 1, 0);
				break;
			case 2: //Left
				//matrix = new Matrix4f(new Quaternion(0.0F, -0.70710677F, 0.0F, 0.70710677F));
				matrixStack.peek().getPositionMatrix().multiply(degfToQuat(-90, 0, 1, 0));
				//GL30.glRotatef(90, 0, 1, 0);
				break;
			case 3: //Bottom
				//matrix = new Matrix4f(new Quaternion(0.70710677F, 0.0F, 0.0F, 0.70710677F));
				matrixStack.peek().getPositionMatrix().multiply(degfToQuat(90, 1, 0, 0));
				//GL30.glRotatef(-90, 1, 0, 0);
				break;
			case 4: //Top
				//matrix = new Matrix4f(new Quaternion(-0.70710677F, 0.0F, 0.0F, 0.70710677F));
				matrixStack.peek().getPositionMatrix().multiply(degfToQuat(-90, 1, 0, 0));
				//GL30.glRotatef(90, 1, 0, 0);
				break;
			case 5: //Behind
				//matrix = new Matrix4f(new Quaternion(0.0F, -1.0F, 0.0F, 0.0F));
				matrixStack.peek().getPositionMatrix().multiply(degfToQuat(180, 0, 1, 0));
				//GL30.glRotatef(180, 0, 1, 0);
		}

	}

	public void saveRenderPass() {
		Framebuffer defaultFramebuffer = MinecraftClient.getInstance().getFramebuffer();
		Framebuffer targetFramebuffer = BufferManager.getInstance().getFramebuffer();
		targetFramebuffer.beginWrite(false);
		//GlStateManager._clear(16640, MinecraftClient.IS_SYSTEM_MAC);
		RenderSystem.clear(GL30.GL_DEPTH_BUFFER_BIT, MinecraftClient.IS_SYSTEM_MAC);
		GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, defaultFramebuffer.fbo);
		GL30.glReadBuffer(GL30.GL_COLOR_ATTACHMENT0);
		GL30.glDrawBuffer(GL30.GL_COLOR_ATTACHMENT0 + renderPass);
		GL30.glBlitFramebuffer(BufferManager.minX, BufferManager.minY, BufferManager.maxX, BufferManager.maxY, 0, 0, targetFramebuffer.textureWidth, targetFramebuffer.textureHeight, GL30.GL_COLOR_BUFFER_BIT, GL30.GL_LINEAR);
		GL30.glDrawBuffer(GL30.GL_COLOR_ATTACHMENT0);
		GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, 0);
		defaultFramebuffer.beginWrite(false);
		//GlStateManager._clear(16640, MinecraftClient.IS_SYSTEM_MAC);
		RenderSystem.clear(GL30.GL_DEPTH_BUFFER_BIT, MinecraftClient.IS_SYSTEM_MAC);
		
	}

	public void loadUniforms() {
		int shaderProgram = shaderManager.getShaderProgram();
		GL30.glUseProgram(shaderProgram);
		MinecraftClient mc = MinecraftClient.getInstance();
		Window window = mc.getWindow();
		int displayWidth = window.getWidth();
		int displayHeight = window.getHeight();
		int aaUniform = GL30.glGetUniformLocation(shaderProgram, "antialiasing");
		GL30.glUniform1i(aaUniform, getAntialiasing());
		int pixelOffsetUniform;
		int zoomUniform;
		if (getAntialiasing() == 16) {
			float left = -0.75F / (float)displayWidth;
			float top = -0.75F / (float)displayHeight;
			float right = 0.5F / (float)displayWidth;
			float bottom = 0.5F / (float)displayHeight;

			for(int y = 0; y < 4; ++y) {
				for(zoomUniform = 0; zoomUniform < 4; ++zoomUniform) {
					pixelOffsetUniform = GL30.glGetUniformLocation(shaderProgram, "pixelOffset[" + (y * 4 + zoomUniform) + "]");
					GL30.glUniform2f(pixelOffsetUniform, left + right * (float)zoomUniform, top + bottom * (float)y);
				}
			}
		} else if (getAntialiasing() == 4) {
			pixelOffsetUniform = GL30.glGetUniformLocation(shaderProgram, "pixelOffset[0]");
			GL30.glUniform2f(pixelOffsetUniform, -0.5F / (float)displayWidth, -0.5F / (float)displayHeight);
			pixelOffsetUniform = GL30.glGetUniformLocation(shaderProgram, "pixelOffset[1]");
			GL30.glUniform2f(pixelOffsetUniform, 0.5F / (float)displayWidth, -0.5F / (float)displayHeight);
			pixelOffsetUniform = GL30.glGetUniformLocation(shaderProgram, "pixelOffset[2]");
			GL30.glUniform2f(pixelOffsetUniform, -0.5F / (float)displayWidth, 0.5F / (float)displayHeight);
			pixelOffsetUniform = GL30.glGetUniformLocation(shaderProgram, "pixelOffset[3]");
			GL30.glUniform2f(pixelOffsetUniform, 0.5F / (float)displayWidth, 0.5F / (float)displayHeight);
		} else {
			pixelOffsetUniform = GL30.glGetUniformLocation(shaderProgram, "pixelOffset[0]");
			GL30.glUniform2f(pixelOffsetUniform, 0.0F, 0.0F);
		}

		int texUniform = GL30.glGetUniformLocation(shaderProgram, "texFront");
		GL30.glUniform1i(texUniform, 0);
		texUniform = GL30.glGetUniformLocation(shaderProgram, "texBack");
		GL30.glUniform1i(texUniform, 5);
		texUniform = GL30.glGetUniformLocation(shaderProgram, "texLeft");
		GL30.glUniform1i(texUniform, 2);
		texUniform = GL30.glGetUniformLocation(shaderProgram, "texRight");
		GL30.glUniform1i(texUniform, 1);
		texUniform = GL30.glGetUniformLocation(shaderProgram, "texTop");
		GL30.glUniform1i(texUniform, 4);
		texUniform = GL30.glGetUniformLocation(shaderProgram, "texBottom");
		GL30.glUniform1i(texUniform, 3);
		int fovxUniform = GL30.glGetUniformLocation(shaderProgram, "fovx");
		GL30.glUniform1f(fovxUniform, (float)getFovX());
		int fovyUniform = GL30.glGetUniformLocation(shaderProgram, "fovy");
		GL30.glUniform1f(fovyUniform, (float)getFovY());
		int backgroundUniform = GL30.glGetUniformLocation(shaderProgram, "backgroundColor");
		float[] backgroundColor = getBackgroundColor(skyBackground);
		if (backgroundColor != null) {
			GL30.glUniform4f(backgroundUniform, backgroundColor[0], backgroundColor[1], backgroundColor[2], 1.0F);
		} else {
			GL30.glUniform4f(backgroundUniform, 0.0F, 0.0F, 0.0F, 1.0F);
		}

		zoomUniform = GL30.glGetUniformLocation(shaderProgram, "zoom");
		GL30.glUniform1f(zoomUniform, (float)Math.pow(2.0, (double)(-zoom)));
		int drawCursorUniform = GL30.glGetUniformLocation(shaderProgram, "drawCursor");
		GL30.glUniform1i(drawCursorUniform, getResizeGui() && mc.currentScreen != null ? 1 : 0);
		int cursorPosUniform = GL30.glGetUniformLocation(shaderProgram, "cursorPos");
		float mouseX = (float)mc.mouse.getX() / (float)window.getWidth();
		float mouseY = (float)mc.mouse.getY() / (float)window.getHeight();
		mouseX = (mouseX - 0.5F) * (float)window.getWidth() / (float)window.getHeight() + 0.5F;
		mouseX = Math.max(0.0F, Math.min(1.0F, mouseX));
		GL30.glUniform2f(cursorPosUniform, mouseX, 1.0F - mouseY);
	}

	public void runShader() {
		Framebuffer customFramebuffer = BufferManager.getInstance().getFramebuffer();
		int[] colorArray = ((FramebufferAdditions)customFramebuffer).flexFOV$getColorArray();
		GL30.glActiveTexture(GL30.GL_TEXTURE2);
		int texture2 = GL30.glGetInteger(GL30.GL_TEXTURE_BINDING_2D);

		int i;
		for(i = 0; i < colorArray.length; ++i) {
			GL30.glActiveTexture(GL30.GL_TEXTURE0 + i);
			GL30.glBindTexture(GL30.GL_TEXTURE_2D, colorArray[i]);
		}

		BufferManager.getInstance().draw();

		for(i = colorArray.length - 1; i >= 0; --i) {
			GL30.glActiveTexture(GL30.GL_TEXTURE1);
			GL30.glBindTexture(GL30.GL_TEXTURE_2D, 0);
		}

		GL30.glActiveTexture(GL30.GL_TEXTURE2);
		GL30.glBindTexture(GL30.GL_TEXTURE_2D, texture2);
		GL30.glActiveTexture(GL30.GL_TEXTURE0);
		GL30.glUseProgram(0);
		if (getShowHand() && !getResizeGui()) {
			RenderSystem.clear(GL30.GL_DEPTH_BUFFER_BIT, MinecraftClient.IS_SYSTEM_MAC);
			((GameRendererAdditions)MinecraftClient.getInstance().gameRenderer).flexFOV$renderHand();
		}

	}

	public int getAntialiasing() {
		return antialiasing;
	}

	public float[] getBackgroundColor(boolean skyColor) {
		return skyColor ? backgroundColor : null;
	}

	public boolean getResizeGui() {
		return resizeGui && MinecraftClient.getInstance().world != null;
	}

	public boolean shouldRotateParticles() {
		return true;
	}

	public boolean getShowHand() {
		return showHand;
	}

	public double getPassFOV(double fovIn) {
		return (double)BufferManager.passFov;
	}

	public double getFovX() {
		return fov;
	}

	public double getFovY() {
		Window window = MinecraftClient.getInstance().getWindow();
		float width = (float)window.getWidth();
		float height = (float)window.getHeight();
		return getFovX() * (double)height / (double)width;
	}

	static {
		defaultProjection = Projection.Projections.FLEX;
	}

	public static enum Projections {
		CUBIC,
		CYLINDER,
		EQUIRECTANGULAR,
		FISHEYE,
		FLEX,
		HAMMER,
		PANINI,
		RECTILINEAR;

		// $FF: synthetic method
		/* 
		@SuppressWarnings("unused")
		private static Projections[] $values() {
			return new Projections[]{CUBIC, CYLINDER, EQUIRECTANGULAR, FISHEYE, FLEX, HAMMER, PANINI, RECTILINEAR};
		}*/
	}
}
