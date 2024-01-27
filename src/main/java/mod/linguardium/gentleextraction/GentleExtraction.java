package mod.linguardium.gentleextraction;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GentleExtraction implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("gentleextraction");

	@Override
	public void onInitialize() {
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(
				new SimpleSynchronousResourceReloadListener() {
					final Identifier id = new Identifier("gentleextraction:configloader");
					@Override
					public Identifier getFabricId() { return id; }
					@Override
					public void reload(ResourceManager manager) { Config.loadConfig();	}
			   });
	}
}