package io.github.JumperOnJava.jjpizza.pizzamenu.actions;

import io.github.JumperOnJava.jjpizza.pizzamenu.configurer.ConfigActionApplier;
import io.github.javajumper.lavajumper.datatypes.CircleSlice;
import io.github.javajumper.lavajumper.gui.widgets.PizzaSlice;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ConfiguringPizzaSlice implements PizzaSlice {
    private final ConfigurablePizzaSlice targetAction;
    private final Consumer<Screen> clickCallback;
    private final Runnable updateCallback;
    private final Consumer<ConfigurablePizzaSlice> rightClickCallback;
    private final Consumer<PizzaSlice> removeCallback;

    public ConfiguringPizzaSlice(ConfigurablePizzaSlice targetAction, ConfigActionApplier athing){
        this.targetAction = targetAction;
        this.clickCallback = athing::setSliceConfigScreen;
        this.rightClickCallback = athing::addEmptySlice;
        this.updateCallback = athing::rebuildSlices;
        this.removeCallback = athing::removeSlice;
    }

    @Override
    public void onLeftClick() {
        clickCallback.accept(targetAction.getConfiguratorScreen(updateCallback::run));
    }

    @Override
    public void onRightClick() {
        rightClickCallback.accept(targetAction);
    }

    public CircleSlice getSlice() {
        return targetAction.getSlice();
    }
    public void remove(){
        removeCallback.accept(targetAction);
    }

    public int getBackgroundColor(){
        return targetAction.getBackgroundColor();
    }

    @Override
    public Identifier getIconTexture() {
        return targetAction.getIconTexture();
    }

    @Override
    public Text getName() {
        return targetAction.getName();
    }
}
