package net.clot.vientelex.mixin;

import net.clot.vientelex.VietnameseIME;
import net.clot.vientelex.VietnameseIMEClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
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

    @Inject(method = "extractRenderState", at = @At("TAIL"))
    private void onExtractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (this.font == null) return;

        boolean enabled = VietnameseIME.isEnabled();
        String badge = enabled ? "[VI]" : "[EN]";
        int color = enabled ? 0xFF55FF55 : 0xFFAAAAAA;

        int textWidth = Math.max(this.font.width("[VI]"), this.font.width("[EN]"));
        int x = this.width - textWidth - 6;
        int y = this.height - this.font.lineHeight - 4;

        boolean hovered = mouseX >= x - 2 && mouseX <= x + textWidth + 2 && mouseY >= y - 2 && mouseY <= y + this.font.lineHeight;
        int bgColor = hovered ? 0xCC333333 : 0x88000000;

        guiGraphics.fill(x - 2, y - 2, x + textWidth + 2, y + this.font.lineHeight, bgColor);
        guiGraphics.text(this.font, badge, x, y, color, false);
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void onMouseClicked(MouseButtonEvent event, boolean wasHandled, CallbackInfoReturnable<Boolean> cir) {
        if (event.button() == 0 && this.font != null) {
            int textWidth = Math.max(this.font.width("[VI]"), this.font.width("[EN]"));
            int x = this.width - textWidth - 6;
            int y = this.height - this.font.lineHeight - 4;

            double mouseX = event.x();
            double mouseY = event.y();
            if (mouseX >= x - 2 && mouseX <= x + textWidth + 2 && mouseY >= y - 2 && mouseY <= y + this.font.lineHeight) {
                VietnameseIMEClient.toggleAndNotify(this.minecraft, true);
                cir.setReturnValue(true);
            }
        }
    }

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void onKeyPressed(KeyEvent event, CallbackInfoReturnable<Boolean> cir) {
        if (VietnameseIMEClient.TOGGLE_KEY.matches(event)) {
            VietnameseIMEClient.toggleAndNotify(this.minecraft, false);
            cir.setReturnValue(true);
        }
    }
}
