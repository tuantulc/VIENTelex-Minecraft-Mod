package net.clot.vientelex.fabric.client;

import net.clot.vientelex.VietnameseIMEClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;

@Environment(EnvType.CLIENT)
public class ViEnTelexFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        net.clot.vientelex.VietnameseIMEConfig.load();
        KeyMappingHelper.registerKeyMapping(VietnameseIMEClient.TOGGLE_KEY);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null) {
                while (VietnameseIMEClient.TOGGLE_KEY.consumeClick()) {
                    VietnameseIMEClient.toggleAndNotify(client, false);
                }
            }
        });
    }
}
