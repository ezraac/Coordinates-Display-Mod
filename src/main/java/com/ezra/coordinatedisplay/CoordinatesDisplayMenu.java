package com.ezra.coordinatedisplay;

import net.minecraft.client.input.KeyInput;
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

		double width = MinecraftClient.getInstance().getWindow().getScaledWidth();
		double height = MinecraftClient.getInstance().getWindow().getScaledHeight();
		int startX = (int) (width * 0.05);
		int startY = (int) (height * 0.05);

		int sliderWidth = (int) (width * 0.25);
		int sliderHeight = (int) (height * 0.075);

		int padding = settings.size.padding;

		int nextColorY = startY;

		// Display on/off button
		toggleButton = ButtonWidget.builder(
				Text.of("Display: " + (CoordinatesDisplayClient.displayOn ? "On" : "Off")),
				builder -> {
					CoordinatesDisplayClient.displayOn = !CoordinatesDisplayClient.displayOn;
					toggleButton.setMessage(Text.of("Display: " + (CoordinatesDisplayClient.displayOn ? "On" : "Off")));
				})
				.position(startX, startY)
				.size(sliderWidth, sliderHeight)
				.build();
		
		addDrawableChild(toggleButton);

		// Red Slider
		addDrawableChild(new ColorSlider(startX, nextColorY += sliderHeight + padding, sliderWidth, sliderHeight, "Red", redValue/255d, r -> {
			int a = (CoordinatesDisplayClient.color >> 24) & 0xFF;
			int g = (CoordinatesDisplayClient.color >> 8) & 0xFF;
			int b = CoordinatesDisplayClient.color & 0xFF;
			CoordinatesDisplayClient.color = (a << 24) | (r << 16) | (g << 8) | b;
			this.redValue = r;
			settings.color.red = r;
		}));
		
		// Green Slider
		addDrawableChild(new ColorSlider(startX, nextColorY += sliderHeight + padding, sliderWidth, sliderHeight, "Green", greenValue/255d, g -> {
			int a = (CoordinatesDisplayClient.color >> 24) & 0xFF;
			int r = (CoordinatesDisplayClient.color >> 16) & 0xFF;
			int b = CoordinatesDisplayClient.color & 0xFF;
			CoordinatesDisplayClient.color = (a << 24) | (r << 16) | (g << 8) | b;
			this.greenValue = g;
			settings.color.green = g;
		}));
		
		// Blue Slider
		addDrawableChild(new ColorSlider(startX, nextColorY += sliderHeight + padding, sliderWidth, sliderHeight, "Blue", blueValue/255d, b -> {
			int a = (CoordinatesDisplayClient.color >> 24) & 0xFF;
			int r = (CoordinatesDisplayClient.color >> 16) & 0xFF;
			int g = (CoordinatesDisplayClient.color >> 8) & 0xFF;
			CoordinatesDisplayClient.color = (a << 24) | (r << 16) | (g << 8) | b;
			this.blueValue = b;
			settings.color.blue = b;
		}));
		
		// Opacity Slider
		addDrawableChild(new ColorSlider(startX, nextColorY + (sliderHeight + padding), sliderWidth, sliderHeight, "Opacity", alphaValue/255d, a -> {
			int r = (CoordinatesDisplayClient.color >> 16) & 0xFF;
			int g = (CoordinatesDisplayClient.color >> 8) & 0xFF;
			int b = CoordinatesDisplayClient.color & 0xFF;
			
			CoordinatesDisplayClient.color = (alphaValue << 24) | (r << 16) | (g << 8) | b;
			this.alphaValue = a;
			settings.color.alpha = a;
		}));
	}

	/**
	 * Parses the color settings from settings.
	 */
	private void parseGuiSettings() {
		redValue = settings.color.red;
		greenValue = settings.color.green;
		blueValue = settings.color.blue;
		alphaValue = settings.color.alpha;
	}

	@Override
	public boolean keyPressed(KeyInput keyInput) {
		// Check if the key pressed matches your toggle key
		if (keyInput.key() == GLFW.GLFW_KEY_I) {
			MinecraftClient.getInstance().setScreen(null); // Close the menu
			return true;
		}

		return super.keyPressed(keyInput);
	}

}
