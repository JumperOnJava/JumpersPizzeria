package io.github.JumperOnJava.jjpizza.pizzamenu.configurer;

import io.github.JumperOnJava.lavajumper.datatypes.CircleSlice;
import io.github.JumperOnJava.lavajumper.gui.SubScreen;
import io.github.JumperOnJava.lavajumper.gui.widgets.PizzaSlice;
import io.github.JumperOnJava.lavajumper.gui.widgets.PizzaWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import java.util.*;

public class EntirePizzaConfiguratorScreen extends Screen {
    private final List<EditorPizzaSlice> widgetSlices;
    private final List<PizzaSlice> deleteSlices;
    private final SubScreen sliceConfiguratorSubScreen = new SubScreen();
    private final PizzaWidget pizza;
    private final PizzaWidget deletePizza;
    private final Runnable exitCallback;
    Runnable saveCallback;
    public EntirePizzaConfiguratorScreen(Runnable exitCallback) {
        super(Text.empty());
        //this.deleteSlices = deleteSlices;
        this.widgetSlices = new LinkedList<>();
        this.deleteSlices = new LinkedList<>();
        pizza = new PizzaWidget();
        deletePizza = new PizzaWidget();
        //this.saveCallback=exitCallback;
        this.exitCallback = exitCallback;
    }

    @Override
    public void close() {
        exitCallback.run();
        super.close();
    }

    public void setWidgetSlices(List<? extends EditorPizzaSlice> widgetSlices) {
        this.widgetSlices.clear();
        this.widgetSlices.addAll(widgetSlices);
        deleteSlices.clear();
        for(var slice : widgetSlices){
            //if(slice instanceof ConfiguringPizzaSlice targetSlice)
            {
                var delSlice = new PizzaSlice() {
                    EditorPizzaSlice target;
                    @Override
                    public CircleSlice getSlice() {
                        return slice.getSlice();
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
                delSlice.target=slice;
                deleteSlices.add(delSlice);
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
