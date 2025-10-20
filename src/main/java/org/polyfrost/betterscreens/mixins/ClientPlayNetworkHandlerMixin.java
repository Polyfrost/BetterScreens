package org.polyfrost.betterscreens.mixins;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.*;
import net.minecraft.client.network.NetHandlerPlayClient;
import org.polyfrost.betterscreens.BetterScreens;
import org.polyfrost.betterscreens.config.BetterScreensConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Mixin(NetHandlerPlayClient.class)
public class ClientPlayNetworkHandlerMixin {

    @Shadow
    private Minecraft gameController;

    @Unique
    private static final Set<Class<?>> betterscreens$preventServerFromClosing = new HashSet<>(Arrays.asList(
        //PauseScreen.class,
        GuiOptions.class,
        GuiScreenResourcePacks.class,
        GuiScreenOptionsSounds.class,
        GuiVideoSettings.class,
        GuiControls.class,
        GuiLanguage.class,
        //AccessibilityOptionsScreen.class,
        GuiChat.class
    ));

    @WrapWithCondition(method = "handleCloseWindow", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;closeScreenAndDropStack()V"))
    public boolean betterscreens$handleContainerClose(EntityPlayerSP instance) {
        if (BetterScreens.config.preventClosingScreens) {
            GuiScreen screen = this.gameController.currentScreen;
            return screen == null || !betterscreens$preventServerFromClosing.contains(screen.getClass());
        }

        return true;
    }

}
