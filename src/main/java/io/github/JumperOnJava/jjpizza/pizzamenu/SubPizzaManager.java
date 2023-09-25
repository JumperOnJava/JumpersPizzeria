package io.github.JumperOnJava.jjpizza.pizzamenu;

import net.fabricmc.loader.api.FabricLoader;

import java.io.File;

public class SubPizzaManager extends PizzaManager{
    public final String id;

    public SubPizzaManager(String id) {
        super();
        this.id = id;
    }

    @Override
    protected File getConfigFile() {
        return FabricLoader.getInstance().getConfigDir().resolve("jjpizza/sub/"+id+".json").toFile();
    }

    @Override
    public boolean matchesKey(int keyCode, int scanCode) {
        return false;
    }
}
