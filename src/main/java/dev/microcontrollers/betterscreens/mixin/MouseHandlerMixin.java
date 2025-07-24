package dev.microcontrollers.betterscreens.mixin;

import dev.microcontrollers.betterscreens.ScreenHook;
import kotlin.Pair;
import net.minecraft.client.MouseHandler;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * This code is taken from Firmament under the GPL-3.0-or-later license * <a href="https://github.com/nea89o/Firmament/blob/master/LICENSES/GPL-3.0-or-later.txt"></a>
 * Code has been changed only to adapt to this product
 */
@Debug(export = true)
@Mixin(MouseHandler.class)
public class MouseHandlerMixin {
    @Shadow private double xpos;
    @Shadow private double ypos;

    @Inject(method = "grabMouse", at = @At(value = "FIELD", opcode = Opcodes.PUTFIELD, target = "Lnet/minecraft/client/MouseHandler;mouseGrabbed:Z"))
    public void onLockCursor(CallbackInfo ci) {
        ScreenHook.saveCursorOriginal(this.xpos, this.ypos);
    }

    @Inject(method = "grabMouse", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/Window;getWindow()J"))
    public void onLockCursorAfter(CallbackInfo ci) {
        ScreenHook.saveCursorMiddle(this.xpos, this.ypos);
    }

    @Inject(method = "releaseMouse", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/Window;getWindow()J"))
    public void onUnlockCursor(CallbackInfo ci) {
        Pair<Double, Double> cursorPosition = ScreenHook.loadCursor(this.xpos, this.ypos);
        if (cursorPosition == null) return;
        this.xpos = cursorPosition.getFirst();
        this.ypos = cursorPosition.getSecond();
    }
}
