package com.ezra.coordinatedisplay;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class CoordinatesDisplayMenu extends Screen {
	
	private double redValue = (CoordinatesDisplayClient.color >> 16) & 0xFF;
	private double greenValue = (CoordinatesDisplayClient.color >> 8) & 0xFF;
	private double blueValue = CoordinatesDisplayClient.color & 0xFF;
	private double alphaValue = (CoordinatesDisplayClient.color >> 24) & 0xFF;
	
	private ButtonWidget toggleButton;
	
	public CoordinatesDisplayMenu(Text title) {
		super(title);
	}
	
	@Override
	protected void init() {
		// Display on button
		toggleButton = ButtonWidget.builder(
				Text.of("Display: " + (CoordinatesDisplayClient.displayOn ? "on" : "off")), 
				builder -> {
					CoordinatesDisplayClient.displayOn = !CoordinatesDisplayClient.displayOn;
					toggleButton.setMessage(Text.of("Display: " + (CoordinatesDisplayClient.displayOn ? "on" : "off")));
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
		}));
		
		// Green Slider
		addDrawableChild(new ColorSlider(10, 80, 100, 20, "Green", greenValue/255d, g -> {
			int a = (CoordinatesDisplayClient.color >> 24) & 0xFF;
			int r = (CoordinatesDisplayClient.color >> 16) & 0xFF;
			int b = CoordinatesDisplayClient.color & 0xFF;
			CoordinatesDisplayClient.color = (a << 24) | (r << 16) | (g << 8) | b;
			this.greenValue = g;
		}));
		
		// Blue Slider
		addDrawableChild(new ColorSlider(10, 110, 100, 20, "Blue", blueValue/255d, b -> {
			int a = (CoordinatesDisplayClient.color >> 24) & 0xFF;
			int r = (CoordinatesDisplayClient.color >> 16) & 0xFF;
			int g = (CoordinatesDisplayClient.color >> 8) & 0xFF;
			CoordinatesDisplayClient.color = (a << 24) | (r << 16) | (g << 8) | b;
			this.blueValue = b;
		}));
		
		// Transparency Slider
		addDrawableChild(new ColorSlider(10, 140, 100, 20, "Transparancy", alphaValue/255d, a -> {
			int r = (CoordinatesDisplayClient.color >> 16) & 0xFF;
			int g = (CoordinatesDisplayClient.color >> 8) & 0xFF;
			int b = CoordinatesDisplayClient.color & 0xFF;
			
			int invertedAlpha = 255-a;
			CoordinatesDisplayClient.color = (invertedAlpha << 24) | (r << 16) | (g << 8) | b;
			this.alphaValue = a;
		}));
	}
}
