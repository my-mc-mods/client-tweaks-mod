package dev.aika.clienttweaks.client.gui.screen;

import dev.aika.clienttweaks.boot.ModConfig;
import dev.aika.clienttweaks.util.ModLoaderAccess;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineTextWidget;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

public class ClothConfigWarning extends EasyScreen {
    private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);

    public ClothConfigWarning(Screen parent) {
        super(parent, Component.translatable("modmenu.nameTranslation.gizmoclienttweaks"));
    }

    @Override public void init() {
        layout.addTitleHeader(this.title, this.font);

        final var contents = layout.addToContents(LinearLayout.vertical().spacing(12));
        contents.defaultCellSetting().alignHorizontallyCenter();
        contents.addChild(new MultiLineTextWidget(
                Component.translatable("gui.gizmoclienttweaks.cloth_config_warning.message",
                        Component.literal("gizmoclienttweaks.json")
                                .withStyle(Style.EMPTY.applyFormat(ChatFormatting.UNDERLINE))), this.font
        ).setCentered(true).setMaxWidth(width - 30));
        final var buttons = contents.addChild(LinearLayout.horizontal().spacing(8));
        buttons.addChild(Button.builder(Component.translatable("gui.gizmoclienttweaks.cloth_config_warning.load_config"), this::loadConfig).build());
        buttons.addChild(Button.builder(Component.translatable("gui.gizmoclienttweaks.cloth_config_warning.open_config_folder"), this::openConfigFolder).build());

        final var footer = layout.addToFooter(LinearLayout.horizontal());
        footer.addChild(Button.builder(CommonComponents.GUI_BACK, ignoreButton -> this.onClose()).build());

        layout.visitWidgets(this::addRenderableWidget);
        layout.arrangeElements();
    }

    private void loadConfig(Button ignoreButton) {
        ModConfig.load();
    }

    private void openConfigFolder(Button ignoreButton) {
        var path = ModLoaderAccess.INSTANCE.getConfigDir(); //? if >=26.3 {
        /*com.mojang.blaze3d.Blaze3D.openUri(path.toUri()); *///?} else {
        net.minecraft.util.Util.getPlatform().openUri(path.toUri()); //?}
    }
}