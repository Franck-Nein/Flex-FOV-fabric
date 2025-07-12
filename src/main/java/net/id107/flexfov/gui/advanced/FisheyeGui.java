package net.id107.flexfov.gui.advanced;

import net.id107.flexfov.ConfigManager;
import net.id107.flexfov.projection.Fisheye;
import net.id107.flexfov.projection.Projection;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.DoubleOption;
import net.minecraft.text.LiteralText;

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
		DoubleOption FOV = new DoubleOption("fisheyeFov", 0, fovSliderLimit, 1,
				(gameOptions) -> {return Math.min(finalSliderLimit, Projection.getInstance().getFovX());},
				(gameOptions, number) -> {Projection.fov = number; ConfigManager.saveConfig();},
				(gameOptions, doubleOption) -> {return new LiteralText("FOV: " + (int)Math.min(finalSliderLimit, Projection.getInstance().getFovX()));});
		addDrawableChild(FOV.createButton(client.options, width / 2 - 180, height / 6 + 132, fovSliderLimit));
		
		addDrawableChild(new ButtonWidget(width / 2 - 155, height / 6 + 84, 150, 20,
				new LiteralText("Background Color: " + (Projection.skyBackground ? "Sky" : "Black")), (buttonWidget) -> {
					Projection.skyBackground = !Projection.skyBackground;
					buttonWidget.setMessage(new LiteralText("Background Color: " + (Projection.skyBackground ? "Sky" : "Black")));
					ConfigManager.saveConfig();
				}));
				addDrawableChild(new ButtonWidget(width / 2 - 155, height / 6 + 108, 150, 20,
				new LiteralText("Full Frame: " + (Fisheye.fullFrame ? "ON" : "OFF")), (buttonWidget) -> {
					Fisheye.fullFrame = !Fisheye.fullFrame;
					buttonWidget.setMessage(new LiteralText("Full Frame: " + (Fisheye.fullFrame ? "ON" : "OFF")));
					ConfigManager.saveConfig();
				}));
	}
}
