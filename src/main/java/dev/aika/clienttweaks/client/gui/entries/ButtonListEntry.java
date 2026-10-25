package dev.aika.clienttweaks.client.gui.entries;

//? if cloth_config {
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.shedaniel.clothconfig2.gui.entries.TextListEntry;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings({"OptionalUsedAsFieldOrParameterType", "unused"})
public class ButtonListEntry extends TextListEntry {
    private final Button buttonWidget;
    private final List<AbstractWidget> widgets;
    @Getter @Setter private Object value;
    @Getter private final Optional<Object> defaultValue;

    @SuppressWarnings({"UnstableApiUsage", "deprecation"})
    private ButtonListEntry(Component fieldName,
                            Component buttonText, Button.OnPress buttonOnPress,
                            Object value, Optional<Object> defaultValue) {
        super(fieldName, fieldName.copy().withColor(0xAAAAAA));
        this.buttonWidget = Button.builder(buttonText, buttonOnPress).size(150, 20).build();
        this.widgets = Lists.newArrayList(new AbstractWidget[]{this.buttonWidget});
        this.value = value;
        this.defaultValue = defaultValue;
    }

    private ButtonListEntry(Component fieldName, Component buttonText, Button.OnPress buttonOnPress) {
        this(fieldName, buttonText, buttonOnPress, null, Optional.empty());
    }

    public static Builder builder(Component fieldName, Component buttonText) {
        return new Builder(fieldName, buttonText);
    }

    public @NonNull List<? extends GuiEventListener> children() {
        return this.widgets;
    }

    public List<? extends NarratableEntry> narratables() {
        return this.widgets;
    }

    @Override //? if >=26 {
    public void extractRenderState(
            net.minecraft.client.gui.GuiGraphicsExtractor graphics, //?} else {
    /*public void render(
            net.minecraft.client.gui.GuiGraphicsExtractor graphics,*///?}
            int index, int y, int x, int entryWidth, int entryHeight,
            int mouseX, int mouseY, boolean isHovered, float delta) {
        //? if >=26 {
        super.extractRenderState(graphics, index, y, x, entryWidth, entryHeight, mouseX, mouseY, isHovered, delta); //?} else
        //super.render(graphics, index, y, x, entryWidth, entryHeight, mouseX, mouseY, isHovered, delta);

        this.buttonWidget.setY(y);
        this.buttonWidget.setX(x + entryWidth - 150);
        //? if >=26 {
        this.buttonWidget.extractRenderState(graphics, mouseX, mouseY, delta); //?} else
        //this.buttonWidget.render(graphics, mouseX, mouseY, delta);
    }

    @Accessors(chain = true)
    public static class Builder {
        private final Component fieldName;
        private final Component buttonText;
        @Setter private Button.OnPress buttonOnPress;
        private @Nullable Supplier<Optional<Component[]>> tooltipSupplier;
        @Setter private Object value = null;
        @Setter private Optional<Object> defaultValue = Optional.empty();
        @Setter private Consumer<Object> saveConsumer = null;

        public Builder(Component fieldName, Component buttonText) {
            this.fieldName = fieldName;
            this.buttonText = buttonText;
        }

        public Builder setTooltip(Component... tooltip) {
            this.tooltipSupplier = () -> Optional.of(tooltip);
            return this;
        }

        public ButtonListEntry build() {
            final ButtonListEntry entry = new ButtonListEntry(fieldName, buttonText, buttonOnPress, value, defaultValue);
            if (tooltipSupplier != null) entry.setTooltipSupplier(tooltipSupplier);
            if (saveConsumer != null) entry.saveCallback = saveConsumer;
            return entry;
        }
    }
}
//?}