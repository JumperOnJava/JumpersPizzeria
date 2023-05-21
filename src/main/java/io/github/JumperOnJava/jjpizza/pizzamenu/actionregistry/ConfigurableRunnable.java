package io.github.JumperOnJava.jjpizza.pizzamenu.actionregistry;

import io.github.JumperOnJava.jjpizza.pizzamenu.slices.ConfigurablePizzaSlice;
import net.minecraft.client.gui.screen.Screen;

public interface ConfigurableRunnable extends Runnable {
    void setParent(ConfigurablePizzaSlice configurableRunnable);
    Screen getConfiguratorScreen();
}
