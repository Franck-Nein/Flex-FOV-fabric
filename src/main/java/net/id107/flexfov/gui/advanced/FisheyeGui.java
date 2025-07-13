package net.id107.flexfov.gui.advanced;

import net.id107.flexfov.ConfigManager;
import net.id107.flexfov.projection.Fisheye;
import net.id107.flexfov.projection.Projection;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;

public class FisheyeGui extends AdvancedGui {
	public FisheyeGui(Screen parent) {
		super(parent);
		Projection.setProjection(Projection.Projections.FISHEYE);
	}

	protected void init() {
		super.init();
		mkButton(-190, 60	, 76, 20, "Orthographic", Fisheye.fisheyeType == 0, (buttonWidget) -> {
			Fisheye.fisheyeType = 0;
			client.setScreen(new FisheyeGui(parentScreen));
		});

		mkButton(- 114, 60, 76, 20, "Thoby", Fisheye.fisheyeType == 1, (buttonWidget) -> {
			Fisheye.fisheyeType = 1;
			client.setScreen(new FisheyeGui(parentScreen));
		});

		mkButton(- 38, 60, 76, 20, "Equisolid",Fisheye.fisheyeType == 2, (buttonWidget) -> {
			Fisheye.fisheyeType = 2;
			client.setScreen(new FisheyeGui(parentScreen));
		});

		mkButton(38, 60, 76, 20, "Equidistant", Fisheye.fisheyeType == 3, (buttonWidget) -> {
			Fisheye.fisheyeType = 3;
			client.setScreen(new FisheyeGui(parentScreen));
		});

		mkButton(114, 60, 76, 20, "Stereographic", Fisheye.fisheyeType == 4, (buttonWidget) -> {
			Fisheye.fisheyeType = 4;
			client.setScreen(new FisheyeGui(parentScreen));
		});

		int fovSliderLimit = 360;
		if (Fisheye.fisheyeType == 1) fovSliderLimit = (int)Math.ceil(fovSliderLimit*0.713); //Thoby 256.68 degrees, slider goes up to 257
		if (Fisheye.fisheyeType == 0) fovSliderLimit = 180; //Orthographic
		int finalSliderLimit = fovSliderLimit; // WHY?
		SimpleOption<Double> FOV = new SimpleOption<>(
			"fisheyeFov", // Identifier for localization
			SimpleOption.emptyTooltip(),
			(gameOptions, value) -> Text.literal("FOV: " + (int) (value * finalSliderLimit)), // Map [0,1] to [0, fovSliderLimit] for display
			SimpleOption.DoubleSliderCallbacks.INSTANCE, // Use DoubleSliderCallbacks with range [0,1]
			Math.min(finalSliderLimit, Projection.getInstance().getFovX()) / fovSliderLimit, // Default normalized to [0,1]
			(value) -> {
				Projection.fov = value * finalSliderLimit; // Map [0,1] to [0, fovSliderLimit] and save
				ConfigManager.saveConfig();
			}
	);
	addDrawableChild(FOV.createWidget(client.options, width / 2 - 180, height / 6 + 132, fovSliderLimit));
		
		addDrawableChild(ButtonWidget.builder(
				Text.literal("Background Color: " + (Projection.skyBackground ? "Sky" : "Black")), (button) -> {
					Projection.skyBackground = !Projection.skyBackground;
					button.setMessage(Text.literal("Background Color: " + (Projection.skyBackground ? "Sky" : "Black")));
					ConfigManager.saveConfig();
				})
				.position(width / 2 - 155, height / 6 + 84)
				.size(150, 20)
				.build());

		addDrawableChild(ButtonWidget.builder(
				Text.literal("Full Frame: " + (Fisheye.fullFrame ? "ON" : "OFF")), (button) -> {
					Fisheye.fullFrame = !Fisheye.fullFrame;
					button.setMessage(Text.literal("Full Frame: " + (Fisheye.fullFrame ? "ON" : "OFF")));
					ConfigManager.saveConfig();
				})
				.position(width / 2 - 155, height / 6 + 108)
				.size(150, 20)
				.build());
	}
}
