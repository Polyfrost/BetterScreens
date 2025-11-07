package org.polyfrost.betterscreens.mixins;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.settings.GameSettings;
import org.objectweb.asm.Opcodes;
import org.polyfrost.betterscreens.ResolutionHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ScaledResolution.class)
public class ScaledResolutionMixin {
    @Redirect(method = "<init>", at = @At(value = "FIELD", target = "Lnet/minecraft/client/settings/GameSettings;guiScale:I", opcode = Opcodes.GETFIELD))
    private int patcher$modifyScale(GameSettings gameSettings) {
        int scale = ResolutionHelper.getScaleOverride();
        return scale >= 0 ? scale : gameSettings.guiScale;
    }
}
