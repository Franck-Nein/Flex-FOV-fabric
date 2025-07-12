package net.id107.flexfov.gui.advanced;

import net.id107.flexfov.ConfigManager;
import net.id107.flexfov.projection.Projection;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.DoubleOption;
import net.minecraft.text.LiteralText;

public class PaniniGui extends AdvancedGui {
	public PaniniGui(Screen parent) {
		super(parent);
		Projection.setProjection(Projection.Projections.PANINI);
	}
	
	protected void init() {
		super.init();
		DoubleOption FOV = new DoubleOption("paniniFov", 0.0, 360.0, 1,
				(gameOptions) -> {return Projection.getInstance().getFovX();},
				(gameOptions, number) -> {Projection.fov = number; ConfigManager.saveConfig();},
				(gameOptions, doubleOption) -> {return new LiteralText("FOV: " + (int)Projection.getInstance().getFovX());});
		addDrawableChild(FOV.createButton(client.options, width / 2 - 180, height / 6 + 60, 360));
	}
}
