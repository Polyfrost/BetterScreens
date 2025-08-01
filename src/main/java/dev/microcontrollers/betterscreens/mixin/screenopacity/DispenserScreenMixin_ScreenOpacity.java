package dev.microcontrollers.betterscreens.mixin.screenopacity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import dev.microcontrollers.betterscreens.ScreenOpacityHook;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.DispenserScreen;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DispenserScreen.class)
public class DispenserScreenMixin_ScreenOpacity {
    @WrapOperation(method = "renderBg", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/ResourceLocation;IIFFIIII)V"))
    private void dispenserContainerOpacity(GuiGraphics instance, RenderPipeline renderPipeline, ResourceLocation atlasLocation, int x, int y, float uOffset, float vOffset, int uWidth, int vHeight, int textureWidth, int textureHeight, Operation<Void> original) {
        if (ScreenOpacityHook.INSTANCE.isDefault()) original.call(instance, renderPipeline, atlasLocation, x, y, uOffset, vOffset, uWidth, vHeight, textureWidth, textureHeight);
        else instance.blit(renderPipeline, atlasLocation, x, y, uOffset, vOffset, uWidth, vHeight, textureWidth, textureHeight, ScreenOpacityHook.INSTANCE.getAlpha());
    }
}
