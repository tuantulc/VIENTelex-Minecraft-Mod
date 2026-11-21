package net.clot.vientelex.mixin;

import net.clot.vientelex.VietnameseIME;
import net.minecraft.client.gui.components.EditBox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EditBox.class)
public abstract class EditBoxMixin {

    @Shadow public abstract String getValue();
    @Shadow public abstract int getCursorPosition();
    @Shadow public abstract void setValue(String text);
    @Shadow public abstract void setCursorPosition(int pos);
    @Shadow public abstract boolean canConsumeInput();

    @Inject(method = "charTyped", at = @At("HEAD"), cancellable = true)
    private void onCharTyped(char codePoint, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (!this.canConsumeInput()) {
            return;
        }

        VietnameseIME.ProcessResult result = VietnameseIME.processInput(this.getValue(), this.getCursorPosition(), codePoint);
        if (result != null) {
            this.setValue(result.newText);
            this.setCursorPosition(result.newCursor);
            cir.setReturnValue(true);
        }
    }
}
