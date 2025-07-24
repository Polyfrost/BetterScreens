package dev.microcontrollers.betterscreens.config;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import dev.isxander.yacl3.platform.YACLPlatform;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class BetterScreensConfig {
    public static final ConfigClassHandler<BetterScreensConfig> CONFIG = ConfigClassHandler.createBuilder(BetterScreensConfig.class)
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(YACLPlatform.getConfigDir().resolve("betterscreens.json"))
                    .build())
            .build();

    @SerialEntry public boolean preventClosingScreens = false;
    @SerialEntry public boolean dontResetCursor = false;

    public static Screen configScreen(Screen parent) {
        return YetAnotherConfigLib.create(CONFIG, ((defaults, config, builder) -> builder
                .title(Component.translatable("betterscreens.betterscreens"))
                .category(ConfigCategory.createBuilder()
                        .name(Component.translatable("betterscreens.betterscreens"))
                        .option(Option.<Boolean>createBuilder()
                                .name(Component.translatable("betterscreens.prevent-closing-screens.name"))
                                .description(OptionDescription.of(Component.translatable("betterscreens.prevent-closing-screens.description")))
                                .binding(defaults.preventClosingScreens, () -> config.preventClosingScreens, newVal -> config.preventClosingScreens = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.<Boolean>createBuilder()
                                .name(Component.translatable("betterscreens.dont-reset-cursor.name"))
                                .description(OptionDescription.of(Component.translatable("betterscreens.dont-reset-cursor.description")))
                                .binding(defaults.dontResetCursor, () -> config.dontResetCursor, newVal -> config.dontResetCursor = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .build())
        )).generateScreen(parent);
    }
}
