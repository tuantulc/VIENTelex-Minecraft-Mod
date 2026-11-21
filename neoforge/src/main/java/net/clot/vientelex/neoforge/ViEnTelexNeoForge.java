package net.clot.vientelex.neoforge;

import net.clot.vientelex.VietnameseIMEClient;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

@Mod("vientelex")
public class ViEnTelexNeoForge {
    public static final String MOD_ID = "vientelex";

    @EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
    public static class ClientEvents {
        @SubscribeEvent
        public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
            net.clot.vientelex.VietnameseIMEConfig.load();
            event.register(VietnameseIMEClient.TOGGLE_KEY);
        }

        @SubscribeEvent
        public static void onClientTick(ClientTickEvent.Post event) {
            if (Minecraft.getInstance().player != null) {
                while (VietnameseIMEClient.TOGGLE_KEY.consumeClick()) {
                    VietnameseIMEClient.toggleAndNotify(Minecraft.getInstance(), false);
                }
            }
        }
    }
}
