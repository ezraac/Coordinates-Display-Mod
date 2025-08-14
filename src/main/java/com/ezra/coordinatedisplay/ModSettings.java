package com.ezra.coordinatedisplay;

import com.google.gson.Gson;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class ModSettings {
  //Singleton Instance
  private static Settings INSTANCE;
  //Final fields
  private static final Gson gson = new Gson();
  private static final String FILE_NAME = "settings.json";
  private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve(FILE_NAME);

  /**
   * Gets the singleton instance of the settings
   *
   * @return INSTANCE the instance of settings
   */
  public static Settings get() {
    if (INSTANCE == null) {
      System.out.println(CONFIG_PATH);
      if (Files.exists(CONFIG_PATH)) {
        INSTANCE = load();
      } else {
        INSTANCE = new Settings();
        save();
      }
    }
    return INSTANCE;
  }

  /**
   * Loads the configurations from the config path
   * and stores it in a new Settings class.
   * If settings config isn't found or doesn't exist,
   * it loads the default settings stored in the settings class.
   *
   * @return a new Settings instance
   */
  public static Settings load() {
    try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
      Settings s = gson.fromJson(reader, Settings.class);
      return s;
    } catch (IOException e) {
      System.out.println("Settings doesn't exist, loading default settings...");
      return new Settings();
    }
  }

  /**
   * Saves configuration settings into a JSON file
   *
   * @return
   */
  public static void save() {
    assert INSTANCE != null;
    INSTANCE.color.hex = (INSTANCE.color.alpha << 24) | (INSTANCE.color.red << 16) | (INSTANCE.color.green << 8) | INSTANCE.color.blue;
    try(Writer writer = Files.newBufferedWriter(CONFIG_PATH, StandardOpenOption.CREATE)) {
      gson.toJson(INSTANCE, writer);
    } catch (IOException e) {
      System.err.println("Failed to save settings.");
    }
  }

  /**
   * The settings class for holding all the
   * configuration settings for the client.
   */
  public static class Settings {
    public Color color = new Color();
    public Size size = new Size();
  }

  /**
   * Static configuration class for color.
   */
  public static class Color {
    public int red = 255;
    public int green = 145;
    public int blue = 242;
    public int alpha= 255;
    public int hex = 0xFFFF91F2;
  }

  /**
   * Static configuration class for holding size.
   */
  public static class Size {
    public int padding = 20;
  }
}
