package org.polyfrost.betterscreens.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import org.polyfrost.betterscreens.BetterScreens;
import org.polyfrost.betterscreens.ResolutionHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(GuiScreen.class)
public class GuiScreenMixin_BackgroundOpacity {

    @Shadow
    public Minecraft mc;
    @Shadow public int width;
    @Shadow public int height;

    @Inject(method = "drawDefaultBackground", at = @At("HEAD"), cancellable = true)
    private void betterscreens$cancelRendering(CallbackInfo ci) {
        if (BetterScreens.config.containerBackgroundOpacity == 0 && this.mc.theWorld != null) {
            // Some guis (for example, the advancements gui) depend on the depth buffer having the rectangle from
            // the background in it. So, we draw a similar rectangle to only the depth buffer.
            GlStateManager.colorMask(false, false, false, false);
            Gui.drawRect(0, 0, this.width, this.height, -1);
            GlStateManager.colorMask(true, true, true, true);

            MinecraftForge.EVENT_BUS.post(new GuiScreenEvent.BackgroundDrawnEvent((GuiScreen) (Object) this));
            ci.cancel();
        }
    }

    @ModifyArgs(method = "drawWorldBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiScreen;drawGradientRect(IIIIII)V"))
    private void betterscreens$modifyColor(Args args) {
        if (BetterScreens.config.containerBackgroundOpacity != 0) {
            args.set(4, betterscreens$getOpacity(args.get(4), Math.max(BetterScreens.config.containerBackgroundOpacity / 100F - 16/255F, 0)));
            args.set(5, betterscreens$getOpacity(args.get(4), Math.max(BetterScreens.config.containerBackgroundOpacity / 100F, 0)));
        }
    }

    @Unique
    private int betterscreens$getOpacity(int color, float opacity) {
        return (int) (opacity * 255) << 24 | (color & 0xFFFFFF);
    }
}
