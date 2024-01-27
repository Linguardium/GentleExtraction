package mod.linguardium.gentleextraction;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.PacketByteBuf;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Config {
    public static Config INSTANCE = new Config();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("gentle_extraction.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public float breakDivisor;
    public boolean useBlackList;
    public boolean useWhiteList;

    public Config() {
        this(10.0f,true,false);
    }
    public Config(float divisor, boolean blacklist, boolean whitelist) {
        breakDivisor = divisor;
        useBlackList = blacklist;
        useWhiteList = whitelist;
    }
    public Config(PacketByteBuf buf) {
        this(buf.readFloat(),buf.readBoolean(),buf.readBoolean());
    }
    public static void saveConfig() {
        GentleExtraction.LOGGER.info("Saving configuraiton");
        try {
            Files.writeString(CONFIG_PATH,GSON.toJson(INSTANCE));
        } catch (IOException e) {
            GentleExtraction.LOGGER.warn("Unable to save config file: {}",CONFIG_PATH);
            GentleExtraction.LOGGER.warn("{}",e.getMessage());
        }
    }
    public static void loadConfig() {
        GentleExtraction.LOGGER.info("Loading configuration");
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
    public static PacketByteBuf toPacketBuf() {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeFloat(Config.INSTANCE.breakDivisor);
        buf.writeBoolean(Config.INSTANCE.useBlackList);
        buf.writeBoolean(Config.INSTANCE.useWhiteList);
        return buf;
    }
}
