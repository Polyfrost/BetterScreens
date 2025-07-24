package dev.microcontrollers.betterscreens;

import dev.kikugie.fletching_table.annotation.fabric.Entrypoint;
import dev.microcontrollers.betterscreens.config.BetterScreensConfig;
//? if fabric
import net.fabricmc.api.ModInitializer;
//? if neoforge {
/*import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
*///?}


//? if neoforge {
/*@Mod(value = "@MODID@", dist = Dist.CLIENT)
*///?} else {
@Entrypoint
//?}
public class BetterScreens /*? if fabric {*/ implements ModInitializer /*?}*/ {
    //? if fabric {
    @Override
    public void onInitialize() {
        BetterScreensConfig.CONFIG.load();
    }
    //?}

    //? if neoforge {
    /*public BetterScreens() {
        BetterScreensConfig.CONFIG.load();
        ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class, () -> (client, parent) -> BetterScreensConfig.configScreen(parent));
    }
	*///?}
}
