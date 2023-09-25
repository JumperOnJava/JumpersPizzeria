package io.github.JumperOnJava.jjpizza.pizzamenu;

import com.google.gson.reflect.TypeToken;
import io.github.JumperOnJava.jjpizza.pizzamenu.configurer.EntirePizzaConfiguratorScreen;
import io.github.JumperOnJava.jjpizza.pizzamenu.slices.ConfigurablePizzaSlice;
import io.github.JumperOnJava.jjpizza.pizzamenu.slices.runnable.RunnableSlice;
import io.github.JumperOnJava.jjpizza.pizzamenu.slices.runnable.actionregistry.ActionTypeRegistry;
import io.github.JumperOnJava.lavajumper.common.FileReadWrite;
import io.github.JumperOnJava.lavajumper.common.Binder;
import io.github.JumperOnJava.lavajumper.datatypes.CircleSlice;
import io.github.JumperOnJava.lavajumper.gui.AskScreenManager;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;

import java.io.File;
import java.util.LinkedList;
import java.util.*;

public abstract class PizzaManager {
    List<RunnableSlice> actions = new ArrayList<>();
    public static ActionTypeRegistry actionTypeRegistry = new ActionTypeRegistry();
    public PizzaManager(){
        actions = load();
    }

    public void openPizza(MinecraftClient client){
        //this.slices = new PizzaConfiguration(actions);

        client.setScreen(new PizzaScreen(actions,getBuilderScreen(),this));
    }

    public Screen getBuilderScreen(){
        return new EntirePizzaConfiguratorScreen(new LinkedList<>(actions),this::setSlices,()->{});
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

    protected abstract File getConfigFile();

    private void setSlices(List<ConfigurablePizzaSlice> configuration) {
        actions.clear();
        configuration.forEach(pizzaSlice -> {if(pizzaSlice instanceof RunnableSlice runnablePizzaSlice) actions.add(runnablePizzaSlice);});
        save();
    }

    public abstract boolean matchesKey(int keyCode, int scanCode);
}


