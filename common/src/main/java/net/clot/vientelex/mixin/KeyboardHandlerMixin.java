package net.clot.vientelex.mixin;

import net.clot.vientelex.VietnameseIME;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public abstract class KeyboardHandlerMixin {

    @Shadow @Final private Minecraft minecraft;

    @Inject(method = "charTyped", at = @At("HEAD"), cancellable = true)
    private void onCharTyped(long window, CharacterEvent event, CallbackInfo ci) {
        if (window != this.minecraft.getWindow().handle()) {
            return;
        }

        Screen screen = this.minecraft.gui.screen();
        if (screen == null || this.minecraft.gui.overlay() != null) {
            return;
        }

        GuiEventListener focused = screen.getFocused();
        while (focused instanceof ContainerEventHandler container && container.getFocused() != null) {
            focused = container.getFocused();
        }

        if (focused instanceof EditBox editBox) {
            if (!editBox.canConsumeInput()) {
                return;
            }
            if (editBox.getCursorPosition() != ((EditBoxAccessor) editBox).getHighlightPos()) {
                return;
            }

            char codePoint = (char) event.codepoint();
            VietnameseIME.ProcessResult result = VietnameseIME.processInput(editBox.getValue(), editBox.getCursorPosition(), codePoint);
            if (result != null) {
                editBox.setValue(result.newText);
                editBox.setCursorPosition(result.newCursor);
                editBox.setHighlightPos(result.newCursor);
                ci.cancel();
            }
        }
    }
}
