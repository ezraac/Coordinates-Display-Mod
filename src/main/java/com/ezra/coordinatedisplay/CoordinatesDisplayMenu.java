package com.ezra.coordinatedisplay;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class CoordinatesDisplayMenu extends Screen {
	
	private static int redValue;
	private static int greenValue;
	private static int blueValue;
	private static int alphaValue;
	
	private ButtonWidget toggleButton;
	private ModSettings.Settings settings;

	public CoordinatesDisplayMenu(Text title) {
		super(title);
	}
	
	@Override
	protected void init() {
		// Getting settings from JSON
		settings = ModSettings.get();
		parseGuiSettings();

		// Display on/off button
		toggleButton = ButtonWidget.builder(
				Text.of("Display: " + (CoordinatesDisplayClient.displayOn ? "On" : "Off")),
				builder -> {
					CoordinatesDisplayClient.displayOn = !CoordinatesDisplayClient.displayOn;
					toggleButton.setMessage(Text.of("Display: " + (CoordinatesDisplayClient.displayOn ? "On" : "Off")));
				})
				.position(10, 10)
				.size(100, 20)
				.build();
		
		addDrawableChild(toggleButton);
		// Red Slider
		addDrawableChild(new ColorSlider(10, 50, 100, 20, "Red", redValue/255d, r -> {
			int a = (CoordinatesDisplayClient.color >> 24) & 0xFF;
			int g = (CoordinatesDisplayClient.color >> 8) & 0xFF;
			int b = CoordinatesDisplayClient.color & 0xFF;
			CoordinatesDisplayClient.color = (a << 24) | (r << 16) | (g << 8) | b;
			this.redValue = r;
			settings.color.red = r;
		}));
		
		// Green Slider
		addDrawableChild(new ColorSlider(10, 80, 100, 20, "Green", greenValue/255d, g -> {
			int a = (CoordinatesDisplayClient.color >> 24) & 0xFF;
			int r = (CoordinatesDisplayClient.color >> 16) & 0xFF;
			int b = CoordinatesDisplayClient.color & 0xFF;
			CoordinatesDisplayClient.color = (a << 24) | (r << 16) | (g << 8) | b;
			this.greenValue = g;
			settings.color.green = g;
		}));
		
		// Blue Slider
		addDrawableChild(new ColorSlider(10, 110, 100, 20, "Blue", blueValue/255d, b -> {
			int a = (CoordinatesDisplayClient.color >> 24) & 0xFF;
			int r = (CoordinatesDisplayClient.color >> 16) & 0xFF;
			int g = (CoordinatesDisplayClient.color >> 8) & 0xFF;
			CoordinatesDisplayClient.color = (a << 24) | (r << 16) | (g << 8) | b;
			this.blueValue = b;
			settings.color.blue = b;
		}));
		
		// Opacity Slider
		addDrawableChild(new ColorSlider(10, 140, 100, 20, "Opacity", alphaValue/255d, a -> {
			int r = (CoordinatesDisplayClient.color >> 16) & 0xFF;
			int g = (CoordinatesDisplayClient.color >> 8) & 0xFF;
			int b = CoordinatesDisplayClient.color & 0xFF;
			
			CoordinatesDisplayClient.color = (alphaValue << 24) | (r << 16) | (g << 8) | b;
			this.alphaValue = a;
			settings.color.alpha = a;
		}));
	}

	private void parseGuiSettings() {
		redValue = settings.color.red;
		greenValue = settings.color.green;
		blueValue = settings.color.blue;
		alphaValue = settings.color.alpha;
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		// Check if the key pressed matches your toggle key
		if (keyCode == GLFW.GLFW_KEY_L) {
			MinecraftClient.getInstance().setScreen(null); // Close the menu
			return true;
		}

		return super.keyPressed(keyCode, scanCode, modifiers);
	}

}
