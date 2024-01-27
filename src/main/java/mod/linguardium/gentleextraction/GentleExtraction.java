package mod.linguardium.gentleextraction;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GentleExtraction implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("Gentle Extraction");
	public static final TagKey<Block> BLACKLIST = TagKey.of(RegistryKeys.BLOCK,new Identifier("gentleextraction","gentletouch_blacklist"));
	public static final TagKey<Block> WHITELIST = TagKey.of(RegistryKeys.BLOCK,new Identifier("gentleextraction","gentletouch_whitelist"));
	public static final Identifier CONFIG_SYNC_PACKET_ID = new Identifier("gentleextraction","config_sync");
	public static boolean resync = false;
	@Override
	public void onInitialize() {
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(
				new SimpleSynchronousResourceReloadListener() {
					final Identifier id = new Identifier("gentleextraction:configloader");
					@Override
					public Identifier getFabricId() { return id; }
					@Override
					public void reload(ResourceManager manager) {
						Config.loadConfig();
						resync=true;
					}
			   });
		ServerTickEvents.END_SERVER_TICK.register(server -> {
			if (resync) {
				resync = false;
				PacketByteBuf buf = Config.toPacketBuf();
				server.getPlayerManager().getPlayerList().forEach(player->
					ServerPlayNetworking.send(player,CONFIG_SYNC_PACKET_ID,PacketByteBufs.duplicate(buf))
				);
			}
		});
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			sender.sendPacket(CONFIG_SYNC_PACKET_ID,Config.toPacketBuf());
		});
	}


	public static boolean isBlackListed(BlockState state) {
		return Config.INSTANCE.useBlackList && state.isIn(BLACKLIST);
	}
	public static boolean isWhiteListed(BlockState state) {
		return !Config.INSTANCE.useWhiteList || state.isIn(GentleExtraction.WHITELIST);
	}
}