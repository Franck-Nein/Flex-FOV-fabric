package net.id107.flexfov.mixin;

import net.id107.flexfov.BufferManager;
import net.id107.flexfov.mixinHelpers.FramebufferAdditions;
import net.minecraft.client.gl.Framebuffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({Framebuffer.class})
public abstract class FramebufferMixin {
	@SuppressWarnings("unlikely-arg-type")
	@Inject(
		method = {"initFbo"},
		at = {@At(
	value = "INVOKE",
	target = "Lnet/minecraft/client/gl/Framebuffer;checkFramebufferStatus()V"
)}
	)
	private void resize(int width, int height, boolean getError, CallbackInfo ci) {
		if (BufferManager.getInstance().getFramebuffer().equals(this)) {
			((FramebufferAdditions)this).flexFOV$createBindColorArray(width, height);
		}else{
		}
	}
}
