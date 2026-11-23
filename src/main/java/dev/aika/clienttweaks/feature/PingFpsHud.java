package dev.aika.clienttweaks.feature;

import dev.aika.clienttweaks.boot.ModConfig;
import dev.aika.clienttweaks.config.ClientTweaksConfig.PingFpsHudConfig;
import dev.aika.clienttweaks.util.ColorUtil;
import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.CommonColors;

import java.awt.Color;

@UtilityClass
public class PingFpsHud {
    private final String TEXT_KEY = "feature.gizmoclienttweaks.ping_fps_hud.";
    private final Component MISSING_PING = Component.literal("--");
    private final String SEPARATOR = " | ";
    private final int VALUE_DIGITS = 4;
    private final int MARGIN = 4;
    private final int CHAT_INPUT_INSET = 16;
    public final int MIN_SCALE = 50;
    public final int MAX_SCALE = 200;

    public void render(net.minecraft.client.gui.GuiGraphicsExtractor graphics, boolean fromChat) {
        final var config = ModConfig.get().pingFpsHud();
        final var minecraft = Minecraft.getInstance();
        final var player = minecraft.player;
        if (config == null || !config.enabled() || minecraft.level == null || player == null) return;

        //? if <26.2 {
        final var screen = minecraft.screen;
        if (minecraft.options.hideGui) return;
        //?} else {
        /*final var screen = minecraft.gui.screen();
        if (minecraft.gui.hud.isHidden()) return;
        *///?}
        final var anchor = config.anchor() != null ? config.anchor() : PingFpsHudConfig.Anchor.TOP_LEFT;
        final var content = config.content() != null ? config.content() : PingFpsHudConfig.Content.PING_AND_FPS;
        final var format = config.format() != null ? config.format() : PingFpsHudConfig.Format.COMPACT;
        final boolean top = anchor == PingFpsHudConfig.Anchor.TOP_LEFT
                || anchor == PingFpsHudConfig.Anchor.TOP_RIGHT;
        //? if <1.21.11 {
        /*if (top && minecraft.getDebugOverlay().showDebugScreen()) return;
        *///?} else {
        if (top && minecraft.debugEntries.isOverlayVisible()) return;
        //?}
        if (fromChat ? !(screen instanceof ChatScreen) : screen != null) return;

        final boolean labeled = format == PingFpsHudConfig.Format.LABELED;
        final boolean showFps = content != PingFpsHudConfig.Content.PING_ONLY;
        final boolean showPing = content != PingFpsHudConfig.Content.FPS_ONLY;
        final var font = minecraft.font;
        final int numberWidth = numberSlotWidth(font);
        final int fpsWidth = showFps ? numberWidth + font.width(fpsText("", labeled)) : 0;
        final int pingWidth = showPing ? Math.max(
                numberWidth + font.width(pingText(Component.translatable(TEXT_KEY + "ping.compact", ""), labeled)),
                font.width(pingText(MISSING_PING, labeled))) : 0;
        final int separatorWidth = showFps && showPing ? font.width(SEPARATOR) : 0;

        final boolean alignRight = switch (anchor) {
            case TOP_LEFT, BOTTOM_LEFT -> false;
            case TOP_RIGHT, BOTTOM_RIGHT -> true;
        };
        // The config file is user-editable, so keep the scale in the range the slider offers.
        final float scale = Math.clamp(config.scale(), MIN_SCALE, MAX_SCALE) / 100f;
        final float originX = alignRight
                ? graphics.guiWidth() - MARGIN - (fpsWidth + separatorWidth + pingWidth) * scale
                : MARGIN;
        // Leave the chat input bar clear when drawing after the chat screen.
        final float originY = top ? MARGIN
                : graphics.guiHeight() - MARGIN - font.lineHeight * scale - (fromChat ? CHAT_INPUT_INSET : 0);

        //? if >=1.21.11
        graphics.nextStratum();
        final var pose = graphics.pose();
        //? if <1.21.11 {
        /*pose.pushPose();
        pose.translate(originX, originY, 0f);
        pose.scale(scale, scale, 1f);
        *///?} else {
        pose.pushMatrix();
        pose.translate(originX, originY);
        pose.scale(scale, scale);
        //?}

        try {
            int x = 0;
            if (showFps) {
                final int fps = minecraft.getFps();
                final var text = fpsText(Integer.toString(fps), labeled);
                graphics.text(font, text, alignRight ? x + fpsWidth - font.width(text) : x, 0, fpsColor(fps), true);
                x += fpsWidth;
            }
            if (showFps && showPing) {
                graphics.text(font, SEPARATOR, x, 0, CommonColors.WHITE, true);
                x += separatorWidth;
            }
            if (showPing) {
                final int latency = getLatency(minecraft, player);
                final var value = latency < 0 ? MISSING_PING : Component.translatable(TEXT_KEY + "ping.compact", latency);
                final var text = pingText(value, labeled);
                graphics.text(font, text, alignRight ? x + pingWidth - font.width(text) : x, 0,
                        NumericPing.colorForLatency(latency), true);
            }
        } finally {
            //? if <1.21.11 {
            /*pose.popPose();
            *///?} else {
            pose.popMatrix();
            //?}
        }
    }

    private int numberSlotWidth(Font font) {
        // Measure the widest digit to account for proportional resource-pack fonts.
        int widestDigit = 0;
        for (char digit = '0'; digit <= '9'; digit++) {
            widestDigit = Math.max(widestDigit, font.width(Character.toString(digit)));
        }
        return widestDigit * VALUE_DIGITS;
    }

    private Component pingText(Component value, boolean labeled) {
        return labeled ? Component.translatable(TEXT_KEY + "ping.labeled", value) : value;
    }

    private Component fpsText(String value, boolean labeled) {
        return Component.translatable(TEXT_KEY + (labeled ? "fps.labeled" : "fps.compact"), value);
    }

    private int fpsColor(int fps) {
        if (fps >= 60) return ColorUtil.toArgb(Color.GREEN);
        if (fps >= 30) return ColorUtil.toArgb(Color.ORANGE);
        return ColorUtil.toArgb(Color.RED);
    }

    private int getLatency(Minecraft minecraft, LocalPlayer player) {
        final var listener = minecraft.getConnection();
        final int activeLatency = ClientPingRefresh.getLatencyMs(listener);
        if (activeLatency >= 0) return activeLatency;

        final var info = listener == null ? null : listener.getPlayerInfo(player.getUUID());
        return info == null ? -1 : info.getLatency();
    }
}
