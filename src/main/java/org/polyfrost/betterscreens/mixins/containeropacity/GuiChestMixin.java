package org.polyfrost.betterscreens.mixins.containeropacity;

import net.minecraft.client.gui.inventory.GuiChest;
import org.polyfrost.betterscreens.ContainerOpacityHook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiChest.class)
public class GuiChestMixin {
    @Inject(method = "drawGuiContainerBackgroundLayer", at = @At(value = "INVOKE",
        target = "Lnet/minecraft/client/gui/inventory/GuiChest;drawTexturedModalRect(IIIIII)V", ordinal = 0))
    private void patcher$beginContainerOpacity(CallbackInfo ci) {
        ContainerOpacityHook.beginTransparency();
    }

    @Inject(method = "drawGuiContainerBackgroundLayer", at = @At(value = "INVOKE",
        target = "Lnet/minecraft/client/gui/inventory/GuiChest;drawTexturedModalRect(IIIIII)V", ordinal = 1, shift = At.Shift.AFTER))
    private void patcher$endContainerOpacity(CallbackInfo ci) {
        ContainerOpacityHook.endTransparency();
    }
}
