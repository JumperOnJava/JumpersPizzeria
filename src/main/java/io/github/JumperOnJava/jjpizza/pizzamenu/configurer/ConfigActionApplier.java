package io.github.JumperOnJava.jjpizza.pizzamenu.configurer;

import io.github.JumperOnJava.jjpizza.pizzamenu.actions.ConfigurablePizzaSlice;
import io.github.javajumper.lavajumper.gui.widgets.PizzaSlice;
import net.minecraft.client.gui.screen.Screen;

public interface ConfigActionApplier {

    void setSliceConfigScreen(Screen screen);

    void rebuildSlices();

    void removeSlice(PizzaSlice targetAction);

    void addEmptySlice(ConfigurablePizzaSlice configurablePizzaAction);
}
