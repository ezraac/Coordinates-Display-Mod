package com.ezra.coordinatedisplay;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.*;
import net.minecraft.client.render.*;
import net.minecraft.util.Identifier;


/**
 * Coordinates Display Client
 * Class for client code for the client
 * Used to display coordinates for the player
 * 
 * Implementation of ClientModInitializer
 */
public class CoordinatesDisplayClient implements ClientModInitializer{
	private static final Identifier COORDINATE_LAYER = Identifier.of("coordinate-display", "hud-coordinate-layer");
	
	public static int color = 0x40FF91F2;
	@Override
	public void onInitializeClient() {
		HudLayerRegistrationCallback.EVENT.register(layerDrawer -> layerDrawer.attachLayerBefore(IdentifiedLayer.CHAT, COORDINATE_LAYER, CoordinatesDisplayClient::render));
	}
	
	/**
	 * Render
	 * 
	 * Renders the coordinate display UI
	 * 
	 * @param context - Current window context
	 * @param tickCounter - tick counter
	 */
	public static void render(DrawContext context, RenderTickCounter tickCounter) {
		MinecraftClient client = MinecraftClient.getInstance();
		// null check
		if (client.player == null || client.world == null) return;
		
		// Settings for the UI
		
		double x = client.player.getX();
		double y = client.player.getY();
		double z = client.player.getZ();
		
		TextRenderer textRenderer = client.textRenderer;
		String toDisplay = String.format("%.1f, %.1f, %.1f", x, y, z);
		
		// Padding
		int xPadding = 5;
		int yPadding = 5;
		
		// Top left corner of UI
		int xUI = 0;
		int yUI = 0;
		
		// Fill a square to display coordinates in
		context.fill(xUI,
					yUI,
					xUI + textRenderer.getWidth(toDisplay) + (xPadding*2),
					yUI + textRenderer.fontHeight + (yPadding*2),
					color);
		
		// Draw the coordinates inside the square
		context.drawText(textRenderer, toDisplay, xUI + xPadding, yUI + yPadding, 0xFFFFFF, false);
	}
}
