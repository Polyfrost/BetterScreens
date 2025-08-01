package dev.microcontrollers.betterscreens.mixin.screenopacity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import dev.microcontrollers.betterscreens.ScreenOpacityHook;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.recipebook.RecipeBookTabButton;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RecipeBookTabButton.class)
public class RecipeBookTabButtonMixin_ScreenOpacity {
    @WrapOperation(method = "renderWidget", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/ResourceLocation;IIII)V"))
    private void recipeBookContainerOpacityStart(GuiGraphics instance, RenderPipeline renderPipeline, ResourceLocation sprite, int x, int y, int width, int height, Operation<Void> original) {
        if (ScreenOpacityHook.INSTANCE.isDefault()) original.call(instance, renderPipeline, sprite, x, y, width, height);
        else instance.blitSprite(renderPipeline, sprite, x, y, width, height, ScreenOpacityHook.INSTANCE.getAlpha());
    }
}
