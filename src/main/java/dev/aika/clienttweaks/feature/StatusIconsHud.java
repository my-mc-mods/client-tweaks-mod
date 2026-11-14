//? if status_icons {
/*package dev.aika.clienttweaks.feature;

import dev.aika.clienttweaks.boot.ModConfig;
import dev.aika.clienttweaks.config.ClientTweaksConfig.StatusIconsConfig.Anchor;
import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public final class StatusIconsHud {
    private static final Identifier TEXTURE_FIRE =
            Identifier.fromNamespaceAndPath("gizmoclienttweaks", "textures/hud/on_fire.png");
    private static final Identifier TEXTURE_FROST =
            Identifier.fromNamespaceAndPath("gizmoclienttweaks", "textures/hud/freezing.png");

    public void render(net.minecraft.client.gui.GuiGraphicsExtractor graphics, float partialTick) {
        final var conf = ModConfig.get().statusIcons();
        final var mc = Minecraft.getInstance();
        final var player = mc.player;
        //? if >=26.2 {
        /^if (player == null || mc.gui.hud.isHidden()) return;
        ^///?} else {
        if (player == null || mc.options.hideGui) return;
         //?}

        final List<Identifier> icons = new ArrayList<>();
        if (conf.showOnFire() && player.isOnFire()) icons.add(TEXTURE_FIRE);
        if (conf.showFreezing() && player.getTicksFrozen() > 0) icons.add(TEXTURE_FROST);
        if (icons.isEmpty()) return;

        final int size = conf.iconSize();
        final int spacing = conf.iconSpacing();
        final int screenW = mc.getWindow().getGuiScaledWidth();
        final int screenH = mc.getWindow().getGuiScaledHeight();

        final int totalH = icons.size() * size + (icons.size() - 1) * spacing;
        final int baseX = anchorX(conf.anchor(), screenW, size) + conf.offsetX();
        final int baseY = anchorY(conf.anchor(), screenH, totalH) + conf.offsetY();

        for (int i = 0; i < icons.size(); i++) {
            int y = baseY + i * (size + spacing);
            //? if >26.3 {
            /^graphics.blit(net.minecraft.client.gui.GuiPipeline.TEXTURED,
                    icons.get(i), baseX, y, 0, 0, size, size, size, size);
            ^///?} elif >=26 {
            graphics.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED,
                    icons.get(i), baseX, y, 0, 0, size, size, size, size);
            //?} else {
            /^graphics.blit(icons.get(i), baseX, y, 0, 0, size, size, size, size);
             ^///?}
        }
    }

    private int anchorX(Anchor a, int sw, int size) {
        return switch (a) {
            case TOP_LEFT, MIDDLE_LEFT, BOTTOM_LEFT -> 0;
            case TOP_CENTER, CENTER, BOTTOM_CENTER -> (sw - size) / 2;
            case TOP_RIGHT, MIDDLE_RIGHT, BOTTOM_RIGHT -> sw - size;
        };
    }

    private int anchorY(Anchor a, int sh, int totalH) {
        return switch (a) {
            case TOP_LEFT, TOP_CENTER, TOP_RIGHT -> 0;
            case MIDDLE_LEFT, CENTER, MIDDLE_RIGHT -> (sh - totalH) / 2;
            case BOTTOM_LEFT, BOTTOM_CENTER, BOTTOM_RIGHT -> sh - totalH;
        };
    }
}
*///?}
