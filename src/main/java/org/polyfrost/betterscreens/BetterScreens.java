package org.polyfrost.betterscreens;

//#if FABRIC
//$$ import net.fabricmc.api.ModInitializer;
//#elseif FORGE
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
//#endif

import org.polyfrost.betterscreens.config.BetterScreensConfig;

//#if FORGE-LIKE
@Mod(modid = BetterScreens.ID, name = BetterScreens.NAME, version =BetterScreens.VERSION)
//#endif
public class BetterScreens
    //#if FABRIC
    //$$ implements ModInitializer
    //#endif
{
    public static final String ID = "@MOD_ID@";
    public static final String NAME = "@MOD_NAME@";
    public static final String VERSION = "@MOD_VERSION@";

    public static BetterScreensConfig config;

    //#if FABRIC
    //$$ @Override
    //#elseif FORGE
    @Mod.EventHandler
    //#endif
    public void onInitialize(
        //#if FORGE
        FMLInitializationEvent event
        //#endif
    ) {
        config = new BetterScreensConfig();
    }
}
