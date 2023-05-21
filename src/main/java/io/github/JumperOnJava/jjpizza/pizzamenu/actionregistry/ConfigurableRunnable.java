package io.github.JumperOnJava.jjpizza.pizzamenu.actionregistry;

import net.minecraft.client.gui.screen.Screen;

public interface ConfigurableRunnable extends Runnable {
    Screen getConfiguratorScreen();
}
