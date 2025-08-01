package dev.microcontrollers.betterscreens.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.platform.Window;
import dev.microcontrollers.betterscreens.config.BetterScreensConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.render.GuiRenderer;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.fog.FogRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

// TODO: MAKE WORK!!
@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Shadow
    @Final
    private GuiRenderer guiRenderer;

    @Shadow
    @Final
    private FogRenderer fogRenderer;

    @Shadow
    @Final
    private Minecraft minecraft;

    /** TODO - Remaining known issues:
     * 1. Depth issues (can be seen clearly with scoreboard or chat)
     * 2. Changing window size (such as with maximize/restore down) causes a messed up gui scale until window is resized while screen is closed (can no longer reproduce though with nothing of note changed?)
     * 3. Recipe Book Search field is not interactable
     * 4. REI issues (tooltips in wrong position & wrong size, opening a recipe resets back to vanilla scaling (uses own screen type)
     *      - It is worth noting REI has a ton of other issues with other mods as well, such as Blur+ not blurring while in a recipe screen (EMI when?!?!)
     */
    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;renderWithTooltip(Lnet/minecraft/client/gui/GuiGraphics;IIF)V"))
    private void containerScale(Screen instance, GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, Operation<Void> original) {
        if (BetterScreensConfig.CONFIG.instance().containerScale == -1 || !(instance instanceof AbstractContainerScreen<?>)) {
            original.call(instance, guiGraphics, mouseX, mouseY, partialTick);
            return;
        }

        // Render everything before the screen
        this.guiRenderer.render((this.fogRenderer.getBuffer(FogRenderer.FogMode.NONE)));

        Window window = this.minecraft.getWindow();

        window.setGuiScale(window.calculateScale(BetterScreensConfig.CONFIG.instance().containerScale, this.minecraft.isEnforceUnicode()));
        instance.resize(this.minecraft, window.getGuiScaledWidth(), window.getGuiScaledHeight());

        original.call(instance, guiGraphics, mouseX, mouseY, partialTick);

        // Start another render section after rendering the screen
        this.guiRenderer.render((this.fogRenderer.getBuffer(FogRenderer.FogMode.NONE)));

        window.setGuiScale(this.minecraft.options.guiScale().get());
    }
}
