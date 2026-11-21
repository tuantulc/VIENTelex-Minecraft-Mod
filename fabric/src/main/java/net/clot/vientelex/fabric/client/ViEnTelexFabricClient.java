package net.clot.vientelex.fabric.client;

import net.clot.vientelex.VietnameseIMEClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;

@Environment(EnvType.CLIENT)
public class ViEnTelexFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        KeyBindingHelper.registerKeyBinding(VietnameseIMEClient.TOGGLE_KEY);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null) {
                while (VietnameseIMEClient.TOGGLE_KEY.consumeClick()) {
                    VietnameseIMEClient.toggleAndNotify(client, false);
                }
            }
        });
    }
}
