package dev.aika.clienttweaks.client.gui.screen;

//? if cloth_config {
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import dev.aika.clienttweaks.boot.ModConfig;
import dev.aika.clienttweaks.boot.ModMixinPlugin;
import dev.aika.clienttweaks.config.ClientTweaksConfig;
import dev.aika.clienttweaks.feature.PingTracker;
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
import net.minecraft.network.chat.Component;

import java.awt.*;
import java.util.List;
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
                .setTooltip(Component.translatable("config.gizmoclienttweaks.remove_realms_button.@tooltip"))
                .setSaveConsumer(conf::removeRealmsButton)
                .setDefaultValue(defaultConfig.isDevEnv())
                .build());
        general.addEntry(eb.startBooleanToggle(
                        Component.translatable("config.gizmoclienttweaks.better_language_filter"),
                        conf.betterLanguageFilter())
                .setTooltip(Component.translatable("config.gizmoclienttweaks.better_language_filter.@tooltip"))
                .setSaveConsumer(conf::betterLanguageFilter)
                .setDefaultValue(defaultConfig.betterLanguageFilter())
                .build());
        general.addEntry(eb.startBooleanToggle(
                        Component.translatable("config.gizmoclienttweaks.smart_fire_overlay"),
                        conf.smartFireOverlay())
                .setTooltip(Component.translatable("config.gizmoclienttweaks.smart_fire_overlay.@tooltip"))
                .setSaveConsumer(conf::smartFireOverlay)
                .setDefaultValue(defaultConfig.smartFireOverlay())
                .build());
        general.addEntry(eb.startBooleanToggle(
                        Component.translatable("config.gizmoclienttweaks.hide_unsecure_warning_toast"),
                        conf.hideUnsecureWarningToast())
                .setTooltip(Component.translatable("config.gizmoclienttweaks.hide_unsecure_warning_toast.@tooltip"))
                .setSaveConsumer(conf::hideUnsecureWarningToast)
                .setDefaultValue(defaultConfig.hideUnsecureWarningToast())
                .build());
        general.addEntry(eb.startIntSlider(
                        Component.translatable("config.gizmoclienttweaks.unfocused_fps_limit"),
                        conf.unfocusedFpsLimit(),
                        -1, 260
                )
                .setTooltip(Component.translatable("config.gizmoclienttweaks.unfocused_fps_limit.@tooltip"))
                .setSaveConsumer(conf::unfocusedFpsLimit)
                .setDefaultValue(defaultConfig::unfocusedFpsLimit)
                .setTextGetter(value -> value < 1
                        ? Component.translatable("config.gizmoclienttweaks.@disabled")
                        : Component.translatable("config.gizmoclienttweaks.unfocused_fps_limit.@fps", value))
                .build());
        general.addEntry(eb.startBooleanToggle(
                        Component.translatable("config.gizmoclienttweaks.unfocused_mute"),
                        conf.unfocusedMute())
                .setTooltip(Component.translatable("config.gizmoclienttweaks.unfocused_mute.@tooltip"))
                .setSaveConsumer(conf::unfocusedMute)
                .setDefaultValue(defaultConfig::unfocusedMute)
                .build());
        general.addEntry(eb.startIntSlider(
                        Component.translatable("config.gizmoclienttweaks.client_ping_interval"),
                        conf.clientPingInterval(),
                        -1, 30
                )
                .setTooltip(Component.translatable("config.gizmoclienttweaks.client_ping_interval.@tooltip"))
                .setSaveConsumer(value -> {
                    conf.clientPingInterval(value);
                    PingTracker.setInterval(value);
                })
                .setDefaultValue(defaultConfig::clientPingInterval)
                .setTextGetter(value -> value < 1 || value >= 30
                        ? Component.translatable("config.gizmoclienttweaks.@disabled")
                        : Component.translatable("config.gizmoclienttweaks.client_ping_interval.@second", value))
                .build());

        final var numericPing = eb.startSubCategory(
                        Component.translatable("config.gizmoclienttweaks.numeric_ping.@category"))
                .setTooltip(Component.translatable("config.gizmoclienttweaks.numeric_ping.@tooltip"))
                .setExpanded(true);
        final var numericPingConfig = conf.numericPing();
        numericPing.addLast(eb.startBooleanToggle(
                        Component.translatable("config.gizmoclienttweaks.numeric_ping.enabled"),
                        numericPingConfig.enabled())
                .setTooltip(Component.translatable("config.gizmoclienttweaks.numeric_ping.enabled.@tooltip"))
                .setSaveConsumer(numericPingConfig::enabled)
                .setDefaultValue(defaultConfig.numericPing()::enabled)
                .build());
        numericPing.addLast(eb.startBooleanToggle(
                        Component.translatable("config.gizmoclienttweaks.numeric_ping.show_ping_icon"),
                        numericPingConfig.showPingIcon())
                .setTooltip(Component.translatable("config.gizmoclienttweaks.numeric_ping.show_ping_icon.@tooltip"))
                .setSaveConsumer(numericPingConfig::showPingIcon)
                .setDefaultValue(defaultConfig.numericPing()::showPingIcon)
                .build());
        numericPing.addLast(eb.startIntSlider(
                        Component.translatable("config.gizmoclienttweaks.numeric_ping.slot_padding_left"),
                        numericPingConfig.slotPaddingLeft(),
                        0, 260
                )
                .setTooltip(Component.translatable("config.gizmoclienttweaks.numeric_ping.slot_padding_left.@tooltip"))
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
                true, false,
                (elem, ignore) -> {
                    if (elem == null) {
                        var newRule = new ClientTweaksConfig.NumericPingConfig.Rule("*", Color.GRAY);
                        return createRuleListEntry(eb, newRule);
                    }
                    return createRuleListEntry(eb, elem);
                }
        );
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
                        eb.startStrField(Component.translatable("config.gizmoclienttweaks.numeric_ping.rules.rule"),
                                        rule.range().toString())
                                .setSaveConsumer(value -> rule.range(NumberExpression.parse(value)))
                                .build(),
                        eb.startColorField(Component.translatable("config.gizmoclienttweaks.numeric_ping.rules.color"),
                                        ColorUtil.toArgb(rule.color()))
                                .setAlphaMode(true)
                                .setSaveConsumer(value -> rule.color(ColorUtil.fromArgb(value)))
                                .build()
                ),
                true
        );
    }

    public static void createMixinList(final ConfigEntryBuilder eb, final ConfigCategory category) {
        final ClientTweaksConfig defaultConfig = new ClientTweaksConfig();
        final var mixins = Sets.filter(
                ImmutableSortedSet.copyOf(ModMixinPlugin.mixins),
                mixin -> !ModMixinPlugin.disabledMixins.contains(mixin)
        );
        final var disabledMixins = Sets.newHashSet(ModConfig.get().disabledMixins());

        category.addEntry(eb.startTextDescription(
                Component.translatable("config.gizmoclienttweaks.mixins.warning")).build());
        mixins.forEach((mixin) -> category.addEntry(eb.startBooleanToggle(
                        Component.literal(mixin.replaceFirst("dev.aika.clienttweaks.mixin.", "")),
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
    }
}
//?}