package dev.microcontrollers.betterscreens.mixin;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GuiGraphics.class)
public interface GuiGraphicsInvoker {
    @Invoker
    void invokeBlitSprite(RenderPipeline renderPipeline, TextureAtlasSprite textureAtlasSprite, int textureWidth, int textureHeight, int u, int v, int x, int y, int width, int height, int color);
}
