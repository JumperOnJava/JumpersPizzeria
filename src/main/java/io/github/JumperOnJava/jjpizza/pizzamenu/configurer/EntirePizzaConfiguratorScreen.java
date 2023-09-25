package io.github.JumperOnJava.jjpizza.pizzamenu.configurer;

import io.github.JumperOnJava.jjpizza.pizzamenu.slices.ConfigurablePizzaSlice;
import io.github.JumperOnJava.jjpizza.pizzamenu.slices.runnable.RunnableSlice;
import io.github.JumperOnJava.lavajumper.common.Translation;
import io.github.JumperOnJava.lavajumper.datatypes.CircleSlice;
import io.github.JumperOnJava.lavajumper.gui.AskScreen;
import io.github.JumperOnJava.lavajumper.gui.widgets.PizzaSlice;
import io.github.JumperOnJava.lavajumper.gui.widgets.PizzaWidget;
import io.github.JumperOnJava.lavajumper.gui.widgets.SubScreen;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import java.util.*;
import java.util.function.Consumer;

import static io.github.JumperOnJava.jjpizza.pizzamenu.slices.runnable.actionregistry.ActionTypeRegistry.gap;

public class EntirePizzaConfiguratorScreen extends AskScreen<List<ConfigurablePizzaSlice>> implements ConfigActionApplier  {
    private final List<EditorPizzaSlice> widgetSlices;
    private final List<PizzaSlice> deleteSlices;
    private final SubScreen sliceConfiguratorSubScreen = new SubScreen();
    private final PizzaWidget pizza;
    private final PizzaWidget deletePizza;
    private final LinkedList<ConfigurablePizzaSlice> editSlices;

    public void rebuildSlices() {
        var l = new LinkedList<EditorPizzaSlice>();
        for(var slice : editSlices) {
            l.add(new EditorPizzaSlice(slice, this));
        }
        this.setWidgetSlices(l);
    }
    public EntirePizzaConfiguratorScreen(List<ConfigurablePizzaSlice> input,Consumer<List<ConfigurablePizzaSlice>> onSuccess, Runnable onFail) {
        super(onSuccess, onFail);
        this.editSlices = new LinkedList<>();
        for(var t : input){
            editSlices.add(t.copy());
        }
        this.widgetSlices = new LinkedList<>();
        this.deleteSlices = new LinkedList<>();
        pizza = new PizzaWidget();
        deletePizza = new PizzaWidget();
    }
    Runnable saveCallback;

    @Override
    public void close() {
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
        var radius = (Math.min(width/4,height/2)*.8);
        pizza.setupSize((int) radius, (int) (radius/4),width/4,height/2);
        pizza.setupSlices(widgetSlices);
        //deletePizza.setupSize((int) (radius/16*17), (int) radius,width/4,height/2);
        deletePizza.setupSize((int) (radius/4), (int) radius/8,width/4,height/2);
        deletePizza.setupSlices(deleteSlices);
        addDrawableChild(pizza);
        addDrawableChild(deletePizza);
        addDrawableChild(sliceConfiguratorSubScreen.init(width/2,0,width/2,height));
        var hwidth = width/2;
        addDrawableChild(new ButtonWidget.Builder(Translation.get("jjpizza.edit.save"),b->this.success(editSlices)).dimensions(gap/2,height-20-gap/2,hwidth/2-gap,20).build());
        addDrawableChild(new ButtonWidget.Builder(Translation.get("jjpizza.edit.cancel"),b->this.fail()).dimensions(gap/2+hwidth/2,height-20-gap/2,hwidth/2-gap,20).build());
        rebuildSlices();
    }

    public void setSliceConfiguratorScreen(Screen screen){
        sliceConfiguratorSubScreen.setScreen(screen);
    }
    public void render(DrawContext context, int mouseX, int mouseY, float delta){
        renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
    }
    public void setSliceConfigScreen(Screen screen) {
        this.setSliceConfiguratorScreen(screen);
    }
    public void splitSlice(ConfigurablePizzaSlice clickedSlice) {
        CircleSlice circleSlice = clickedSlice.getSlice();
        editSlices.remove(clickedSlice);
        editSlices.add(new RunnableSlice("Empty action", new CircleSlice(circleSlice.startAngle, circleSlice.getMidAngle()),clickedSlice.getManager()));
        editSlices.add(new RunnableSlice("Empty action", new CircleSlice(circleSlice.getMidAngle(), circleSlice.endAngle),clickedSlice.getManager()));
        rebuildSlices();
    }

    @Override
    public void initClose() {
        if(this.client.currentScreen==this)
            this.client.setScreen(null);
        else
            super.initClose();
    }

    @Override
    public void removeSlice(ConfigurablePizzaSlice targetAction) {
        editSlices.remove(editSlices.indexOf(targetAction));
        if(editSlices.size()==0){
            editSlices.add(new RunnableSlice("Empty action", CircleSlice.percent(0.125f,0.375f),targetAction.getManager()));
        }
        rebuildSlices();
    }

}
