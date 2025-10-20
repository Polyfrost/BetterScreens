package org.polyfrost.betterscreens.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.settings.GameSettings;
import org.polyfrost.betterscreens.BetterScreens;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GuiContainer.class)
public class GuiContainerMixin {
    @WrapOperation(method = "mouseClicked", at = @At(value = "FIELD", target = "Lnet/minecraft/client/settings/GameSettings;touchscreen:Z", ordinal = 0))
    private boolean betterscreens$checkSetting(GameSettings instance, Operation<Boolean> original) {
        return BetterScreens.config.clickOutOfContainers || instance.touchscreen;
    }
}
