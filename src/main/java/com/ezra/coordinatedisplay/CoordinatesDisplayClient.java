package com.ezra.coordinatedisplay;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import org.lwjgl.glfw.GLFW;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.*;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.*;
import net.minecraft.text.Text;
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
  private static final MinecraftClient mc = MinecraftClient.getInstance();

  public static int color = 0x40FF91F2;
  public static boolean displayOn = true;
  public static KeyBinding openKey;
  @Override
  public void onInitializeClient() {

    //Register L key to open settings
    openKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
        "key.coordinates_display.settings",
        GLFW.GLFW_KEY_L,
        "category.coordinates_display"
    ));

    // Runs upon key pressing.
    ClientTickEvents.END_CLIENT_TICK.register(client -> {
      while(openKey.wasPressed()) {
        if (mc.currentScreen instanceof CoordinatesDisplayMenu) {
          mc.setScreen(null);
        } else {
          client.setScreen(new CoordinatesDisplayMenu(Text.of("Coordinates Display Settings")));
        }
      }
    });

    // Runs upon Minecraft client exit.
    ClientLifecycleEvents.CLIENT_STOPPING.register(client -> {
      //Save Settings
      ModSettings.save();
    });

    HudLayerRegistrationCallback.EVENT.register(layerDrawer -> layerDrawer.attachLayerBefore(IdentifiedLayer.CHAT, COORDINATE_LAYER, CoordinatesDisplayClient::render));
    color = ModSettings.get().color.hex;
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
    if (!displayOn) return;

    // Settings for the UI
    double x = client.player.getX();
    double y = client.player.getY();
    double z = client.player.getZ();

    TextRenderer textRenderer = client.textRenderer;
    String toDisplay = String.format("%.1f, %.1f, %.1f", x, y, z);

    // Padding
    int padding = (int) (textRenderer.getWidth(toDisplay) * 0.05);

    // Top left corner of UI
    int xUI = 0;
    int yUI = 0;

    // Fill a square to display coordinates in
    context.fill(xUI,
        yUI,
        xUI + textRenderer.getWidth(toDisplay) + (padding*2),
        yUI + textRenderer.fontHeight + (padding*2),
        color);

    // Draw the coordinates inside the square
    context.drawText(textRenderer, toDisplay, xUI + padding, yUI + padding, 0xFFFFFF, false);
  }
}
