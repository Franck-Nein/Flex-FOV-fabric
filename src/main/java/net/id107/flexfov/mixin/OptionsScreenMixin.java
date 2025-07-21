package net.id107.flexfov.mixin;

import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.client.gui.widget.GridWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

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
				value = "INVOKE",
				target = "Lnet/minecraft/client/gui/screen/option/OptionsScreen;createButton(Lnet/minecraft/text/Text;Ljava/util/function/Supplier;)Lnet/minecraft/client/gui/widget/ButtonWidget;",
				ordinal = 0
		)},
		locals = LocalCapture.CAPTURE_FAILHARD
	)
	private void addCustomButton(CallbackInfo ci, DirectionalLayoutWidget directionalLayoutWidget) {
		directionalLayoutWidget.add(ButtonWidget.builder(
			Text.literal("Flex FOV Settings"),
			(button) -> this.client.setScreen(SettingsGui.getGui(this))
			).build()
		);
	}
}
