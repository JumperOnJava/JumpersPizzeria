package io.github.JumperOnJava.jjpizza.pizzamenu.configurer;

import io.github.JumperOnJava.jjpizza.pizzamenu.slices.ConfigurablePizzaSlice;
import net.minecraft.client.gui.screen.Screen;

public interface ConfigActionApplier {

    void setSliceConfigScreen(Screen screen);

    void rebuildSlices();

    void removeSlice(ConfigurablePizzaSlice targetAction);

    void splitSlice(ConfigurablePizzaSlice configurablePizzaAction);
}
