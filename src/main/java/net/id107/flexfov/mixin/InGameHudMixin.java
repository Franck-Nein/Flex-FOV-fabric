package net.id107.flexfov.mixin;

import net.id107.flexfov.projection.Projection;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin({InGameHud.class})
public class InGameHudMixin {
	@ModifyArg(
		method = {"renderVignetteOverlay"},
		at = @At(
	value = "INVOKE",
	target = "Lnet/minecraft/util/math/MathHelper;clamp(FFF)F"
),
		index = 0
	)
	private float renderVignette(float value) {
		return Projection.getInstance().getResizeGui() ? 0.0F : value;
	}
}
