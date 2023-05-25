package io.github.JumperOnJava.jjpizza.pizzamenu;

import io.github.JumperOnJava.lavajumper.common.Translation;
import io.github.JumperOnJava.lavajumper.gui.widgets.PizzaSlice;
import io.github.JumperOnJava.lavajumper.gui.widgets.PizzaWidget;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import java.util.*;
import net.minecraft.text.Text;

public class PizzaScreen extends Screen {
    private final List<? extends PizzaSlice> slices;
    private final Screen configuratorScreen;
    private final PizzaManager manager;
    public PizzaWidget pizzaWidget;
    //ImmutableSlicesList slices;
    public PizzaScreen(List<? extends PizzaSlice> slices, Screen configuratorScreen,PizzaManager manager) {
        super(Text.empty());
        this.manager = manager;
        this.slices = slices;
        this.configuratorScreen = configuratorScreen;
    }
    public void init(){
        pizzaWidget = new PizzaWidget();
        pizzaWidget.setupSize((int) (Math.min(width,height)/2f*.8f),(int) (Math.min(width,height)/2f*.8f/8),width/2,height/2);
        pizzaWidget.setupSlices(slices);
        addDrawableChild(pizzaWidget);
        if(configuratorScreen!=null)
        addDrawableChild(new ButtonWidget.Builder(Translation.get("jjpizza.screen.openconfig"), b->{
            MinecraftClient.getInstance().setScreen(configuratorScreen);
        }).position(10,10).size(60,20).build());
        if(FabricLoader.getInstance().isDevelopmentEnvironment()){
            addDrawableChild(new ButtonWidget.Builder(Text.literal("Export Translaslation"),b->Translation.generateTranlationMap()).position(10,32).width(120).build());
            /*addDrawableChild(new ButtonWidget.Builder(Text.literal("Force load"),b->{
                //PizzaManager.getManager().actions.clear();
                manager.actions=manager.load();
            }).position(10,54).width(70).build());*/

        }
    }
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if(manager.pizzaKeybind.matchesKey(keyCode,scanCode)) {
            this.close();
            return true;
        }
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
        renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, delta);
    }
}
