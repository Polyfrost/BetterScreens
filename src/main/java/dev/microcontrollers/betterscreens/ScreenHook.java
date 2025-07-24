package dev.microcontrollers.betterscreens;

import com.mojang.blaze3d.platform.InputConstants;
import dev.microcontrollers.betterscreens.config.BetterScreensConfig;
import kotlin.Pair;
import net.minecraft.client.Minecraft;

/**
 * This code is taken from Firmament under the GPL-3.0-or-later license * <a href="https://github.com/nea89o/Firmament/blob/master/LICENSES/GPL-3.0-or-later.txt"></a>
 * Code has been converted to Java and condensed to only include the code necessary for this project. Code from TimeMark.kt has also been moved to this class for simplification.
 */
public class ScreenHook {
    private static final BetterScreensConfig config = BetterScreensConfig.CONFIG.instance();
    private static Pair<Double, Double> savedPositionedP1 = null;
    private static SavedPosition savedPosition = null;

    public static void saveCursorOriginal(double xPos, double yPos) {
        savedPositionedP1 = new Pair<>(xPos, yPos);
    }

    public static void saveCursorMiddle(double middleX, double middleY) {
        if (!config.dontResetCursor) return;
        if (savedPositionedP1 == null) return;
        Pair<Double, Double> cursorPos = savedPositionedP1;
        savedPosition = new SavedPosition(new Pair<>(middleX, middleY), cursorPos);
    }

    public static Pair<Double, Double> loadCursor(double middleX, double middleY) {
        if (!config.dontResetCursor) return null;
        SavedPosition lastPosition = (savedPosition != null && savedPosition.savedAt.passedTimeMillis() < 150L) ? savedPosition : null;
        savedPosition = null;

        if (lastPosition != null && Math.abs(lastPosition.middle.getFirst() - middleX) < 1 && Math.abs(lastPosition.middle.getSecond() - middleY) < 1) {
            InputConstants.grabOrReleaseMouse(Minecraft.getInstance().getWindow().getWindow(), InputConstants.CURSOR_NORMAL, lastPosition.cursor.getFirst(), lastPosition.cursor.getSecond());
            return lastPosition.cursor;
        }

        return null;
    }

    public static class SavedPosition {
        public final Pair<Double, Double> middle;
        public final Pair<Double, Double> cursor;
        public final TimeMark savedAt;

        public SavedPosition(Pair<Double, Double> middle, Pair<Double, Double> cursor) {
            this.middle = middle;
            this.cursor = cursor;
            this.savedAt = TimeMark.now();
        }
    }

    public record TimeMark(long timeMark) {
        public long passedTimeMillis() {
            return (timeMark == 0L) ? Long.MAX_VALUE : System.currentTimeMillis() - timeMark;
        }

        public static TimeMark now() {
            return new TimeMark(System.currentTimeMillis());
        }
    }
}
