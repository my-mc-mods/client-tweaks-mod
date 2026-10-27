package dev.aika.clienttweaks.config;

import dev.aika.clienttweaks.util.ModLoaderAccess;
import dev.aika.clienttweaks.util.NumberExpression;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.awt.*;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Getter @Setter
@Accessors(fluent = true)
public class ClientTweaksConfig {
    private transient final boolean isDevEnv = ModLoaderAccess.INSTANCE.isDevelopmentEnvironment();

    private boolean removeRealmsButton = isDevEnv;
    private boolean betterLanguageFilter = true;
    private boolean smartFireOverlay = false;
    private boolean hideUnsecureWarningToast = isDevEnv;
    private boolean unfocusedMute = isDevEnv;
    private int unfocusedFpsLimit = 10;

    private NumericPingConfig numericPing = new NumericPingConfig();

    private Set<String> disabledMixins = new LinkedHashSet<>();

    @Getter @Setter
    @Accessors(fluent = true)
    public static class NumericPingConfig {
        private boolean enabled = true;
        private boolean showPingIcon = true;
        private int slotPaddingLeft = 48;
        private List<Rule> rules = List.of(
                new Rule("> 600", Color.RED),
                new Rule("> 300", Color.ORANGE),
                new Rule(">= 0", Color.GREEN),
                new Rule("*", Color.GRAY)
        );

        @SuppressWarnings("unused")
        @Getter @Setter
        @Accessors(fluent = true)
        public static class Rule {
            private NumberExpression range;
            private Color color;

            public Rule(String range, Color color) {
                this.range = NumberExpression.parse(range);
                this.color = color;
            }

            public Rule(String range, Color color, float opacity) {
                this(range, new Color(color.getRed(), color.getGreen(), color.getBlue(), Math.round(opacity * 255)));
            }
        }
    }
}