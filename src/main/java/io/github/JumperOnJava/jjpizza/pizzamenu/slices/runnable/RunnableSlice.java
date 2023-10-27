package io.github.JumperOnJava.jjpizza.pizzamenu.slices.runnable;

import com.google.gson.annotations.Expose;
import io.github.JumperOnJava.jjpizza.datatypes.Angle;
import io.github.JumperOnJava.jjpizza.datatypes.CircleSlice;
import io.github.JumperOnJava.jjpizza.pizzamenu.PizzaManager;
import io.github.JumperOnJava.jjpizza.pizzamenu.slices.runnable.actionregistry.ConfigurableRunnable;
import io.github.JumperOnJava.jjpizza.pizzamenu.slices.runnable.actionproviders.NullActionProvider;
import io.github.JumperOnJava.jjpizza.pizzamenu.slices.ConfigurablePizzaSlice;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;

import java.util.Random;


public class RunnableSlice implements ConfigurablePizzaSlice {
    @Expose
    CircleSlice circleSlice;
    @Expose
    String name;
    @Expose
    int color;
    @Expose
    ConfigurableRunnable onLeftClick = new NullActionProvider(false);
    @Expose
    ConfigurableRunnable onRightClick = new NullActionProvider(false);
    @Expose
    Identifier icon = new Identifier("textures/item/diamond.png");

    @Override
    public PizzaManager getManager() {
        return manager;
    }
    public void setManager(PizzaManager manager){
        this.manager=manager;
    }

    @Override
    public ConfigurablePizzaSlice copy() {
        var newSlice = new RunnableSlice(this.name,this.circleSlice,manager);
        newSlice.circleSlice = new CircleSlice(circleSlice.startAngle,circleSlice.endAngle);
        newSlice.icon=this.icon;
        newSlice.onLeftClick = this.onLeftClick.copy();
        newSlice.onRightClick = this.onRightClick.copy();
        newSlice.color=this.color;
        return newSlice;
    }

    transient PizzaManager manager;


    public RunnableSlice(String name, CircleSlice circleSlice, PizzaManager manager){
        this.name=name;
        setSlice(circleSlice);
        var r = new Random();
        this.color = ColorHelper.Argb.getArgb(255,r.nextInt(255),r.nextInt(255),r.nextInt(255));
        //ActionTextRenderer.sendChatMessage(Integer.toHexString(this.color));
        this.manager = manager;
    }

    @Override
    public void onLeftClick() {
        MinecraftClient.getInstance().setScreen(null);
        onLeftClick.run();
    }

    @Override
    public void onRightClick() {
        MinecraftClient.getInstance().setScreen(null);
        onRightClick.run();
    }

    public Text getName(){
        return Text.literal(name);
    }
    public Identifier getIconTexture(){
        return icon;
    }
    @Override
    public CircleSlice getSlice() {
        return this.circleSlice;
    }
    public void setSlice(CircleSlice inCircleSlice){
        if(Math.abs(inCircleSlice.startAngle.getDegree() - inCircleSlice.endAngle.getDegree())<5)
            this.circleSlice =new CircleSlice(
                    inCircleSlice.startAngle.add(
                            Angle.newDegree(-5)
                    ),
                    inCircleSlice.endAngle.add(
                            Angle.newDegree(5)
                    )
            );
        else
            this.circleSlice = inCircleSlice;
    }
    public int getBackgroundColor(){
        return color;
    }

    public Screen getConfiguratorScreen(Runnable updateCallback) {
        return new RunnableScreen(this,updateCallback);
    }

}
