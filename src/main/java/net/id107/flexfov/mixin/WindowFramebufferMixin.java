package net.id107.flexfov.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.TextureUtil;
import java.nio.IntBuffer;
import net.id107.flexfov.BufferManager;
import net.id107.flexfov.FlexFOV;
import net.id107.flexfov.mixinHelpers.FramebufferAdditions;
import net.minecraft.client.gl.WindowFramebuffer;

import org.lwjgl.opengl.GL30;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({WindowFramebuffer.class})
public abstract class WindowFramebufferMixin implements FramebufferAdditions {
	@Unique
	private final int[] colorArray = new int[6];

	@Inject(
		method = {"init"},
		at = {@At(
	value = "INVOKE",
	target = "Lnet/minecraft/client/gl/WindowFramebuffer;checkFramebufferStatus()V"
)}
	)
	
	private void init(int width, int height, CallbackInfo ci) {
		if (BufferManager.inConstructor) {
			flexFOV$createBindColorArray(width, height);
		}
		
	}
	
	@Unique
	public void flexFOV$createBindColorArray(int width, int height) {
		int size = Math.min(width, height);
		FlexFOV.LOGGER.info("width "+ width);
		FlexFOV.LOGGER.info("height "+ height);
	  try {		
	  } catch (Exception e) {
		FlexFOV.LOGGER.info("Crash BufferManager.framebuffer.texture...");
	  }
		
		int i;
		float aspectRatio = (float)width / (float)height;
		if (aspectRatio>=1) {
			FlexFOV.LOGGER.info("width >= height");
			
			BufferManager.passFov = 90.0F;
			i = (width - height) / 2;
			BufferManager.minX = i;
			BufferManager.maxX = width+i;
			BufferManager.minY = 0;
			BufferManager.maxY = height;


		} else {
			FlexFOV.LOGGER.info("width >= height Not!!");
			
			BufferManager.passFov = (float)Math.toDegrees(2.0 * Math.atan(Math.tan(Math.toRadians(45.0)) / (double)aspectRatio));
			int diff = (height - width) / 2;
			BufferManager.minX = 0;
			BufferManager.maxX = width;
			BufferManager.minY = diff;
			BufferManager.maxY = diff + height;
		}

		if (colorArray[0] >= 0) {
			for(i = 0; i < colorArray.length; ++i) {
				TextureUtil.releaseTextureId(colorArray[i]);
				colorArray[i] = -1;
			}
		}

		for(i = 0; i < colorArray.length; ++i) {
			colorArray[i] = TextureUtil.generateTextureId();
			GlStateManager._bindTexture(colorArray[i]);
			GlStateManager._texImage2D(GL30.GL_TEXTURE_2D, 0, GL30.GL_RGBA8, size, size, 0, GL30.GL_RGBA, GL30.GL_UNSIGNED_BYTE, (IntBuffer)null);
			GlStateManager._texParameter(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MIN_FILTER, GL30.GL_LINEAR);
			GlStateManager._texParameter(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MAG_FILTER, GL30.GL_LINEAR);
			GlStateManager._texParameter(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_WRAP_S, GL30.GL_CLAMP_TO_EDGE);
			GlStateManager._texParameter(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_WRAP_T, GL30.GL_CLAMP_TO_EDGE);
			GlStateManager._glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0 + i, GL30.GL_TEXTURE_2D, colorArray[i], 0);
		}

	}

	@Unique
	public int[] flexFOV$getColorArray() {
		return colorArray;
	}
}
