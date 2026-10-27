package dev.aika.clienttweaks.util;

import lombok.experimental.UtilityClass;

import java.awt.*;

@UtilityClass
@SuppressWarnings("unused")
public class ColorUtil {
    public int toRgb(Color color) {
        return color.getRGB() & 0xFFFFFF;
    }

    public int toArgb(Color color) {
        return (color.getAlpha() << 24)
                | (color.getRed() << 16)
                | (color.getGreen() << 8)
                | color.getBlue();
    }

    public int toRgba(Color color) {
        return (color.getRed() << 24)
                | (color.getGreen() << 16)
                | (color.getBlue() << 8)
                | color.getAlpha();
    }

    public Color fromHex(String rgbHex, float opacity) {
        final var rgb = Integer.parseInt(
                rgbHex.startsWith("#") ? rgbHex.substring(1) : rgbHex,
                16
        );
        final int alpha = Math.round(opacity * 255);
        return new Color(
                (rgb >> 16) & 0xFF,
                (rgb >> 8) & 0xFF,
                rgb & 0xFF,
                alpha
        );
    }

    public Color fromArgb(int argb) {
        return new Color((argb >> 16) & 0xFF, (argb >> 8) & 0xFF, argb & 0xFF, (argb >> 24) & 0xFF);
    }
}