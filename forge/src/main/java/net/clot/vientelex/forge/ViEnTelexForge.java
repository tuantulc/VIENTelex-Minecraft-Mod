package net.clot.vientelex.forge;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;

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
    }
}
