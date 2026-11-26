package net.clot.vientelex.forge;

import net.clot.vientelex.client.gui.VietnameseIMEConfigScreen;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;

@Mod(ViEnTelexForge.MOD_ID)
public class ViEnTelexForge {
    public static final String MOD_ID = "vientelex";

    public ViEnTelexForge() {
        ModLoadingContext.get().registerExtensionPoint(
                IExtensionPoint.DisplayTest.class,
                () -> new IExtensionPoint.DisplayTest(
                        () -> IExtensionPoint.DisplayTest.IGNORESERVERONLY,
                        (remoteVersion, isServer) -> true
                )
        );
        ModLoadingContext.get().registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory(
                        (mc, parent) -> new VietnameseIMEConfigScreen(parent)
                )
        );
    }
}
