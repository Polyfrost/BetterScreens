package org.polyfrost.betterscreens;

//#if FABRIC
//$$ import net.fabricmc.api.ModInitializer;
//#elseif FORGE

import com.mojang.brigadier.arguments.StringArgumentType;
import dev.deftu.omnicore.api.client.chat.OmniClientChat;
import dev.deftu.omnicore.api.client.commands.OmniClientCommands;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
//#endif

import org.polyfrost.betterscreens.config.BetterScreensConfig;
import org.polyfrost.oneconfig.api.ui.v1.Notifications;

//#if FORGE-LIKE
@Mod(modid = BetterScreens.ID, name = BetterScreens.NAME, version = BetterScreens.VERSION)
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

        OmniClientCommands.register(
            OmniClientCommands.literal("invscale")
                .then(OmniClientCommands.argument("scale", StringArgumentType.string())
                    .suggests((s, b) -> {
                        b.suggest("off");
                        b.suggest("none");
                        b.suggest("small");
                        b.suggest("normal");
                        b.suggest("large");
                        b.suggest("auto");
                        b.suggest("0");
                        b.suggest("1");
                        b.suggest("2");
                        b.suggest("3");
                        b.suggest("4");
                        b.suggest("5");
                        return b.buildFuture();
                    })
                    .executes(c -> {
                        String argument = StringArgumentType.getString(c, "scale");
                        if (argument.equalsIgnoreCase("help")) {
                            OmniClientChat.displayChatMessage("             &eInventory Scale");
                            OmniClientChat.displayChatMessage("&7Usage: /invscale <scaling>");
                            OmniClientChat.displayChatMessage("&7Scaling may be a number between 1-5, or");
                            OmniClientChat.displayChatMessage("&7small/normal/large/auto");
                            OmniClientChat.displayChatMessage("&7Use '/invscale off' to disable scaling.");
                            return 0;
                        }

                        if (argument.equalsIgnoreCase("off") || argument.equalsIgnoreCase("none")) {
                            Notifications.enqueue(Notifications.Type.Info, "Inventory Scale", "Disabled inventory scaling.");
                            config.inventoryScale = 0;
                            config.save();
                            return 0;
                        }

                        int scaling;
                        if (argument.equalsIgnoreCase("small")) {
                            scaling = 1;
                        } else if (argument.equalsIgnoreCase("normal")) {
                            scaling = 2;
                        } else if (argument.equalsIgnoreCase("large")) {
                            scaling = 3;
                        } else if (argument.equalsIgnoreCase("auto")) {
                            scaling = 5;
                        } else {
                            try {
                                scaling = Integer.parseInt(argument);
                            } catch (Exception e) {
                                Notifications.enqueue(Notifications.Type.Info,"Inventory Scale", "Invalid scaling identifier. Use '/invscale help' for assistance.");
                                return 0;
                            }
                        }

                        if (scaling < 1) {
                            Notifications.enqueue(Notifications.Type.Info,"Inventory Scale", "Disabled inventory scaling.");
                            config.inventoryScale = 0;
                            config.save();
                            return 0;
                        } else if (scaling > 5) {
                            Notifications.enqueue(Notifications.Type.Info,"Inventory Scale", "Invalid scaling. Must be between 1-5.");
                            return 0;
                        }

                        Notifications.enqueue(Notifications.Type.Info,"Inventory Scale", "Set inventory scaling to " + scaling);
                        config.inventoryScale = scaling;
                        config.save();
                        return 1;
                    })
                )
                .executes(c -> {
                    OmniClientChat.displayChatMessage("             &eInventory Scale");
                    OmniClientChat.displayChatMessage("&7Usage: /invscale <scaling>");
                    OmniClientChat.displayChatMessage("&7Scaling may be a number between 1-5, or");
                    OmniClientChat.displayChatMessage("&7small/normal/large/auto");
                    OmniClientChat.displayChatMessage("&7Use '/invscale off' to disable scaling.");
                    return 1;
                })
        );
    }
}
