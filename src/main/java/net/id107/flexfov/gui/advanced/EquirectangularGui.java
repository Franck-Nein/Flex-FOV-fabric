package net.id107.flexfov.gui.advanced;

import net.id107.flexfov.ConfigManager;
import net.id107.flexfov.projection.Equirectangular;
import net.id107.flexfov.projection.Projection;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.LiteralText;

public class EquirectangularGui extends AdvancedGui {
	public EquirectangularGui(Screen parent) {
		super(parent);
		Projection.setProjection(Projection.Projections.EQUIRECTANGULAR);
	}

	protected void init() {
		super.init();

		addDrawableChild(new ButtonWidget(width / 2 - 155, height / 6 + 84, 150, 20,
				new LiteralText("Show Circle: " + (Equirectangular.drawCircle ? "ON" : "OFF")), (buttonWidget) -> {
					Equirectangular.drawCircle = !Equirectangular.drawCircle;
					buttonWidget.setMessage(new LiteralText("Show Circle: " + (Equirectangular.drawCircle ? "ON" : "OFF")));
					ConfigManager.saveConfig();
				}));
		addDrawableChild(new ButtonWidget(width / 2 - 155, height / 6 + 132, 150, 20,
				new LiteralText("Stabilize Pitch: " + (Equirectangular.stabilizePitch ? "ON" : "OFF")), (buttonWidget) -> {
					Equirectangular.stabilizePitch = !Equirectangular.stabilizePitch;
					buttonWidget.setMessage(new LiteralText("Stabilize Pitch: " + (Equirectangular.stabilizePitch ? "ON" : "OFF")));
					ConfigManager.saveConfig();
				}));
		addDrawableChild(new ButtonWidget(width / 2 + 5, height / 6 + 132, 150, 20,
				new LiteralText("Stabilize Yaw: " + (Equirectangular.stabilizeYaw ? "ON" : "OFF")), (buttonWidget) -> {
					Equirectangular.stabilizeYaw = !Equirectangular.stabilizeYaw;
					buttonWidget.setMessage(new LiteralText("Stabilize Yaw: " + (Equirectangular.stabilizeYaw ? "ON" : "OFF")));
					ConfigManager.saveConfig();
				}));
	}
}
