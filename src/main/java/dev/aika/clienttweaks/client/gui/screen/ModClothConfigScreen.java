package dev.aika.clienttweaks.client.gui.screen;

//? if cloth_config {
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import dev.aika.clienttweaks.boot.ModConfig;
import dev.aika.clienttweaks.boot.ModMixinPlugin;
import dev.aika.clienttweaks.config.ClientTweaksConfig;
import dev.aika.clienttweaks.config.ClientTweaksConfig.PingFpsHudConfig;
import dev.aika.clienttweaks.feature.PingFpsHud;
//? if status_icons {
/*import dev.aika.clienttweaks.config.ClientTweaksConfig.StatusIconsConfig;
*///?}
import dev.aika.clienttweaks.util.ColorUtil;
import dev.aika.clienttweaks.util.ComponentUtil;
import dev.aika.clienttweaks.util.NumberExpression;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.AbstractConfigScreen;
import me.shedaniel.clothconfig2.gui.entries.MultiElementListEntry;
import me.shedaniel.clothconfig2.gui.entries.NestedListListEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;

import java.awt.*;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class ModClothConfigScreen {
    private static final List<Runnable> savingRunnable = List.of(ModConfig::save);

    @SuppressWarnings("UnstableApiUsage")
    public static Screen create(Screen parent) {
        final var configScreenRef = new Object() {
            AbstractConfigScreen screen = null;
        };
        final Component title = Component.translatable("modmenu.nameTranslation.gizmoclienttweaks");
        final ConfigBuilder builder = ConfigBuilder.create().setTitle(title)
            .setParentScreen(parent).setSavingRunnable(() -> savingRunnable.forEach(Runnable::run));

        final ConfigEntryBuilder eb = builder.entryBuilder();
        final var conf = ModConfig.get();
        final ClientTweaksConfig defaultConfig = new ClientTweaksConfig();

        final var general = builder.getOrCreateCategory(Component.translatable("config.gizmoclienttweaks.general"));
        createMixinList(eb, builder.getOrCreateCategory(Component.translatable("config.gizmoclienttweaks.mixins")));
        general.addEntry(eb.startBooleanToggle(
                Component.translatable("config.gizmoclienttweaks.remove_realms_button"),
                conf.removeRealmsButton())
            .requireRestart()
            .setTooltip(maybeTooltip(
                Component.translatable("config.gizmoclienttweaks.remove_realms_button.@tooltip")))
            .setSaveConsumer(conf::removeRealmsButton)
            .setDefaultValue(defaultConfig.isDevEnv())
            .build());
        //? if >=1.21.11 {
        general.addEntry(eb.startBooleanToggle(
                Component.translatable("config.gizmoclienttweaks.language_code_filter"),
                conf.languageCodeFilter())
            .setTooltip(maybeTooltip(
                Component.translatable("config.gizmoclienttweaks.language_code_filter.@tooltip")))
            .setSaveConsumer(conf::languageCodeFilter)
            .setDefaultValue(defaultConfig.languageCodeFilter())
            .build());
        //?}
        general.addEntry(eb.startBooleanToggle(
                Component.translatable("config.gizmoclienttweaks.hide_fire_overlay"),
                conf.hideFireOverlay())
            .setTooltip(maybeTooltip(
                Component.translatable("config.gizmoclienttweaks.hide_fire_overlay.@tooltip")))
            .setSaveConsumer(conf::hideFireOverlay)
            .setDefaultValue(defaultConfig.hideFireOverlay())
            .build());
        general.addEntry(eb.startBooleanToggle(
                Component.translatable("config.gizmoclienttweaks.hide_insecure_warning_toast"),
                conf.hideInsecureWarningToast())
            .setTooltip(maybeTooltip(
                Component.translatable("config.gizmoclienttweaks.hide_insecure_warning_toast.@tooltip")))
            .setSaveConsumer(conf::hideInsecureWarningToast)
            .setDefaultValue(defaultConfig.hideInsecureWarningToast())
            .build());
        general.addEntry(eb.startIntSlider(
                Component.translatable("config.gizmoclienttweaks.unfocused_fps_limit"),
                conf.unfocusedFpsLimit(),
                0, 260
            )
            .setTooltip(maybeTooltip(
                Component.translatable("config.gizmoclienttweaks.unfocused_fps_limit.@tooltip")))
            .setSaveConsumer(conf::unfocusedFpsLimit)
            .setDefaultValue(defaultConfig::unfocusedFpsLimit)
            .setTextGetter(value -> value < 1
                ? Component.translatable("config.gizmoclienttweaks.@disabled")
                : Component.translatable("config.gizmoclienttweaks.@slider.fps", value))
            .build());
        general.addEntry(eb.startBooleanToggle(
                Component.translatable("config.gizmoclienttweaks.unfocused_mute"),
                conf.unfocusedMute())
            .setTooltip(maybeTooltip(
                Component.translatable("config.gizmoclienttweaks.unfocused_mute.@tooltip")))
            .setSaveConsumer(conf::unfocusedMute)
            .setDefaultValue(defaultConfig::unfocusedMute)
            .build());
        general.addEntry(eb.startIntSlider(
                Component.translatable("config.gizmoclienttweaks.client_ping_refresh"),
                conf.clientPingRefresh(),
                0, 30
            )
            .setTooltip(maybeTooltip(
                Component.translatable("config.gizmoclienttweaks.client_ping_refresh.@tooltip")))
            .setSaveConsumer(conf::clientPingRefresh)
            .setDefaultValue(defaultConfig::clientPingRefresh)
            .setTextGetter(value -> value < 1 || value >= 30
                ? Component.translatable("config.gizmoclienttweaks.@disabled")
                : Component.translatable("config.gizmoclienttweaks.@slider.seconds", value))
            .build());

        //? if status_icons {
        /*final var statusIcons = eb.startSubCategory(
                        Component.translatable("config.gizmoclienttweaks.status_icons.@category"))
                .setTooltip(maybeTooltip(
                        Component.translatable("config.gizmoclienttweaks.status_icons.@tooltip")))
                .setExpanded(true);
        final var siConf = conf.statusIcons();
        statusIcons.addLast(eb.startBooleanToggle(
                        Component.translatable("config.gizmoclienttweaks.status_icons.show_on_fire"),
                        siConf.showOnFire())
                .setTooltip(maybeTooltip(
                        Component.translatable("config.gizmoclienttweaks.status_icons.show_on_fire.@tooltip")))
                .setSaveConsumer(siConf::showOnFire)
                .setDefaultValue(defaultConfig.statusIcons()::showOnFire)
                .build());
        statusIcons.addLast(eb.startBooleanToggle(
                        Component.translatable("config.gizmoclienttweaks.status_icons.show_freezing"),
                        siConf.showFreezing())
                .setTooltip(maybeTooltip(
                        Component.translatable("config.gizmoclienttweaks.status_icons.show_freezing.@tooltip")))
                .setSaveConsumer(siConf::showFreezing)
                .setDefaultValue(defaultConfig.statusIcons()::showFreezing)
                .build());
        statusIcons.addLast(eb.startEnumSelector(
                        Component.translatable("config.gizmoclienttweaks.status_icons.anchor"),
                        StatusIconsConfig.Anchor.class,
                        siConf.anchor())
                .setTooltip(maybeTooltip(
                        Component.translatable("config.gizmoclienttweaks.status_icons.anchor.@tooltip")))
                .setSaveConsumer(siConf::anchor)
                .setDefaultValue(defaultConfig.statusIcons()::anchor)
                .build());
        statusIcons.addLast(eb.startIntField(
                        Component.translatable("config.gizmoclienttweaks.status_icons.offset_x"),
                        siConf.offsetX())
                .setMin(-500).setMax(500)
                .setTooltip(maybeTooltip(
                        Component.translatable("config.gizmoclienttweaks.status_icons.offset_x.@tooltip")))
                .setSaveConsumer(siConf::offsetX)
                .setDefaultValue(defaultConfig.statusIcons()::offsetX)
                .build());
        statusIcons.addLast(eb.startIntField(
                        Component.translatable("config.gizmoclienttweaks.status_icons.offset_y"),
                        siConf.offsetY())
                .setMin(-500).setMax(500)
                .setTooltip(maybeTooltip(
                        Component.translatable("config.gizmoclienttweaks.status_icons.offset_y.@tooltip")))
                .setSaveConsumer(siConf::offsetY)
                .setDefaultValue(defaultConfig.statusIcons()::offsetY)
                .build());
        statusIcons.addLast(eb.startIntSlider(
                        Component.translatable("config.gizmoclienttweaks.status_icons.icon_size"),
                        siConf.iconSize(), 8, 64)
                .setTooltip(maybeTooltip(
                        Component.translatable("config.gizmoclienttweaks.status_icons.icon_size.@tooltip")))
                .setSaveConsumer(siConf::iconSize)
                .setDefaultValue(defaultConfig.statusIcons()::iconSize)
                .build());
        statusIcons.addLast(eb.startIntSlider(
                        Component.translatable("config.gizmoclienttweaks.status_icons.icon_spacing"),
                        siConf.iconSpacing(), 0, 20)
                .setTooltip(maybeTooltip(
                        Component.translatable("config.gizmoclienttweaks.status_icons.icon_spacing.@tooltip")))
                .setSaveConsumer(siConf::iconSpacing)
                .setDefaultValue(defaultConfig.statusIcons()::iconSpacing)
                .build());
        general.addEntry(statusIcons.build());
        *///?}

        final String hudKey = "config.gizmoclienttweaks.ping_fps_hud.";
        if (conf.pingFpsHud() == null) conf.pingFpsHud(new PingFpsHudConfig());
        final var hud = conf.pingFpsHud();
        final var defaultHud = defaultConfig.pingFpsHud();
        final var hudCategory = eb.startSubCategory(Component.translatable(hudKey + "@category"))
            .setExpanded(true);
        hudCategory.addLast(eb.startBooleanToggle(Component.translatable(hudKey + "enabled"), hud.enabled())
            .setTooltip(maybeTooltip(Component.translatable(hudKey + "enabled.@tooltip")))
            .setSaveConsumer(hud::enabled)
            .setDefaultValue(defaultHud::enabled)
            .build());
        hudCategory.addLast(eb.startEnumSelector(Component.translatable(hudKey + "content"),
                PingFpsHudConfig.Content.class,
                hud.content() != null ? hud.content() : defaultHud.content())
            .setEnumNameProvider(value -> Component.translatable(hudKey + "content." + value.name().toLowerCase(Locale.ROOT)))
            .setSaveConsumer(hud::content)
            .setDefaultValue(defaultHud::content)
            .build());
        hudCategory.addLast(eb.startEnumSelector(Component.translatable(hudKey + "anchor"),
                PingFpsHudConfig.Anchor.class,
                hud.anchor() != null ? hud.anchor() : defaultHud.anchor())
            .setEnumNameProvider(value -> Component.translatable(hudKey + "anchor." + value.name().toLowerCase(Locale.ROOT)))
            .setSaveConsumer(hud::anchor)
            .setDefaultValue(defaultHud::anchor)
            .build());
        hudCategory.addLast(eb.startEnumSelector(Component.translatable(hudKey + "format"),
                PingFpsHudConfig.Format.class,
                hud.format() != null ? hud.format() : defaultHud.format())
            .setEnumNameProvider(value -> Component.translatable(hudKey + "format." + value.name().toLowerCase(Locale.ROOT)))
            .setSaveConsumer(hud::format)
            .setDefaultValue(defaultHud::format)
            .build());
        hudCategory.addLast(eb.startIntSlider(Component.translatable(hudKey + "scale"),
                Math.clamp(hud.scale(), PingFpsHud.MIN_SCALE, PingFpsHud.MAX_SCALE),
                PingFpsHud.MIN_SCALE, PingFpsHud.MAX_SCALE)
            .setSaveConsumer(hud::scale)
            .setDefaultValue(defaultHud::scale)
            .setTextGetter(value -> Component.literal(value + "%"))
            .build());
        general.addEntry(hudCategory.build());

        final var numericPing = eb.startSubCategory(
                Component.translatable("config.gizmoclienttweaks.numeric_ping.@category"))
            .setTooltip(maybeTooltip(
                Component.translatable("config.gizmoclienttweaks.numeric_ping.@tooltip")))
            .setExpanded(true);
        final var numericPingConfig = conf.numericPing();
        numericPing.addLast(eb.startBooleanToggle(
                Component.translatable("config.gizmoclienttweaks.numeric_ping.enabled"),
                numericPingConfig.enabled())
            .setTooltip(maybeTooltip(
                Component.translatable("config.gizmoclienttweaks.numeric_ping.enabled.@tooltip")))
            .setSaveConsumer(numericPingConfig::enabled)
            .setDefaultValue(defaultConfig.numericPing()::enabled)
            .build());
        numericPing.addLast(eb.startBooleanToggle(
                Component.translatable("config.gizmoclienttweaks.numeric_ping.show_ping_icon"),
                numericPingConfig.showPingIcon())
            .setTooltip(maybeTooltip(
                Component.translatable("config.gizmoclienttweaks.numeric_ping.show_ping_icon.@tooltip")))
            .setSaveConsumer(numericPingConfig::showPingIcon)
            .setDefaultValue(defaultConfig.numericPing()::showPingIcon)
            .build());
        numericPing.addLast(eb.startIntSlider(
                Component.translatable("config.gizmoclienttweaks.numeric_ping.slot_padding_left"),
                numericPingConfig.slotPaddingLeft(),
                0, 260
            )
            .setTooltip(maybeTooltip(
                Component.translatable("config.gizmoclienttweaks.numeric_ping.slot_padding_left.@tooltip")))
            .setSaveConsumer(numericPingConfig::slotPaddingLeft)
            .setDefaultValue(defaultConfig.numericPing()::slotPaddingLeft)
            .build());
        final var rulesEntry = new NestedListListEntry<
            ClientTweaksConfig.NumericPingConfig.Rule,
            MultiElementListEntry<ClientTweaksConfig.NumericPingConfig.Rule>
            >(
            Component.translatable("config.gizmoclienttweaks.numeric_ping.rules"),
            numericPingConfig.rules(),
            true,
            Optional::empty,
            numericPingConfig::rules, defaultConfig.numericPing()::rules,
            eb.getResetButtonKey(),
            true, true,
            (elem, ignore)
                -> createRuleListEntry(eb, Objects.requireNonNullElseGet(elem,
                () -> new ClientTweaksConfig.NumericPingConfig.Rule("*", Color.GRAY)))
        );
        rulesEntry.setTooltipSupplier(() ->
            maybeTooltip(Component.translatable("config.gizmoclienttweaks.numeric_ping.rules.@tooltip")));
        numericPing.addLast(rulesEntry);

        general.addEntry(numericPing.build());


        builder.transparentBackground();
        configScreenRef.screen = (AbstractConfigScreen) builder.build();
        return configScreenRef.screen;
    }

    @SuppressWarnings("UnstableApiUsage")
    private static MultiElementListEntry<ClientTweaksConfig.NumericPingConfig.Rule> createRuleListEntry(
        final ConfigEntryBuilder eb, final ClientTweaksConfig.NumericPingConfig.Rule rule
    ) {
        return new MultiElementListEntry<>(
            Component.translatable("config.gizmoclienttweaks.numeric_ping.rules.rule"), rule,
            Lists.newArrayList(
                eb.startStrField(
                        Component.translatable("config.gizmoclienttweaks.numeric_ping.rules.rule.range"),
                        rule.range().toString())
                    .setSaveConsumer(value -> rule.range(NumberExpression.parse(value)))
                    .setTooltip(maybeTooltip(
                        Component.translatable("config.gizmoclienttweaks.numeric_ping.rules.rule.range.@tooltip")))
                    .build(),
                eb.startColorField(
                        Component.translatable("config.gizmoclienttweaks.numeric_ping.rules.rule.color"),
                        ColorUtil.toArgb(rule.color()))
                    .setTooltip(maybeTooltip(
                        Component.translatable("config.gizmoclienttweaks.numeric_ping.rules.rule.color.@tooltip")))
                    .setAlphaMode(true)
                    .setSaveConsumer(value -> rule.color(ColorUtil.fromArgb(value)))
                    .build()
            ),
            true
        );
    }

    /**
     * Mixin groups, in the same order as the features on the General page. The key is the group id used for
     * translation keys; the values are first-level packages under {@code dev.aika.clienttweaks.mixin}, such as
     * {@code Unfocused}. A mixin whose package matches no group falls into {@link #UNGROUPED_MIXIN_GROUP}, so a newly
     * added mixin shows up instead of silently disappearing.
     * <p>
     * Only groups with at least one mixin actually available in the running environment are shown, so groups that a
     * Stonecutter version branch or the mixin plugin disables stay hidden.
     */
    private static final Map<String, List<String>> MIXIN_GROUPS = mixinGroups();

    private static final String UNGROUPED_MIXIN_GROUP = "ungrouped";

    private static Map<String, List<String>> mixinGroups() {
        final var groups = new LinkedHashMap<String, List<String>>();
        groups.put("remove_realms_button", List.of("RemoveRealmsButton"));
        groups.put("language_code_filter", List.of("LanguageCodeFilter"));
        groups.put("hide_fire_overlay", List.of("HideFireOverlay"));
        groups.put("hide_insecure_warning_toast", List.of("HideInsecureWarningToast"));
        groups.put("unfocused", List.of("Unfocused"));
        groups.put("client_ping_refresh", List.of("ClientPingRefresh"));
        groups.put("ping_fps_hud", List.of("PingFpsHud"));
        groups.put("numeric_ping", List.of("NumericPing"));
        groups.put("cloth_config_fix", List.of("ClothConfigFix"));
        return groups;
    }

    private static String mixinGroup(String mixinClassName) {
        final String relative = mixinClassName.replaceFirst("dev\\.aika\\.clienttweaks\\.mixin\\.", "");
        final String pkg = relative.contains(".") ? relative.substring(0, relative.indexOf('.')) : "";
        return MIXIN_GROUPS.entrySet().stream()
            .filter(entry -> entry.getValue().contains(pkg))
            .map(Map.Entry::getKey)
            .findFirst()
            .orElse(UNGROUPED_MIXIN_GROUP);
    }

    public static void createMixinList(final ConfigEntryBuilder eb, final ConfigCategory category) {
        final ClientTweaksConfig defaultConfig = new ClientTweaksConfig();
        final var mixins = ModMixinPlugin.getAvailableMixins();
        final var disabledMixins = Sets.newHashSet(ModConfig.get().disabledMixins());

        category.addEntry(eb.startTextDescription(
            Component.translatable("config.gizmoclienttweaks.mixins.warning")).build());

        final var grouped = new LinkedHashMap<String, List<String>>();
        MIXIN_GROUPS.keySet().forEach(group -> grouped.put(group, Lists.newArrayList()));
        grouped.put(UNGROUPED_MIXIN_GROUP, Lists.newArrayList());
        mixins.forEach(mixin -> grouped.get(mixinGroup(mixin)).add(mixin));

        grouped.forEach((group, groupMixins) -> {
            if (groupMixins.isEmpty()) return;
            final String key = "config.gizmoclienttweaks.mixins.group." + group;
            final var sub = eb.startSubCategory(Component.translatable(key))
                .setTooltip(maybeTooltip(Component.translatable(key + ".@tooltip")))
                .setExpanded(true);
            groupMixins.forEach(mixin -> sub.addLast(eb.startBooleanToggle(
                    Component.literal(mixin.substring(mixin.lastIndexOf('.') + 1)),
                    !disabledMixins.contains(mixin))
                .requireRestart()
                .setTooltip(Component.literal(mixin))
                .setYesNoTextSupplier(ComponentUtil::optionStatus)
                .setDefaultValue(!defaultConfig.disabledMixins().contains(mixin))
                .setSaveConsumer(v -> {
                    if (v) ModConfig.get().disabledMixins().remove(mixin);
                    else ModConfig.get().disabledMixins().add(mixin);
                })
                .build()));
            category.addEntry(sub.build());
        });
    }

    private static Optional<Component[]> maybeTooltip(Component... tooltip) {
        final Component[] arr = Arrays.stream(tooltip).filter(component -> {
            if (component.getContents() instanceof TranslatableContents contents)
                return Language.getInstance().has(contents.getKey());
            return true;
        }).toArray(Component[]::new);
        return arr.length < 1 ? Optional.empty() : Optional.of(arr);
    }
}
//?}
