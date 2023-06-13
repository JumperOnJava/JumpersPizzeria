package io.github.JumperOnJava.jjpizza.pizzamenu.actionproviders;

import io.github.JumperOnJava.jjpizza.pizzamenu.actionregistry.ConfigurableRunnable;
import io.github.JumperOnJava.jjpizza.pizzamenu.slices.ConfigurablePizzaSlice;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class MalilibActionProvider implements ConfigurableRunnable {
    private String keyIdentifier;
    private void getTargetKeybind(){

    }
    @Override
    public void setParent(ConfigurablePizzaSlice configurableRunnable) {

    }

    @Override
    public Screen getConfiguratorScreen() {
        //return new MalilibKeybindEditScreen(this);
        return null;
    }

    @Override
    public void run() {

    }
    private class MalilibKeybindEditScreen extends Screen{
        protected MalilibKeybindEditScreen() {
            super(Text.empty());
        }

    }
}
