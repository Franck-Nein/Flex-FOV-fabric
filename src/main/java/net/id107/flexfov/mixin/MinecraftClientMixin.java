package net.id107.flexfov.mixin;

import net.id107.flexfov.BufferManager;
import net.id107.flexfov.projection.Projection;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({MinecraftClient.class})
public abstract class MinecraftClientMixin {
	@Inject(
		method = {"onResolutionChanged"},
		at = {@At(
	value = "INVOKE",
	target = "Lnet/minecraft/client/gl/Framebuffer;resize(IIZ)V"
)}
	)
	private void resolutionChanged(CallbackInfo ci) {
		BufferManager.getInstance().getFramebuffer().resize(MinecraftClient.getInstance().getWindow().getFramebufferWidth(), MinecraftClient.getInstance().getWindow().getFramebufferHeight(), MinecraftClient.IS_SYSTEM_MAC);
	}

	@Inject(
		method = {"stop"},
		at = {@At("HEAD")}
	)
	private void cleanup(CallbackInfo ci) {
		Projection.deleteProjection();
	}
}
