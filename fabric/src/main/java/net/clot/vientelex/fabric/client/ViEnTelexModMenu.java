package net.clot.vientelex.fabric.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.clot.vientelex.client.gui.VietnameseIMEConfigScreen;

public class ViEnTelexModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return VietnameseIMEConfigScreen::new;
    }
}
