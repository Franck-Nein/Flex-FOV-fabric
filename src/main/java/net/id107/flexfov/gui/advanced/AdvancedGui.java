package net.id107.flexfov.gui.advanced;

import net.id107.flexfov.ConfigManager;
import net.id107.flexfov.gui.SettingsGui;
import net.id107.flexfov.projection.Projection;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;

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
			// DoubleOption zoom = new DoubleOption(
			// 	"zoom",
			// 	-2, 2, 0.05f,
			// 	(gameOptions) -> {return (double) Projection.zoom;},
			// 	(gameOptions, number) -> {Projection.zoom = (float)(double)number; ConfigManager.saveConfig();},
			// 	(gameOptions, doubleOption) -> {return Text.literal(String.format("Zoom: %.2f", Projection.zoom));});
			SimpleOption<Double> zoom = new SimpleOption<Double>(
				"zoom", // Identifier for localization
				SimpleOption.emptyTooltip(), // Slider callbacks for handling double values
				(optionText, value) -> Text.literal(String.format("Zoom: %.2f", value * 4 - 2)), // Tooltip or text update
				SimpleOption.DoubleSliderCallbacks.INSTANCE,// Use DoubleSliderCallbacks because i dont wanna screw with anything else and this  is already there
				0.5, // Default value// Default value mapped to 0 (in [-2,2]) => 0.5 in [0,1]
				(value) -> {
					Projection.zoom = (float)((value * 4) - 2); // Map slider's range [0,1] -> [-2,2]
					ConfigManager.saveConfig();
				} // Save handler
				);
				addDrawableChild(zoom.createWidget(client.options, width / 2 + 5, height / 6 + 84, 150));
			}

		if (!(this instanceof FisheyeGui)) {
			addDrawableChild(ButtonWidget.builder(
			Text.literal("Show Hand: " + (Projection.showHand ? "ON" : "OFF")), (button) -> {
				Projection.showHand = !Projection.showHand;
				ConfigManager.saveConfig();
				button.setMessage(Text.literal("Show Hand: " + (Projection.showHand ? "ON" : "OFF")));
			})
			.position(width / 2 - 155, height / 6 + 108)
			.size(150, 20)
			.build());
		}

		addDrawableChild(ButtonWidget.builder(
			Text.literal("Resize Gui: " + (Projection.resizeGui ? "ON" : "OFF")), (button) -> {
				Projection.resizeGui = !Projection.resizeGui;
				button.setMessage(Text.literal("Resize Gui: " + (Projection.resizeGui ? "ON" : "OFF")));
			})
			.position(width / 2 + 5, height / 6 + 108)
			.size(150, 20)
			.build());
	}
}
