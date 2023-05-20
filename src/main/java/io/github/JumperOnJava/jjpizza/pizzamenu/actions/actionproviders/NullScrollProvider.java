package io.github.JumperOnJava.jjpizza.pizzamenu.actions.actionproviders;

import io.github.JumperOnJava.jjpizza.pizzamenu.actions.ConfigurableScrollRunnable;
import io.github.javajumper.lavajumper.gui.GuiHelper;
import net.minecraft.client.gui.screen.Screen;

import java.util.function.Consumer;

public class NullScrollProvider implements ConfigurableScrollRunnable {
    @Override
    public Screen getConfigurerScreen() {
        return new GuiHelper.TestScreen(0x5f0088ff);
    }

    @Override
    public void accept(Double aDouble) {

    }
}
