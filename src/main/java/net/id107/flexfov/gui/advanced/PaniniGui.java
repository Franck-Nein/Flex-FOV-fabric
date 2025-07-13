package net.id107.flexfov.gui.advanced;

import net.id107.flexfov.ConfigManager;
import net.id107.flexfov.projection.Projection;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;

public class PaniniGui extends AdvancedGui {
	public PaniniGui(Screen parent) {
		super(parent);
		Projection.setProjection(Projection.Projections.PANINI);
	}
	
	protected void init() {
		super.init();
		// DoubleOption FOV = new DoubleOption("paniniFov", 0.0, 360.0, 1,
		// 		(gameOptions) -> {return Projection.getInstance().getFovX();},
		// 		(gameOptions, number) -> {Projection.fov = number; ConfigManager.saveConfig();},
		// 		(gameOptions, doubleOption) -> {return Text.literal("FOV: " + (int)Projection.getInstance().getFovX());});


		SimpleOption<Double> FOV = new SimpleOption<>(
			"paniniFov", // Identifier for localization
			SimpleOption.emptyTooltip(),
			(gameOptions, value) -> Text.literal("FOV: " + (int) (value * 360)), // Map [0,1] to [0,360] for display
			SimpleOption.DoubleSliderCallbacks.INSTANCE, // Use DoubleSliderCallbacks with range [0,1]
			Projection.getInstance().getFovX() / 360, // Default value normalized to [0,1]
			(value) -> {
				Projection.fov = value * 360; // Map [0,1] to [0,360] and save
				ConfigManager.saveConfig();
			}
	);

	addDrawableChild(FOV.createWidget(client.options, width / 2 - 180, height / 6 + 60, 360));
	}
}
