package net.clot.vientelex.client.gui;

import net.clot.vientelex.VietnameseIMEConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class VietnameseIMEConfigScreen extends Screen {

    private final Screen lastScreen;

    public VietnameseIMEConfigScreen(Screen lastScreen) {
        super(Component.translatable("title.vientelex.config"));
        this.lastScreen = lastScreen;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int startY = this.height / 4;

        this.addRenderableWidget(
                CycleButton.onOffBuilder(VietnameseIMEConfig.showBadge)
                        .create(centerX - 100, startY, 200, 20, Component.translatable("option.vientelex.showBadge"), (button, value) -> {
                            VietnameseIMEConfig.showBadge = value;
                        })
        );

        this.addRenderableWidget(
                new AbstractSliderButton(centerX - 100, startY + 26, 200, 20, Component.empty(), VietnameseIMEConfig.badgeOpacity) {
                    {
                        this.updateMessage();
                    }

                    @Override
                    protected void updateMessage() {
                        int percent = (int) Math.round(this.value * 100.0);
                        this.setMessage(Component.translatable("option.vientelex.badgeOpacity", percent));
                    }

                    @Override
                    protected void applyValue() {
                        VietnameseIMEConfig.badgeOpacity = (float) this.value;
                    }
                }
        );

        this.addRenderableWidget(
                Button.builder(CommonComponents.GUI_DONE, button -> this.onClose())
                        .bounds(centerX - 100, startY + 60, 200, 20)
                        .build()
        );
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.fillGradient(0, 0, this.width, this.height, -1072683200, -804253680);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 20, 0xFFFFFF);
    }

    @Override
    public void onClose() {
        VietnameseIMEConfig.save();
        if (this.minecraft != null) {
            this.minecraft.setScreen(this.lastScreen);
        }
    }
}
