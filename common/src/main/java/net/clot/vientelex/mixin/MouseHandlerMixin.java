package net.clot.vientelex.mixin;

import net.clot.vientelex.VietnameseIMEClient;
import net.clot.vientelex.VietnameseIMEConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public abstract class MouseHandlerMixin {

    @Shadow @Final private Minecraft minecraft;
    @Shadow private double xpos;
    @Shadow private double ypos;

    @Inject(method = "onPress", at = @At("HEAD"), cancellable = true)
    private void onMousePress(long window, int button, int action, int mods, CallbackInfo ci) {
        if (button == 0 && action == 1 && this.minecraft != null && this.minecraft.screen != null) {
            VietnameseIMEConfig.ensureLoaded();
            if (!VietnameseIMEConfig.showBadge || VietnameseIMEConfig.badgeOpacity <= 0.0f) {
                return;
            }

            Screen screen = this.minecraft.screen;
            Font font = this.minecraft.font;
            if (font != null) {
                int textWidth = Math.max(font.width("[VI]"), font.width("[EN]"));
                int x = screen.width - textWidth - 6;
                int y = screen.height - font.lineHeight - 4;

                double mouseX = this.xpos * (double) this.minecraft.getWindow().getGuiScaledWidth() / (double) this.minecraft.getWindow().getScreenWidth();
                double mouseY = this.ypos * (double) this.minecraft.getWindow().getGuiScaledHeight() / (double) this.minecraft.getWindow().getScreenHeight();

                if (mouseX >= x - 2 && mouseX <= x + textWidth + 2 && mouseY >= y - 2 && mouseY <= y + font.lineHeight) {
                    VietnameseIMEClient.toggleAndNotify(this.minecraft, true);
                    ci.cancel();
                }
            }
        }
    }
}
