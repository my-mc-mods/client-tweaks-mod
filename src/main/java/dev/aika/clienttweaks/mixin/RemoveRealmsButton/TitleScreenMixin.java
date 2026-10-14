package dev.aika.clienttweaks.mixin.RemoveRealmsButton;

import com.mojang.realmsclient.gui.screens.RealmsNotificationsScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.PlainTextContents;
import net.minecraft.network.chat.contents.TranslatableContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {
    @Shadow private RealmsNotificationsScreen realmsNotificationsScreen;

    protected TitleScreenMixin(Component title) {
        super(title);
    }

    @Inject(method = "realmsNotificationsEnabled", at = @At("HEAD"))
    public void realmsNotificationsEnabled(CallbackInfoReturnable<Boolean> cir) {
        this.realmsNotificationsScreen = null;
    }

    //? if >=1.21.11 {
    @Inject(method = "createNormalMenuOptions", at = @At("RETURN"), cancellable = true)
    private void createNormalMenuOptions(int topPos, int spacing, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(cir.getReturnValue() - spacing);
    } //?} else {
    /*@org.spongepowered.asm.mixin.injection.ModifyArg(method = "init", index = 0, at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screens/TitleScreen;createNormalMenuOptions(II)V"))
    private int createNormalMenuOptions(int spacing) {
        return spacing + 24;
    } *///?}

    @Inject(method = "init", at = @At("RETURN"))
    private void init(CallbackInfo ci) {
        final List<Button> buttons = this.children().stream()
                .filter(e -> e instanceof Button)
                .map(e -> (Button) e).toList();
        final Button multiplayer = buttons.stream()
                .filter(btn -> btn.getMessage().getContents() instanceof TranslatableContents contents
                        && contents.getKey().equals("menu.multiplayer"))
                .findFirst().orElse(null);
        buttons.forEach(btn -> {
            if (!btn.isActive()) return;
            if (btn.getMessage().getContents() instanceof TranslatableContents contents) {
                if (contents.getKey().equals("fml.menu.mods") && multiplayer != null) {
                    btn.setY(multiplayer.getY() + 24);
                } else if (contents.getKey().equals("menu.online")) {
                    //? if <=26.3 {
                    btn.visible = false; //?} else
                    //btn.setVisible(false);
                    btn.setFocused(false);
                }
            } else if (btn.getMessage().getContents() instanceof PlainTextContents contents
                    && contents.text().equals("Create Test World")) {
                if (multiplayer != null) btn.setY(multiplayer.getY() + 24);
            }
        });
    }
}