package dev.aika.clienttweaks.feature;

import dev.aika.clienttweaks.boot.ModConfig;
import dev.aika.clienttweaks.util.ColorUtil;
import dev.aika.clienttweaks.util.ComponentUtil;
import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import net.minecraft.util.CommonColors;

import java.awt.*;

@UtilityClass
public final class NumericPing {
    private int lastLatency = -1;
    private int lastColor = CommonColors.WHITE;

    public void render(Minecraft mc, net.minecraft.client.gui.GuiGraphicsExtractor graphics,
                       int slotWidth, int x, int y, net.minecraft.client.multiplayer.PlayerInfo info) {
        final var conf = ModConfig.get().numericPing();

        final int latency = info.getLatency();

        final var ping = ComponentUtil.translatableWithFallbackKey(
                "feature.gizmoclienttweaks.numericping.ping",
                "multiplayer.status.ping", latency);

        if (latency != lastLatency) {
            Color color = Color.WHITE;
            for (final var rule : conf.rules()) {
                if (!rule.range().matches(latency)) continue;
                color = rule.color();
                break;
            }
            lastColor = ColorUtil.toArgb(color);
        }
        lastLatency = latency;

        graphics.text(mc.font, ping,
                x + slotWidth - mc.font.width(ping) - (conf.showPingIcon() ? 13 : 0), y,
                lastColor);
    }
}