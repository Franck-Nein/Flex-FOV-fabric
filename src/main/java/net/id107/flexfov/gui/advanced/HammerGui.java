package net.id107.flexfov.gui.advanced;

import net.id107.flexfov.ConfigManager;
import net.id107.flexfov.projection.Projection;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class HammerGui extends AdvancedGui {

	public HammerGui(Screen parent) {
		super(parent);
		Projection.setProjection(Projection.Projections.HAMMER);
	}
	
	protected void init() {
		super.init();
		
		addDrawableChild(ButtonWidget.builder(
			Text.literal("Background Color: " + (Projection.skyBackground ? "Sky" : "Black")), (button) -> {
					Projection.skyBackground = !Projection.skyBackground;
					button.setMessage(Text.literal("Background Color: " + (Projection.skyBackground ? "Sky" : "Black")));
					ConfigManager.saveConfig();
				})
				.position(width / 2 - 155, height / 6 + 84)
				.size(150, 20)
				.build());
	}
}
