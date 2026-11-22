package net.clot.vientelex.mixin;

import net.clot.vientelex.VietnameseIME;
import net.clot.vientelex.VietnameseIMEClient;
import net.clot.vientelex.VietnameseIMEConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Screen.class)
public abstract class ScreenMixin {

    @Shadow protected Minecraft minecraft;
    @Shadow protected Font font;
    @Shadow public int width;
    @Shadow public int height;

    @Inject(method = "render", at = @At("TAIL"))
    private void onRender(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (this.font == null) return;
        VietnameseIMEConfig.ensureLoaded();
        if (!VietnameseIMEConfig.showBadge || VietnameseIMEConfig.badgeOpacity <= 0.0f) return;
        if ((Object) this instanceof TitleScreen || (Object) this instanceof PauseScreen) return;

        boolean enabled = VietnameseIME.isEnabled();
        String badge = enabled ? "[VI]" : "[EN]";

        float opacity = VietnameseIMEConfig.badgeOpacity;
        int textAlpha = (int) (opacity * 255.0f) & 0xFF;
        int textColor = (textAlpha << 24) | (enabled ? 0x55FF55 : 0xAAAAAA);

        int textWidth = Math.max(this.font.width("[VI]"), this.font.width("[EN]"));
        int x = this.width - textWidth - 6;
        int y = this.height - this.font.lineHeight - 4;

        boolean hovered = mouseX >= x - 2 && mouseX <= x + textWidth + 2 && mouseY >= y - 2 && mouseY <= y + this.font.lineHeight;
        int bgAlpha = (int) (opacity * (hovered ? 0xCC : 0x88)) & 0xFF;
        int bgColor = (bgAlpha << 24) | (hovered ? 0x333333 : 0x000000);

        guiGraphics.fill(x - 2, y - 2, x + textWidth + 2, y + this.font.lineHeight, bgColor);
        guiGraphics.drawString(this.font, badge, x, y, textColor, false);
    }

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void onKeyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (VietnameseIMEClient.TOGGLE_KEY.matches(keyCode, scanCode)) {
            VietnameseIMEClient.toggleAndNotify(this.minecraft, false);
            cir.setReturnValue(true);
        }
    }
}
