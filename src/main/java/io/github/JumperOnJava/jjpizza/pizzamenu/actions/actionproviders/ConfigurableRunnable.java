package io.github.JumperOnJava.jjpizza.pizzamenu.actions.actionproviders;

import net.minecraft.client.gui.screen.Screen;

public interface ConfigurableRunnable extends Runnable {
    Screen getConfigurerScreen();
}
