package io.github.JumperOnJava.jjpizza.pizzamenu;

import io.github.JumperOnJava.lavajumper.common.Translation;
import io.github.JumperOnJava.lavajumper.gui.widgets.PizzaSlice;
import io.github.JumperOnJava.lavajumper.gui.widgets.PizzaWidget;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
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
    private boolean releasedOnce=false;
    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if(manager.matchesKey(keyCode,scanCode) && !releasedOnce) {
            clickAtMouse();
        }
        if(!releasedOnce)
            releasedOnce = true;
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    private void clickAtMouse() {
        var x = client.mouse.getX()/client.options.getGuiScale().getValue();
        var y = client.mouse.getY()/client.options.getGuiScale().getValue();
        this.mouseClicked(x,y,0);
    }

    public boolean keyPressed(int keyCode,int scanCode, int modifiers){
        if(!releasedOnce)
            return false;
        if(manager.matchesKey(keyCode,scanCode)){
            clickAtMouse();
            this.close();
            return true;
        }
        return super.keyPressed(keyCode,scanCode,modifiers);
    }
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
    }
}
