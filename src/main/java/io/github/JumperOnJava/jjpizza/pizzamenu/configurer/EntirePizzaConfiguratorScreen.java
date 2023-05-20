package io.github.JumperOnJava.jjpizza.pizzamenu.configurer;

import io.github.JumperOnJava.jjpizza.pizzamenu.PizzaManager;
import io.github.JumperOnJava.jjpizza.pizzamenu.actions.ConfiguringPizzaSlice;
import io.github.javajumper.lavajumper.datatypes.CircleSlice;
import io.github.javajumper.lavajumper.gui.SubScreen;
import io.github.javajumper.lavajumper.gui.widgets.PizzaSlice;
import io.github.javajumper.lavajumper.gui.widgets.PizzaWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

public class EntirePizzaConfiguratorScreen extends Screen {
    private final PizzaWidget.PizzaConfiguration widgetSlices,deleteSlices;
    private final SubScreen sliceConfiguratorSubScreen = new SubScreen();
    private final PizzaWidget pizza;
    private final PizzaWidget deletePizza;
    Runnable saveCallback;
    public EntirePizzaConfiguratorScreen(Runnable exitCallback) {
        super(Text.empty());
        //this.deleteSlices = deleteSlices;
        this.widgetSlices = new PizzaWidget.PizzaConfiguration(new ArrayList<>());
        this.deleteSlices = new PizzaWidget.PizzaConfiguration(new ArrayList<>());
        pizza = new PizzaWidget();
        deletePizza = new PizzaWidget();
        //this.saveCallback=exitCallback;
    }

    @Override
    public void close() {
        //saveCallback.run();
        PizzaManager.getManager().save();
        super.close();
    }

    public void setWidgetSlices(PizzaWidget.PizzaConfiguration widgetSlices) {
        this.widgetSlices.slices.clear();
        this.widgetSlices.slices.addAll(widgetSlices.slices);
        deleteSlices.slices.clear();
        for(var slice : widgetSlices.slices){
            if(slice instanceof ConfiguringPizzaSlice targetSlice){
                var delSlice = new PizzaSlice() {
                    ConfiguringPizzaSlice target;
                    @Override
                    public CircleSlice getSlice() {
                        return targetSlice.getSlice();
                    }

                    @Override
                    public Text getName() {
                        return Text.empty();
                    }

                    @Override
                    public void onLeftClick() {
                        target.remove();
                    }

                    @Override
                    public int getBackgroundColor() {
                        return 0xFFFF7057;
                    }

                    @Override
                    public Identifier getIconTexture() {
                        return null;
                    }
                };
                delSlice.target=targetSlice;
                deleteSlices.slices.add(delSlice);
            }
        }
        deletePizza.setupSlices(deleteSlices);
        pizza.setupSlices(widgetSlices);
    }
    public void init(){
        pizza.setupSize((int) (Math.min(width/4,height/2)*.8),(int) (Math.min(width/4,height/2)*.8/8),width/4,height/2);
        pizza.setupSlices(widgetSlices);
        deletePizza.setupSize((int) (Math.min(width/4,height/2)*.8/8),0,width/4,height/2);
        deletePizza.setupSlices(deleteSlices);
        addDrawableChild(pizza);
        addDrawableChild(deletePizza);
        addDrawableChild(sliceConfiguratorSubScreen.init(width/2,0,width/2,height));
    }

    public void setSliceConfiguratorScreen(Screen screen){
        sliceConfiguratorSubScreen.setScreen(screen);
    }
    public void render(MatrixStack matrixStack,int mouseX,int mouseY,float delta){
        renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, delta);
    }

}
