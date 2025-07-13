package net.id107.flexfov.gui.advanced;

import net.id107.flexfov.ConfigManager;
import net.id107.flexfov.projection.Projection;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class CubicGui extends AdvancedGui {

	public CubicGui(Screen parent) {
		super(parent);
		Projection.setProjection(Projection.Projections.CUBIC);
	}

	protected void init() {
		super.init();
		
		addDrawableChild(ButtonWidget.builder(
				Text.literal("Background Color: " + (Projection.skyBackground ? "Sky" : "Black")),(button) -> {
					Projection.skyBackground = !Projection.skyBackground;
					button.setMessage(Text.literal("Background Color: " + (Projection.skyBackground ? "Sky" : "Black")));
					ConfigManager.saveConfig();
				})
				.position(width / 2 - 155, height / 6 + 84)
				.size(150, 20)
				.build());
	}
}
