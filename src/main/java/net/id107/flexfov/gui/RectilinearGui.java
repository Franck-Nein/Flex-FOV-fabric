package net.id107.flexfov.gui;

import net.id107.flexfov.projection.Projection;
import net.minecraft.client.gui.screen.Screen;

public class RectilinearGui extends SettingsGui {
	public RectilinearGui(Screen parent) {
		super(parent);
		Projection.setProjection(Projection.Projections.RECTILINEAR);
	}
}
