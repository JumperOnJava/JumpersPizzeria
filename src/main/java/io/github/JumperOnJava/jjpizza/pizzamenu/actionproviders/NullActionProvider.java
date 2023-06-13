package io.github.JumperOnJava.jjpizza.pizzamenu.actionproviders;

import io.github.JumperOnJava.jjpizza.pizzamenu.actionregistry.ConfigurableRunnable;
import io.github.JumperOnJava.jjpizza.pizzamenu.slices.ConfigurablePizzaSlice;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class NullActionProvider implements ConfigurableRunnable {
    public NullActionProvider(boolean isReal){}
    @Override
    public void setParent(ConfigurablePizzaSlice pizzaSlice) {}

    @Override
    public Screen getConfiguratorScreen() {
        return new Screen(Text.empty()) {
            public void render(DrawContext context, int mx, int my, float d){
                context.fill(0,0,width,height, 0x3F220000);
                context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer,"No Action",width/2,height/2,0xFFFFFFFF);
            }
        };
    }

    @Override
    public void run() {

    }

}
