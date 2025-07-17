package net.id107.flexfov.mixin;

import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({WorldRenderer.class})
public abstract class WorldRendererMixin {
	@Shadow
	private double lastCameraPitch;

	@Inject(
		method = {"setupTerrain"},
		at = {@At(
	value = "INVOKE",
	target = "Lnet/minecraft/client/render/ChunkRenderingDataPreparer;method_52836()Z"
)}
	)
	private void setFrustum(CallbackInfo ci) {
		lastCameraPitch = Double.NaN;
	}
}
