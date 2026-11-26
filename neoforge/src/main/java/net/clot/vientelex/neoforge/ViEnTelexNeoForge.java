package net.clot.vientelex.neoforge;

import net.clot.vientelex.VietnameseIMEClient;
import net.clot.vientelex.client.gui.VietnameseIMEConfigScreen;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod("vientelex")
public class ViEnTelexNeoForge {
    public static final String MOD_ID = "vientelex";

    public ViEnTelexNeoForge(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, (c, parent) -> new VietnameseIMEConfigScreen(parent));
    }

    @EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
    public static class ModBusEvents {
        @SubscribeEvent
        public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
            net.clot.vientelex.VietnameseIMEConfig.load();
            event.register(VietnameseIMEClient.TOGGLE_KEY);
        }
    }

    @EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
    public static class GameBusEvents {
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
