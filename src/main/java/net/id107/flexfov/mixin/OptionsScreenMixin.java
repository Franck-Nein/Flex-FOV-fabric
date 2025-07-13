package net.id107.flexfov.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.id107.flexfov.gui.SettingsGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

@Mixin({OptionsScreen.class})
public abstract class OptionsScreenMixin extends Screen {
	protected OptionsScreenMixin(Text title) {
		super(title);
	}
	@Inject(
		method = {"init()V"},
		at = {@At(
	value = "TAIL",
	target = "Lnet/minecraft/client/gui/screen/Screen;addDrawableChild(Lnet/minecraft/client/gui/Element;)Lnet/minecraft/client/gui/Element;",
	ordinal = 4
)}
	)
	private void addDrawableChild(CallbackInfo ci) {
		addDrawableChild(ButtonWidget.builder(
			Text.literal("Flex FOV Settings"),
			(button) -> client.setScreen(SettingsGui.getGui(this)))
			.position(width / 2 - 155, height / 6 + 15)
			.size(150, 20)
			.build());
	}
}
