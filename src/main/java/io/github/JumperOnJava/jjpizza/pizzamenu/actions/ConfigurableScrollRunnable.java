package io.github.JumperOnJava.jjpizza.pizzamenu.actions;

import net.minecraft.client.gui.screen.Screen;

import java.util.function.Consumer;

public interface ConfigurableScrollRunnable extends Consumer<Double> {
    Screen getConfigurerScreen();

}
