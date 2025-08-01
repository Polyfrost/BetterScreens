package dev.microcontrollers.betterscreens.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.microcontrollers.betterscreens.config.BetterScreensConfig;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractContainerScreen.class)
public class AbstractContainerScreenMixin<T extends AbstractContainerMenu> {
    @SuppressWarnings("rawtypes")
    @WrapOperation(method = "mouseClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/OptionInstance;get()Ljava/lang/Object;", ordinal = 0))
    private Object clickOutOfContainer(OptionInstance instance, Operation<T> original) {
        return BetterScreensConfig.CONFIG.instance().clickOutOfContainers ? true : original.call(instance);
    }
}
