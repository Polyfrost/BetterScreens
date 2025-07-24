package dev.microcontrollers.betterscreens.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import dev.microcontrollers.betterscreens.config.BetterScreensConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.options.*;
import net.minecraft.client.gui.screens.options.controls.ControlsScreen;
import net.minecraft.client.gui.screens.packs.PackSelectionScreen;
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.CommonListenerCookie;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundContainerClosePacket;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

/**
 * This code is taken from MoulberrysTweaks under the MIT license * <a href="https://github.com/Moulberry/MoulberrysTweaks/blob/master/LICENSE"></a>
 */
@Mixin(ClientPacketListener.class)
public abstract class ClientPacketHandlerMixin extends ClientCommonPacketListenerImpl {
    protected ClientPacketHandlerMixin(Minecraft minecraft, Connection connection, CommonListenerCookie commonListenerCookie) {
        super(minecraft, connection, commonListenerCookie);
    }

    @Unique
    private static final Set<Class<?>> betterscreens$preventServerFromClosing = Set.of(
            PauseScreen.class,
            OptionsScreen.class,
            PackSelectionScreen.class,
            SoundOptionsScreen.class,
            VideoSettingsScreen.class,
            ControlsScreen.class,
            LanguageSelectScreen.class,
            AccessibilityOptionsScreen.class,
            ChatScreen.class
    );

    @WrapWithCondition(method = "handleContainerClose", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;clientSideCloseContainer()V"))
    public boolean handleContainerClose(LocalPlayer instance, ClientboundContainerClosePacket packet) {
        if (BetterScreensConfig.CONFIG.instance().preventClosingScreens) {
            Screen currentScreen = this.minecraft.screen;
            if (currentScreen != null && betterscreens$preventServerFromClosing.contains(currentScreen.getClass())) {
                return false;
            }
        }
        return true;
    }

    @Inject(method = "handleOpenScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/PacketUtils;ensureRunningOnSameThread(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;Lnet/minecraft/util/thread/BlockableEventLoop;)V", shift = At.Shift.AFTER))
    public void handleOpenScreenHead(ClientboundOpenScreenPacket packet, CallbackInfo ci, @Share("oldScreenRef") LocalRef<Screen> oldScreenRef) {
        oldScreenRef.set(this.minecraft.screen);
    }

    @Inject(method = "handleOpenScreen", at = @At("RETURN"))
    public void handleOpenScreenReturn(ClientboundOpenScreenPacket packet, CallbackInfo ci, @Share("oldScreenRef") LocalRef<Screen> oldScreenRef) {
        if (BetterScreensConfig.CONFIG.instance().preventClosingScreens) {
            Screen oldScreen = oldScreenRef.get();
            Screen currentScreen = this.minecraft.screen;
            if (oldScreen == currentScreen) {
                return;
            }
            if (oldScreen != null && betterscreens$preventServerFromClosing.contains(oldScreen.getClass())) {
                if (currentScreen instanceof AbstractContainerScreen<?> containerScreen && containerScreen.shouldCloseOnEsc()) {
                    containerScreen.onClose();
                    currentScreen = this.minecraft.screen;
                    if (currentScreen == null) {
                        Minecraft.getInstance().setScreen(oldScreen);
                    }
                }
            }
        }
    }
}
