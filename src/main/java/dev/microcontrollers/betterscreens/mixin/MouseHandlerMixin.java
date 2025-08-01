package dev.microcontrollers.betterscreens.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.blaze3d.platform.Window;
import com.moulberry.mixinconstraints.annotations.IfModAbsent;
import dev.microcontrollers.betterscreens.ScreenMouseHook;
import dev.microcontrollers.betterscreens.config.BetterScreensConfig;
import kotlin.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * This code is taken from Firmament under the GPL-3.0-or-later license * <a href="https://github.com/nea89o/Firmament/blob/master/LICENSES/GPL-3.0-or-later.txt"></a>
 * Code has been changed only to adapt to this product
 */
@IfModAbsent(value = "firmament")
@Mixin(MouseHandler.class)
public class MouseHandlerMixin {
    @Shadow private double xpos;
    @Shadow private double ypos;

    @Inject(method = "grabMouse", at = @At(value = "FIELD", opcode = Opcodes.PUTFIELD, target = "Lnet/minecraft/client/MouseHandler;mouseGrabbed:Z"))
    public void onLockCursor(CallbackInfo ci) {
        ScreenMouseHook.saveCursorOriginal(this.xpos, this.ypos);
    }

    @Inject(method = "grabMouse", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/Window;getWindow()J"))
    public void onLockCursorAfter(CallbackInfo ci) {
        ScreenMouseHook.saveCursorMiddle(this.xpos, this.ypos);
    }

    @Inject(method = "releaseMouse", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/Window;getWindow()J"))
    public void onUnlockCursor(CallbackInfo ci) {
        Pair<Double, Double> cursorPosition = ScreenMouseHook.loadCursor(this.xpos, this.ypos);
        if (cursorPosition == null) return;
        this.xpos = cursorPosition.getFirst();
        this.ypos = cursorPosition.getSecond();
    }

    /**
     * These following methods are not related to Firmament
     */
    @WrapMethod(method = "getScaledXPos(Lcom/mojang/blaze3d/platform/Window;D)D")
    private static double fixMouseX(Window window, double xPos, Operation<Double> original) {
        return betterscreens$fixMouse(window, xPos, original);
    }

    @WrapMethod(method = "getScaledYPos(Lcom/mojang/blaze3d/platform/Window;D)D")
    private static double fixMouseY(Window window, double yPos, Operation<Double> original) {
        return betterscreens$fixMouse(window, yPos, original);
    }

    @Unique
    private static double betterscreens$fixMouse(Window window, double pos, Operation<Double> original) {
        if (BetterScreensConfig.CONFIG.instance().containerScale == -1 || !(Minecraft.getInstance().screen instanceof AbstractContainerScreen<?>)) {
            return original.call(window, pos);
        }

        double ret;

        window.setGuiScale(window.calculateScale(BetterScreensConfig.CONFIG.instance().containerScale, Minecraft.getInstance().isEnforceUnicode()));
        ret = original.call(window, pos);
        window.setGuiScale(Minecraft.getInstance().options.guiScale().get());

        return ret;
    }
}
