package org.polyfrost.betterscreens.config;

import org.polyfrost.oneconfig.api.config.v1.Config;
import org.polyfrost.oneconfig.api.config.v1.annotations.Dropdown;
import org.polyfrost.oneconfig.api.config.v1.annotations.Slider;
import org.polyfrost.oneconfig.api.config.v1.annotations.Switch;

public class BetterScreensConfig extends Config {
    public BetterScreensConfig() {
        super(org.polyfrost.betterscreens.BetterScreens.ID + ".json", org.polyfrost.betterscreens.BetterScreens.NAME, Category.QOL);

        loadFrom("patcher.toml");
    }

    // 1.8 (and maybe 1.16 if you really care) only
    @Switch(
        title = "Fixed Inventory Position",
        description = "Stop potion effects from shifting your inventory to the right."
    )
    public boolean inventoryPosition = true;

    // all
    @Switch(
        title = "Prevent Closing Screens",
        description = "Prevent the server from closing certain screens (settings, pause menu, etc). May not work on all servers."
    )
    public boolean preventClosingScreens = false;
    // public boolean dontResetCursor = false;

    @Switch(
        title = "Click Out of Containers",
        description = "Click outside a container to close the menu."
    )
    public boolean clickOutOfContainers = false;

    @Dropdown(
        title = "Inventory Scale",
        description = "Change the scale of your inventory independent of your GUI scale.",
        options = {"Off", "1 (Small)", "2 (Normal)", "3 (Large)", "4", "5 (Auto)"}
    )
    public int inventoryScale = 0;

    public int getInventoryScale() {
        return inventoryScale == 0 ? -1 : inventoryScale;
    }

    @Slider(
        title = "Container Background Opacity (%)",
        description = "Change the opacity of the dark background inside a container, or remove it completely. By default, this is 81.5%."
    )
    public float containerBackgroundOpacity = (208/255F) * 100F;

    @Slider(
        title = "Container Opacity (%)",
        description = "Change the opacity of supported containers.\nIncludes Chests & Survival Inventory."
    )
    public float containerOpacity = 100F;

    // modern only
    // public boolean removeRecipeBookShift = false;

}

