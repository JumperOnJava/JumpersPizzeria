package io.github.JumperOnJava.jjpizza.pizzamenu.actions;

import io.github.javajumper.lavajumper.gui.widgets.PizzaSlice;
import net.minecraft.client.gui.screen.Screen;

public interface ConfigurablePizzaSlice extends PizzaSlice {
    Screen getConfiguratorScreen(Runnable updateCallback);

}
