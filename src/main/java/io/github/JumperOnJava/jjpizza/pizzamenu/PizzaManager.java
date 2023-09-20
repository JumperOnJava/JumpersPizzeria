package io.github.JumperOnJava.jjpizza.pizzamenu;

import com.google.gson.reflect.TypeToken;
import io.github.JumperOnJava.jjpizza.pizzamenu.slices.ConfigurablePizzaSlice;
import io.github.JumperOnJava.jjpizza.pizzamenu.slices.runnable.RunnableSlice;
import io.github.JumperOnJava.jjpizza.pizzamenu.slices.runnable.actionregistry.ActionTypeRegistry;
import io.github.JumperOnJava.jjpizza.pizzamenu.configurer.EntirePizzaConfigurator;
import io.github.JumperOnJava.lavajumper.common.FileReadWrite;
import io.github.JumperOnJava.lavajumper.common.Binder;
import io.github.JumperOnJava.lavajumper.datatypes.CircleSlice;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;

import java.io.File;
import java.util.LinkedList;
import java.util.*;

public class PizzaManager {
    List<RunnableSlice> actions = new ArrayList<>();
    public final KeyBinding pizzaKeybind;
    public ActionTypeRegistry actionTypeRegistry = new ActionTypeRegistry();
    public PizzaManager(){
        pizzaKeybind = Binder.addBind("Open Pizza Menu",-1,this::openPizza);
        actions = load();
    }

    public void openPizza(MinecraftClient client){
        //this.slices = new PizzaConfiguration(actions);
        client.setScreen(new PizzaScreen(actions,getBuilderScreen(),this));
    }

    public Screen getBuilderScreen(){
        var configurator = new EntirePizzaConfigurator(new LinkedList<>(actions),this::setSlices);
        return configurator.configuratorScreen;
    }
    public void save(){
        /*var l = new LinkedList<RunnablePizzaSlice>();
        actions.forEach(a->{
            if(a instanceof RunnablePizzaSlice r)
                l.add(r);
        });
        var tt = new TypeToken<LinkedList<RunnablePizzaSlice>>(){}.getType();*/
        try{
            FileReadWrite.write(getConfigFile(), actionTypeRegistry.getGson().toJson(actions));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public List<RunnableSlice> load(){
        if(readConfig().equals("")){
            actions.add(new RunnableSlice("Empty action", CircleSlice.percent(0,.25f),this));
            actions.add(new RunnableSlice("Empty action", CircleSlice.percent(.25f,.5f),this));
            actions.add(new RunnableSlice("Empty action", CircleSlice.percent(.5f,.75f),this));
            actions.add(new RunnableSlice("Empty action", CircleSlice.percent(.75f,1f),this));
            save();
        }
        List<RunnableSlice> l = actionTypeRegistry.getGson().fromJson(readConfig(),new TypeToken<ArrayList<RunnableSlice>>(){}.getType());
        l.forEach(s->s.setManager(this));
        return l;
    }

    private String readConfig() {
        return FileReadWrite.read(getConfigFile());
    }

    private File getConfigFile() {
        return FabricLoader.getInstance().getConfigDir().resolve("jjpizza/main.json").toFile();
    }

    private void setSlices(List<ConfigurablePizzaSlice> configuration) {
        actions.clear();
        configuration.forEach(pizzaSlice -> {if(pizzaSlice instanceof RunnableSlice runnablePizzaSlice) actions.add(runnablePizzaSlice);});
        save();
    }
}


