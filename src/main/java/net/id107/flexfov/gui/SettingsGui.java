package net.id107.flexfov.gui;

import net.id107.flexfov.gui.advanced.AdvancedGui;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public abstract class SettingsGui extends Screen {

	protected final Screen parentScreen;
	
	public static int currentGui = 1;
	
	public SettingsGui(Screen parent) {
		super(Text.literal("Flex FOV Settings"));
		parentScreen = parent;
	}
	
	public static SettingsGui getGui(Screen parent) {
		switch (currentGui) {
		case 0:
		default:
			return new RectilinearGui(parent);
		case 1:
			return new FlexGui(parent);
		case 2:
			return AdvancedGui.getGui(parent);
		}
	}
	
	public ButtonWidget mkButton(int x, int y, int buttonWidth, int buttonHeight, String text, boolean isInactive, ButtonWidget.PressAction action) {
		ButtonWidget button = ButtonWidget.builder(Text.literal(text), action)
				.position(width / 2 + x, height / 6 + y)
				.size(buttonWidth, buttonHeight)
				.build();
		button.active=!isInactive;
		addDrawableChild(button);
		return button;
	}

	protected void init() {
		mkButton(-190, -12, 120, 20, "Default",this instanceof RectilinearGui, (buttonWidget) -> {
			currentGui = 0;
			client.setScreen(new RectilinearGui(parentScreen));
		});
		
		
		mkButton(-60, -12, 120, 20, "Flex", this instanceof FlexGui, (buttonWidget) -> {
			currentGui = 1;
			client.setScreen(new FlexGui(parentScreen));
		});

		
		mkButton(70, -12, 120, 20, "Advanced", this instanceof AdvancedGui, (buttonWidget) -> {
			currentGui = 2;
			client.setScreen(AdvancedGui.getGui(parentScreen));
		});

		
		mkButton(-100, 168, 200, 20, "Done", false,  (buttonWidget) -> {
			client.setScreen(parentScreen);
		});
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		this.renderBackground(context);
		context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 15, 16777215);
		super.render(context, mouseX, mouseY, delta);
	}
}
