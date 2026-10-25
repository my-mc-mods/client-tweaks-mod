package dev.aika.clienttweaks.client.gui.screen;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

@SuppressWarnings({"unused", "RedundantSuppression"})
public abstract class EasyScreen extends Screen {
    private final Screen parent;

    protected EasyScreen(Screen parent, Component title) {
        super(title);
        this.parent = parent;
    }

    @SuppressWarnings("ConstantValue")
    @Override public void onClose() {
        assert this.minecraft != null;
        this.minecraft.setScreen(parent);
    }

    public void onClose(Button ignoreButton) {
        this.onClose();
    }
}