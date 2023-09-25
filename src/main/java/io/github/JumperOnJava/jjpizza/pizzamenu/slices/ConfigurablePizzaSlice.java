package io.github.JumperOnJava.jjpizza.pizzamenu.slices;

import io.github.JumperOnJava.jjpizza.pizzamenu.PizzaManager;
import io.github.JumperOnJava.lavajumper.gui.widgets.PizzaSlice;
import net.minecraft.client.gui.screen.Screen;

public interface ConfigurablePizzaSlice extends PizzaSlice {
    Screen getConfiguratorScreen(Runnable updateCallback);

    PizzaManager getManager();
    void setManager(PizzaManager manager);
    ConfigurablePizzaSlice copy();
}
