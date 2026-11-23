package net.clot.vientelex.mixin;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.realmsclient.gui.screens.RealmsNotificationsScreen;
import net.clot.vientelex.VietnameseIMEClient;
import net.clot.vientelex.VietnameseIMEConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.input.MouseButtonInfo;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public abstract class MouseHandlerMixin {

    @Shadow @Final private Minecraft minecraft;
    @Shadow public abstract double getScaledXPos(com.mojang.blaze3d.platform.Window window);
    @Shadow public abstract double getScaledYPos(com.mojang.blaze3d.platform.Window window);

    @Inject(method = "onButton", at = @At("HEAD"), cancellable = true)
    private void onMouseButton(long window, MouseButtonInfo info, int action, CallbackInfo ci) {
        if ((info.button() == InputConstants.MOUSE_BUTTON_LEFT || info.button() == 0) && action == 1 && this.minecraft != null && this.minecraft.gui != null && this.minecraft.gui.screen() != null) {
            VietnameseIMEConfig.ensureLoaded();
            if (!VietnameseIMEConfig.showBadge || VietnameseIMEConfig.badgeOpacity <= 0.0f) {
                return;
            }

            Screen screen = this.minecraft.gui.screen();
            if (screen instanceof TitleScreen || screen instanceof PauseScreen || screen instanceof RealmsNotificationsScreen) {
                return;
            }

            Font font = screen.getFont();
            if (font != null) {
                int textWidth = Math.max(font.width("[VI]"), font.width("[EN]"));
                int x = screen.width - textWidth - 6;
                int y = screen.height - font.lineHeight - 4;

                double mouseX = this.getScaledXPos(this.minecraft.getWindow());
                double mouseY = this.getScaledYPos(this.minecraft.getWindow());

                if (mouseX >= x - 2 && mouseX <= x + textWidth + 2 && mouseY >= y - 2 && mouseY <= y + font.lineHeight) {
                    VietnameseIMEClient.toggleAndNotify(this.minecraft, true);
                    ci.cancel();
                }
            }
        }
    }
}
