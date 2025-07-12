package net.id107.flexfov.gui.advanced;

import net.id107.flexfov.ConfigManager;
import net.id107.flexfov.gui.SettingsGui;
import net.id107.flexfov.projection.Projection;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.DoubleOption;
import net.minecraft.text.LiteralText;

public class AdvancedGui extends SettingsGui {
	public static int currentGui = 5;

	public AdvancedGui(Screen parent) {
		super(parent);
	}

	public static AdvancedGui getGui(Screen parent) {
		switch(currentGui) {
		case 0:
		default:
			return new CubicGui(parent);
		case 1:
			return new HammerGui(parent);
		case 2:
			return new PaniniGui(parent);
		case 3:
			return new CylinderGui(parent);
		case 4:
			return new FisheyeGui(parent);
		case 5:
			return new EquirectangularGui(parent);
		}
	}

	protected void init() {
		super.init();
		mkButton(-180, 12, 100, 20, "Cubic", this instanceof CubicGui, (buttonWidget) -> {
			currentGui = 0;
			client.setScreen(new CubicGui(parentScreen));
		});

		mkButton(-50, 12, 100, 20, "Hammer", this instanceof HammerGui, (buttonWidget) -> {
			currentGui = 1;
			client.setScreen(new HammerGui(parentScreen));
		});


		mkButton(80, 12, 100, 20, "Panini", this instanceof PaniniGui, (buttonWidget) -> {
			currentGui = 2;
			client.setScreen(new PaniniGui(parentScreen));
		});

		mkButton(- 180, 36, 100, 20, "Cylinder", this instanceof CylinderGui, (buttonWidget) -> {
			currentGui = 3;
			client.setScreen(new CylinderGui(parentScreen));
		});

		mkButton(- 50, 36, 100, 20, "Fisheye", this instanceof FisheyeGui, (buttonWidget) -> {
			currentGui = 4;
			client.setScreen(new FisheyeGui(parentScreen));
		});

		mkButton(80, 36, 100, 20, "Equirectangular", this instanceof EquirectangularGui, (buttonWidget) -> {
			currentGui = 5;
			client.setScreen(new EquirectangularGui(parentScreen));
		});

		if (!(this instanceof CubicGui)) {
			DoubleOption zoom = new DoubleOption("zoom", -2, 2, 0.05f,
					(gameOptions) -> {return (double) Projection.zoom;},
					(gameOptions, number) -> {Projection.zoom = (float)(double)number; ConfigManager.saveConfig();},
					(gameOptions, doubleOption) -> {return new LiteralText(String.format("Zoom: %.2f", Projection.zoom));});
			addDrawableChild(zoom.createButton(client.options, width / 2 + 5, height / 6 + 84, 150));
		}

		if (!(this instanceof FisheyeGui)) {
			addDrawableChild(new ButtonWidget(width / 2 - 155, height / 6 + 108, 150, 20, new LiteralText("Show Hand: " + (Projection.showHand ? "ON" : "OFF")), (buttonWidget) -> {
				Projection.showHand = !Projection.showHand;
				ConfigManager.saveConfig();
				buttonWidget.setMessage(new LiteralText("Show Hand: " + (Projection.showHand ? "ON" : "OFF")));
			}));
		}

		addDrawableChild(new ButtonWidget(width / 2 + 5, height / 6 + 108, 150, 20,
				new LiteralText("Resize Gui: " + (Projection.resizeGui ? "ON" : "OFF")), (buttonWidget) -> {
					Projection.resizeGui = !Projection.resizeGui;
					buttonWidget.setMessage(new LiteralText("Resize Gui: " + (Projection.resizeGui ? "ON" : "OFF")));
				}));
	}
}
