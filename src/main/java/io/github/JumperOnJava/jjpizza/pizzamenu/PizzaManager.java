package io.github.JumperOnJava.jjpizza.pizzamenu;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.github.JumperOnJava.jjpizza.pizzamenu.actions.RunnablePizzaSlice;
import io.github.JumperOnJava.jjpizza.pizzamenu.actions.actionproviders.ActionTypes;
import io.github.JumperOnJava.jjpizza.pizzamenu.configurer.EntirePizzaConfigurator;
import io.github.javajumper.lavajumper.common.Binder;
import io.github.javajumper.lavajumper.common.FileReadWrite;
import io.github.javajumper.lavajumper.datatypes.CircleSlice;
import io.github.javajumper.lavajumper.gui.widgets.PizzaWidget;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;

import java.io.File;
import java.util.LinkedList;
import java.util.*;

public class PizzaManager {
    List<RunnablePizzaSlice> actions = new LinkedList<>();

    private static PizzaManager mainManager;
    public static PizzaManager getManager(){
        if(mainManager==null)
            mainManager=new PizzaManager();
        return mainManager;
    }
    private PizzaManager(){
        Binder.addBind("Open Pizza menu",-1,this::openPizza);
        actions = load();
    }

    public PizzaWidget.PizzaConfiguration getSlices() {
        return new PizzaWidget.PizzaConfiguration(actions);
    }

    public void openPizza(MinecraftClient client){
        //this.slices = new PizzaConfiguration(actions);
        client.setScreen(new PizzaScreen(getSlices(),getBuilderScreen()));
    }

    public Screen getBuilderScreen(){
        var configurator = new EntirePizzaConfigurator(new PizzaWidget.PizzaConfiguration(actions),this::setSlices);
        return configurator.configuratorScreen;
    }
    public void save(){
        FileReadWrite.write(getConfigFile(),ActionTypes.getGson().toJson(actions));
    }
    public List<RunnablePizzaSlice> load(){
        if(readConfig().equals("")){
            actions.add(new RunnablePizzaSlice("Empty action", CircleSlice.percent(0,.25f)));
            actions.add(new RunnablePizzaSlice("Empty action", CircleSlice.percent(.25f,.5f)));
            actions.add(new RunnablePizzaSlice("Empty action", CircleSlice.percent(.5f,.75f)));
            actions.add(new RunnablePizzaSlice("Empty action", CircleSlice.percent(.75f,1f)));
            save();
        }
        return ActionTypes.getGson().fromJson(readConfig(),new TypeToken<LinkedList<RunnablePizzaSlice>>(){}.getType());
    }

    private String readConfig() {
        return FileReadWrite.read(getConfigFile());
    }

    private File getConfigFile() {
        return FabricLoader.getInstance().getConfigDir().resolve("jjpizza/main.json").toFile();
    }

    private void setSlices(PizzaWidget.PizzaConfiguration configuration) {
        actions.clear();
        configuration.slices.forEach(pizzaSlice -> {if(pizzaSlice instanceof RunnablePizzaSlice runnablePizzaSlice) actions.add(runnablePizzaSlice);});
    }
}


