package net.id107.flexfov.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.id107.flexfov.projection.Projection;
import net.minecraft.client.render.BackgroundRenderer;

@Mixin({BackgroundRenderer.class})
public abstract class BackgroundRendererMixin {
	@Shadow
	private static float red;
	@Shadow
	private static float green;
	@Shadow
	private static float blue;

	@Inject(
		method = {"render"},
		at = {@At("TAIL")}
	)
	private static void skyColor(CallbackInfo ci) {
		Projection.backgroundColor[0] = red;
		Projection.backgroundColor[1] = green;
		Projection.backgroundColor[2] = blue;
	}
}
