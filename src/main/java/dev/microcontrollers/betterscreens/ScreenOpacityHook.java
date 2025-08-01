package dev.microcontrollers.betterscreens;

import dev.microcontrollers.betterscreens.config.BetterScreensConfig;
import net.minecraft.util.ARGB;

public class ScreenOpacityHook {
    public boolean isDefault() {
        return BetterScreensConfig.CONFIG.instance().containerBackgroundOpacity == 100F;
    }

    public int getAlpha() {
        return ARGB.colorFromFloat(BetterScreensConfig.CONFIG.instance().containerTextureOpacity / 100F, 1F, 1F, 1F);
    }

    public static final ScreenOpacityHook INSTANCE = new ScreenOpacityHook();
}
