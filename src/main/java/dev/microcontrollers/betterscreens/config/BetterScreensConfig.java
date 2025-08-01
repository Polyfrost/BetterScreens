package dev.microcontrollers.betterscreens.config;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.FloatSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
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

    // TODO: reorganize
    @SerialEntry public boolean preventClosingScreens = false;
    @SerialEntry public boolean dontResetCursor = false;
    @SerialEntry public boolean clickOutOfContainers = false;
    @SerialEntry public float containerBackgroundOpacity = (208/255F) * 100F;
    @SerialEntry public float containerTextureOpacity = 100F;
    @SerialEntry public int containerScale = -1; // -1 for disabled, 0 for auto
    @SerialEntry public boolean removeRecipeBookShift = false;

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
                        .option(Option.<Boolean>createBuilder()
                                .name(Component.translatable("betterscreens.click-out-of-containers.name"))
                                .description(OptionDescription.of(Component.translatable("betterscreens.click-out-of-containers.description")))
                                .binding(defaults.clickOutOfContainers, () -> config.clickOutOfContainers, newVal -> config.clickOutOfContainers = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.<Float>createBuilder()
                                .name(Component.translatable("betterscreens.container-background-opacity.name"))
                                .description(OptionDescription.of(Component.translatable("betterscreens.container-background-opacity.description")))
                                .binding((208/255F) * 100F, () -> config.containerBackgroundOpacity, newVal -> config.containerBackgroundOpacity = newVal)
                                .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                        .formatValue(value -> Component.translatable(String.format("%,.0f", value) + "%"))
                                        .range(0F, 100F)
                                        .step(1F))
                                .build())
                        .option(Option.<Float>createBuilder()
                                .name(Component.translatable("betterscreens.container-texture-opacity.name"))
                                .description(OptionDescription.of(Component.translatable("betterscreens.container-texture-opacity.description")))
                                .binding(100F, () -> config.containerTextureOpacity, newVal -> config.containerTextureOpacity = newVal)
                                .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                        .formatValue(value -> Component.translatable(String.format("%,.0f", value) + "%"))
                                        .range(0F, 100F)
                                        .step(1F))
                                .build())
                        .option(Option.<Integer>createBuilder()
                                .name(Component.translatable("betterscreens.container-scale.name"))
                                .description(OptionDescription.of(Component.translatable("betterscreens.container-scale.dedcription")))
                                .binding(-1, () -> config.containerScale, newVal -> config.containerScale = newVal)
                                .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                        .range(-1, 5)
                                        .step(1))
                                .build())
                        .option(Option.<Boolean>createBuilder()
                                .name(Component.translatable("betterscreens.remove-recipe-book-shift.name"))
                                .description(OptionDescription.of(Component.translatable("betterscreens.remove-recipe-book-shift.description")))
                                .binding(defaults.removeRecipeBookShift, () -> config.removeRecipeBookShift, newVal -> config.removeRecipeBookShift = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .build())
        )).generateScreen(parent);
    }
}
