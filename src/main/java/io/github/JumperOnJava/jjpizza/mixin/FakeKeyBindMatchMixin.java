package io.github.JumperOnJava.jjpizza.mixin;

import io.github.JumperOnJava.jjpizza.pizzamenu.actions.actionproviders.KeybindingActionProvider;
import net.minecraft.client.option.KeyBinding;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(KeyBinding.class)
public class FakeKeyBindMatchMixin {
    @Shadow @Final private String translationKey;

    @Inject(method = "matchesKey",at = @At("HEAD"),cancellable = true)
    private void matchIfQueued(int keyCode, int scanCode, CallbackInfoReturnable<Boolean> cir){
        var id = this.translationKey;
        if(KeybindingActionProvider.awaitingMatch.contains(this.translationKey)){
            cir.setReturnValue(true);
        }
        KeybindingActionProvider.awaitingMatch.remove(id);
    }

}
