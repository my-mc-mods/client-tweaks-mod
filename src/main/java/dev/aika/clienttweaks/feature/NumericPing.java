package dev.aika.clienttweaks.feature;

import dev.aika.clienttweaks.boot.ModConfig;
import dev.aika.clienttweaks.util.ColorUtil;
import dev.aika.clienttweaks.util.ComponentUtil;
import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import net.minecraft.util.CommonColors;

import java.math.BigDecimal;

@UtilityClass
public final class NumericPing {
    public void render(Minecraft mc, net.minecraft.client.gui.GuiGraphicsExtractor graphics,
                       int slotWidth, int x, int y, net.minecraft.client.multiplayer.PlayerInfo info) {
        final var conf = ModConfig.get().numericPing();

        final int latency = info.getLatency();

        final var ping = ComponentUtil.translatableWithFallbackKey(
                "feature.gizmoclienttweaks.numericping.ping",
                "multiplayer.status.ping", latency);

        graphics.text(mc.font, ping,
                x + slotWidth - mc.font.width(ping) - (conf.showPingIcon() ? 13 : 0), y,
                colorForLatency(latency));
    }

    public int colorForLatency(int latency) {
        final var value = BigDecimal.valueOf(latency);
        for (final var rule : ModConfig.get().numericPing().rules()) {
            if (rule.range().matches(value)) return ColorUtil.toArgb(rule.color());
        }
        return CommonColors.WHITE;
    }
}
