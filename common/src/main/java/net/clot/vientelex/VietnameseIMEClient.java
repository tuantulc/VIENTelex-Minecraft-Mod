package net.clot.vientelex;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class VietnameseIMEClient {
    public static final KeyMapping TOGGLE_KEY = new KeyMapping(
            "key.vientelex.toggle",
            InputConstants.KEY_F8,
            KeyMapping.Category.MISC
    );

    public static void toggleAndNotify(Minecraft mc, boolean cycle) {
        boolean newState = VietnameseIME.toggle();

        if (mc.player != null) {
            MutableComponent message = newState
                    ? Component.literal("[VI] Vietnamese Telex: ON").withStyle(ChatFormatting.GREEN)
                    : Component.literal("[EN] Vietnamese Telex: OFF").withStyle(ChatFormatting.GRAY);
            mc.player.sendOverlayMessage(message);
        }
    }
}
