package net.id107.flexfov.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.id107.flexfov.projection.Projection;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;

@Mixin(Screen.class)
public abstract class ScreenMixin {

	@Inject(
		method = "renderBackground",
		at = @At(value = "HEAD"), 
		cancellable = true)

	private void renderBackground(CallbackInfo ci) {
		  if (
			MinecraftClient.getInstance().world != null &&
				(
				Projection.getInstance().getResizeGui() ||
				false
				)
			) 
			{
					ci.cancel();
				}

	}
}
