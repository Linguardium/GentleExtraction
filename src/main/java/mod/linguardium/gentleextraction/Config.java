package mod.linguardium.gentleextraction;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Config {
    public static Config INSTANCE = new Config();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("gentle_extraction.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public float breakDivisor = 10.0f;

    public static void saveConfig() {
        try {
            Files.writeString(CONFIG_PATH,GSON.toJson(INSTANCE));
        } catch (IOException e) {
            GentleExtraction.LOGGER.warn("Unable to save config file: {}",CONFIG_PATH);
            GentleExtraction.LOGGER.warn("{}",e.getMessage());
        }
    }
    public static void loadConfig() {
        try {
            if (Files.exists(CONFIG_PATH)) {
                INSTANCE = GSON.fromJson(Files.readString(CONFIG_PATH, StandardCharsets.UTF_8), Config.class);
                return;
            } else {
                GentleExtraction.LOGGER.info("Config file does not exist, using default config");
            }
        } catch (IOException e) {
            GentleExtraction.LOGGER.warn("Unable to load config file: {}",CONFIG_PATH);
            GentleExtraction.LOGGER.warn("{}",e.getMessage());
        }
        INSTANCE = new Config();
        saveConfig();
    }
}