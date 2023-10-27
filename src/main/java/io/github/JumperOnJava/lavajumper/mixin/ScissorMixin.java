package io.github.JumperOnJava.lavajumper.mixin;

import io.github.JumperOnJava.lavajumper.gui.GuiHelper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/**
 * fix for scissors so they work fine with Subscreen.
 */
@Mixin(DrawContext.class)
public abstract class ScissorMixin {
    @Shadow public abstract MatrixStack getMatrices();

    @ModifyArg(method = "enableScissor",at = @At(value = "INVOKE",target = "Lnet/minecraft/client/gui/DrawContext$ScissorStack;push(Lnet/minecraft/client/gui/ScreenRect;)Lnet/minecraft/client/gui/ScreenRect;"),index = 0)
    private ScreenRect enableScissor(ScreenRect rect){
        var ul = GuiHelper.transformCoords(getMatrices(),rect.getLeft(),rect.getTop());
        var dr = GuiHelper.transformCoords(getMatrices(),rect.getRight(),rect.getBottom());
        return new ScreenRect((int) ul.x, (int) ul.y,(int) (dr.x-ul.x), (int) (dr.y-ul.y));
        //return new ScreenRect(0, 0, 500, 500);
    }
}
