package net.clot.vientelex.forge.client;

import net.clot.vientelex.VietnameseIMEClient;
import net.clot.vientelex.forge.ViEnTelexForge;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ViEnTelexForge.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {

    @Mod.EventBusSubscriber(modid = ViEnTelexForge.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModBusEvents {
        @SubscribeEvent
        public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
            event.register(VietnameseIMEClient.TOGGLE_KEY);
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END && Minecraft.getInstance().player != null) {
            while (VietnameseIMEClient.TOGGLE_KEY.consumeClick()) {
                VietnameseIMEClient.toggleAndNotify(Minecraft.getInstance(), false);
            }
        }
    }
}
