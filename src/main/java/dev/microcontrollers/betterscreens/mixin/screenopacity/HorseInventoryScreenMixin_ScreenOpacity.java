package dev.microcontrollers.betterscreens.mixin.screenopacity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import dev.microcontrollers.betterscreens.ScreenOpacityHook;
import dev.microcontrollers.betterscreens.mixin.GuiGraphicsInvoker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.HorseInventoryScreen;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(HorseInventoryScreen.class)
public class HorseInventoryScreenMixin_ScreenOpacity {
    @WrapOperation(method = "renderBg", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/ResourceLocation;IIFFIIII)V"))
    private void horseContainerOpacity(GuiGraphics instance, RenderPipeline renderPipeline, ResourceLocation atlasLocation, int x, int y, float uOffset, float vOffset, int uWidth, int vHeight, int textureWidth, int textureHeight, Operation<Void> original) {
        if (ScreenOpacityHook.INSTANCE.isDefault()) original.call(instance, renderPipeline, atlasLocation, x, y, uOffset, vOffset, uWidth, vHeight, textureWidth, textureHeight);
        else instance.blit(renderPipeline, atlasLocation, x, y, uOffset, vOffset, uWidth, vHeight, textureWidth, textureHeight, ScreenOpacityHook.INSTANCE.getAlpha());
    }

    @WrapOperation(method = "renderBg", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/ResourceLocation;IIIIIIII)V"))
    private void horseChestSlotOpacity(GuiGraphics instance, RenderPipeline renderPipeline, ResourceLocation sprite, int textureWidth, int textureHeight, int uPosition, int vPosition, int x, int y, int uWidth, int vHeight, Operation<Void> original) {
        if (ScreenOpacityHook.INSTANCE.isDefault()) original.call(instance, renderPipeline, sprite, textureWidth, textureHeight, uPosition, vPosition, x, y, uWidth, vHeight);
        else ((GuiGraphicsInvoker) instance).invokeBlitSprite(renderPipeline, Minecraft.getInstance().getGuiSprites().getSprite(sprite), textureWidth, textureHeight, uPosition, vPosition, x, y, uWidth, vHeight, ScreenOpacityHook.INSTANCE.getAlpha());
    }

    @WrapOperation(method = "drawSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/ResourceLocation;IIII)V"))
    private void horseItemSlotOpacity(GuiGraphics instance, RenderPipeline pipeline, ResourceLocation sprite, int x, int y, int width, int height, Operation<Void> original) {
        if (ScreenOpacityHook.INSTANCE.isDefault()) original.call(instance, pipeline, sprite, x, y, width, height);
        else instance.blitSprite(pipeline, sprite, x, y, width, height, ScreenOpacityHook.INSTANCE.getAlpha());
    }
}
