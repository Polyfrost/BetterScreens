package org.polyfrost.betterscreens.mixins;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.inventory.Container;
import org.polyfrost.betterscreens.BetterScreens;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(InventoryEffectRenderer.class)
public abstract class InventoryEffectRendererMixin extends GuiContainer {

    public InventoryEffectRendererMixin(Container inventorySlotsIn) {
        super(inventorySlotsIn);
    }

    @WrapWithCondition(
        method = "updateActivePotionEffects",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/client/renderer/InventoryEffectRenderer;guiLeft:I"
        )
    )
    private boolean betterscreens$modifyPotionEffectLogic(InventoryEffectRenderer instance, int value) {
        if (BetterScreens.config.inventoryPosition) {
            this.guiLeft = (this.width - this.xSize) / 2;
            return false;
        }
        return true;
    }
}
