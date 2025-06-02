package com.ezra.coordinatedisplay;

import java.util.function.IntConsumer;

import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;


/**
 * ColorSlider
 * 
 * Custom slider class extending the abstract class SliderWidget
 * When updated, it uses a consumer to accept the value and update the static color stored in CoordinatesDisplayClient
 */
public class ColorSlider extends SliderWidget {
	
	private final String label;
	private final IntConsumer onValueChanged;

	public ColorSlider(int x, int y, int width, int height, String label, double value, IntConsumer onValueChanged) {
		super(x, y, width, height, Text.of(label), value);
		this.label = label;
		this.onValueChanged = onValueChanged;
		updateMessage();
	}

	/**
	 * applyValue
	 * 
	 * Accepts the value of the slider to update the color
	 */
	@Override
	protected void applyValue() {
		onValueChanged.accept((int)(value * 255));
	}

	/**
	 * updateMessage
	 * 
	 * Updates the value shown in the menu screen
	 */
	@Override
	protected void updateMessage() {
		setMessage(Text.of(label + ": " + (int)(value * 255)));
	}
	
}
