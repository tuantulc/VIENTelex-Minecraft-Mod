package net.clot.vientelex;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;

public class VietnameseIMEClient {
    public static final KeyMapping TOGGLE_KEY = new KeyMapping(
            "key.vientelex.toggle",
            InputConstants.KEY_F8,
            KeyMapping.Category.MISC
    );

    public static void toggleAndNotify(Minecraft mc, boolean playSound) {
        boolean enabled = VietnameseIME.toggle();
        if (mc != null) {
            if (mc.player != null) {
                mc.player.sendOverlayMessage(
                        Component.translatable(enabled ? "message.vientelex.enabled" : "message.vientelex.disabled")
                );
            }
            if (playSound && mc.getSoundManager() != null) {
                mc.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            }
        }
    }
}
