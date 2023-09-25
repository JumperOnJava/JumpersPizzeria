package io.github.JumperOnJava.jjpizza.pizzamenu;

import io.github.JumperOnJava.jjpizza.pizzamenu.PizzaManager;
import io.github.JumperOnJava.lavajumper.common.Binder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.option.KeyBinding;

import java.io.File;

public class MainPizzaManager extends PizzaManager {
    public final KeyBinding pizzaKeybind;
    public MainPizzaManager() {
        init();
        pizzaKeybind = Binder.addBind("Open Pizza Menu",-1,this::openPizza);
    }

    @Override
    public boolean matchesKey(int keyCode, int scanCode) {
        return pizzaKeybind.matchesKey(keyCode,scanCode);
    }

    protected File getConfigFile() {
        return FabricLoader.getInstance().getConfigDir().resolve("jjpizza/main.json").toFile();
    }
}
