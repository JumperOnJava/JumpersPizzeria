package io.github.JumperOnJava.jjpizza.pizzamenu.actions.actionproviders;

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.Expose;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.ColorHelper;

import java.util.Random;

public class NullActionProvider implements ConfigurableRunnable {
    public NullActionProvider(boolean isReal){}
    @Override
    public Screen getConfigurerScreen() {
        return new Screen(Text.empty()) {
            public void render(MatrixStack matrixStack,int mx,int my,float d){
                fill(matrixStack,0,0,width,height, 0x3F220000);
                DrawableHelper.drawCenteredTextWithShadow(matrixStack, MinecraftClient.getInstance().textRenderer,"No Action",width/2,height/2,0xFFFFFFFF);
            }
        };
    }

    @Override
    public void run() {

    }

}
