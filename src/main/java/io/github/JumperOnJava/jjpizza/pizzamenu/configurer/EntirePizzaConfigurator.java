package io.github.JumperOnJava.jjpizza.pizzamenu.configurer;

import io.github.JumperOnJava.jjpizza.pizzamenu.actions.ConfigurablePizzaSlice;
import io.github.JumperOnJava.jjpizza.pizzamenu.actions.ConfiguringPizzaSlice;
import io.github.JumperOnJava.jjpizza.pizzamenu.actions.RunnablePizzaSlice;
import io.github.javajumper.lavajumper.datatypes.CircleSlice;
import io.github.javajumper.lavajumper.gui.widgets.PizzaSlice;
import io.github.javajumper.lavajumper.gui.widgets.PizzaWidget.PizzaConfiguration;
import net.minecraft.client.Keyboard;
import net.minecraft.client.gui.screen.Screen;

import java.util.function.Consumer;

public class EntirePizzaConfigurator implements ConfigActionApplier {
    private final PizzaConfiguration originalSlices;
    public final EntirePizzaConfiguratorScreen configuratorScreen;
    public EntirePizzaConfigurator(PizzaConfiguration originalSlices, Consumer<PizzaConfiguration> saveCallback) {
        this.originalSlices = originalSlices;
        this.configuratorScreen =  new EntirePizzaConfiguratorScreen(()-> {
            //saveCallback.accept(originalSlices);
        });
        rebuildSlices();
    }
    public void rebuildSlices() {
        var l = new PizzaConfiguration();
        for(PizzaSlice slice : originalSlices.slices){
            if(slice instanceof ConfigurablePizzaSlice configurableSlice)
            {
                l.slices.add(new ConfiguringPizzaSlice(configurableSlice,this));
            }
        }
        configuratorScreen.setWidgetSlices(l);
    }
    public void setSliceConfigScreen(Screen screen) {
        configuratorScreen.setSliceConfiguratorScreen(screen);
    }
    public void addEmptySlice(ConfigurablePizzaSlice clickedSlice) {
        CircleSlice circleSlice = clickedSlice.getSlice();
        //slice.
        originalSlices.slices.remove(clickedSlice);
        originalSlices.slices.add(new RunnablePizzaSlice("Empty action", new CircleSlice(circleSlice.startAngle, circleSlice.getMidAngle())));
        originalSlices.slices.add(new RunnablePizzaSlice("Empty action", new CircleSlice(circleSlice.getMidAngle(), circleSlice.endAngle)));
        rebuildSlices();
    }
    @Override
    public void removeSlice(PizzaSlice targetAction) {
        originalSlices.slices.remove(originalSlices.slices.indexOf(targetAction));
        if(originalSlices.slices.size()==0){
            originalSlices.slices.add(new RunnablePizzaSlice("Empty action", CircleSlice.percent(0.125f,0.375f)));
        }
        rebuildSlices();
    }
}

