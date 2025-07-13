package net.id107.flexfov.gui.advanced;

import net.id107.flexfov.ConfigManager;
import net.id107.flexfov.projection.Equirectangular;
import net.id107.flexfov.projection.Projection;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class EquirectangularGui extends AdvancedGui {
	public EquirectangularGui(Screen parent) {
		super(parent);
		Projection.setProjection(Projection.Projections.EQUIRECTANGULAR);
	}

	protected void init() {
		super.init();

		addDrawableChild(ButtonWidget.builder(
				Text.literal("Show Circle: " + (Equirectangular.drawCircle ? "ON" : "OFF")), (button) -> {
					Equirectangular.drawCircle = !Equirectangular.drawCircle;
					button.setMessage(Text.literal("Show Circle: " + (Equirectangular.drawCircle ? "ON" : "OFF")));
					ConfigManager.saveConfig();
				})
				.position(width / 2 - 155, height / 6 + 84)
				.size(150, 20)
				.build());

		addDrawableChild(ButtonWidget.builder(
				Text.literal("Stabilize Pitch: " + (Equirectangular.stabilizePitch ? "ON" : "OFF")), (button) -> {
					Equirectangular.stabilizePitch = !Equirectangular.stabilizePitch;
					button.setMessage(Text.literal("Stabilize Pitch: " + (Equirectangular.stabilizePitch ? "ON" : "OFF")));
					ConfigManager.saveConfig();
				})
				.position(width / 2 - 155, height / 6 + 132)
				.size(150, 20)
				.build());

addDrawableChild(ButtonWidget.builder(
				Text.literal("Stabilize Yaw: " + (Equirectangular.stabilizeYaw ? "ON" : "OFF")),
				(button) -> {
					Equirectangular.stabilizeYaw = !Equirectangular.stabilizeYaw;
					button.setMessage(Text.literal("Stabilize Yaw: " + (Equirectangular.stabilizeYaw ? "ON" : "OFF")));
					ConfigManager.saveConfig();
				})
				.position(width / 2 + 5, height / 6 + 132)
				.size(150, 20)
				.build());
	}
}
