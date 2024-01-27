package mod.linguardium.gentleextraction;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class GentleExtractionClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ClientPlayNetworking.registerGlobalReceiver(GentleExtraction.CONFIG_SYNC_PACKET_ID,(client, handler, buf, responseSender) -> {
			Config config = new Config(buf);
			client.execute(() -> {
				Config.INSTANCE = config;
			});
		});
	}
}