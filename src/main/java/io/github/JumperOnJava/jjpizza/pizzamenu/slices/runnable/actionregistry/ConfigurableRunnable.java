package io.github.JumperOnJava.jjpizza.pizzamenu.slices.runnable.actionregistry;

import io.github.JumperOnJava.jjpizza.pizzamenu.slices.ConfigurablePizzaSlice;
import net.minecraft.client.gui.screen.Screen;

public interface ConfigurableRunnable extends Runnable {
    /**
     * ignore if you dont need parent slice
     * @param configurableRunnable
     */
    default void setParent(ConfigurablePizzaSlice configurableRunnable){};
    Screen getConfiguratorScreen();
}
