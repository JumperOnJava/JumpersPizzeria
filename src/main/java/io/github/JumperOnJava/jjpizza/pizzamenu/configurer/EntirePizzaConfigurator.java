package io.github.JumperOnJava.jjpizza.pizzamenu.configurer;

import io.github.JumperOnJava.jjpizza.pizzamenu.slices.ConfigurablePizzaSlice;
import io.github.JumperOnJava.jjpizza.pizzamenu.slices.runnable.RunnableSlice;
import io.github.JumperOnJava.lavajumper.datatypes.CircleSlice;
import net.minecraft.client.gui.screen.Screen;

import java.util.*;
import java.util.function.Consumer;

public class EntirePizzaConfigurator implements ConfigActionApplier {
    private final List<ConfigurablePizzaSlice> originalSlices;
    public final EntirePizzaConfiguratorScreen configuratorScreen;
    public EntirePizzaConfigurator(List<ConfigurablePizzaSlice> originalSlices, Consumer<List<ConfigurablePizzaSlice>> saveCallback) {
        this.originalSlices = originalSlices;
        this.configuratorScreen =  new EntirePizzaConfiguratorScreen(()-> {
            saveCallback.accept(originalSlices);
        });
        rebuildSlices();
    }
    public void rebuildSlices() {
        var l = new LinkedList<EditorPizzaSlice>();
        for(var slice : originalSlices) {
            l.add(new EditorPizzaSlice(slice, this));
        }
        configuratorScreen.setWidgetSlices(l);
    }
    public void setSliceConfigScreen(Screen screen) {
        configuratorScreen.setSliceConfiguratorScreen(screen);
    }
    public void splitSlice(ConfigurablePizzaSlice clickedSlice) {
        CircleSlice circleSlice = clickedSlice.getSlice();
        //slice.
        originalSlices.remove(clickedSlice);
        originalSlices.add(new RunnableSlice("Empty action", new CircleSlice(circleSlice.startAngle, circleSlice.getMidAngle()),clickedSlice.getManager()));
        originalSlices.add(new RunnableSlice("Empty action", new CircleSlice(circleSlice.getMidAngle(), circleSlice.endAngle),clickedSlice.getManager()));
        rebuildSlices();
    }
    @Override
    public void removeSlice(ConfigurablePizzaSlice targetAction) {
        originalSlices.remove(originalSlices.indexOf(targetAction));
        if(originalSlices.size()==0){
            originalSlices.add(new RunnableSlice("Empty action", CircleSlice.percent(0.125f,0.375f),targetAction.getManager()));
        }
        rebuildSlices();
    }
}

