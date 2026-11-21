package net.clot.vientelex;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;

public class VietnameseIMEConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static File configFile;
    private static boolean loaded = false;

    public static boolean showBadge = true;
    public static float badgeOpacity = 0.8f;

    private static class ConfigData {
        boolean showBadge = true;
        float badgeOpacity = 0.8f;
    }

    public static synchronized void ensureLoaded() {
        if (!loaded) {
            load();
            loaded = true;
        }
    }

    public static synchronized void load() {
        try {
            if (configFile == null) {
                File gameDir = Minecraft.getInstance().gameDirectory;
                File configDir = new File(gameDir, "config");
                if (!configDir.exists()) {
                    configDir.mkdirs();
                }
                configFile = new File(configDir, "vientelex.json");
            }

            if (configFile.exists()) {
                try (Reader reader = new FileReader(configFile)) {
                    ConfigData data = GSON.fromJson(reader, ConfigData.class);
                    if (data != null) {
                        showBadge = data.showBadge;
                        badgeOpacity = Math.max(0.0f, Math.min(1.0f, data.badgeOpacity));
                    }
                }
            } else {
                save();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized void save() {
        try {
            if (configFile == null) {
                File gameDir = Minecraft.getInstance().gameDirectory;
                File configDir = new File(gameDir, "config");
                if (!configDir.exists()) {
                    configDir.mkdirs();
                }
                configFile = new File(configDir, "vientelex.json");
            }

            ConfigData data = new ConfigData();
            data.showBadge = showBadge;
            data.badgeOpacity = Math.max(0.0f, Math.min(1.0f, badgeOpacity));

            try (Writer writer = new FileWriter(configFile)) {
                GSON.toJson(data, writer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
