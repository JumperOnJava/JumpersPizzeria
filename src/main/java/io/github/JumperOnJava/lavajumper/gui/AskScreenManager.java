package io.github.JumperOnJava.lavajumper.gui;

import com.mojang.datafixers.types.templates.List;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.ParentElement;

import java.lang.reflect.Field;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Objects;

public class AskScreenManager {
    public static <T> void ask(AskScreen<T> askScreen){
        var client = MinecraftClient.getInstance();
        var currentScreen = client.currentScreen;
        if(currentScreen==null){
            client.setScreen(askScreen);
            return;
        }
        var askSubScreen = new OverlayScreen();
        askSubScreen.init(0,0, currentScreen.width,currentScreen.height);
        askSubScreen.setScreen(askScreen);
        currentScreen.children();
        currentScreen.drawables.add(askSubScreen);
        ((java.util.List<Element>)currentScreen.children()).add(0,askSubScreen);
    }

    public static <T extends AskScreen<?>> void close(T screen) {
        var client = MinecraftClient.getInstance();
        var children = client.currentScreen.children();
        var del = new ArrayList<Element>();
        for(var c : children){
            if(c instanceof OverlayScreen overlayScreen){
                //bruh gonna use reflection
                try {
                    Field field = SubScreen.class.getDeclaredField("screen");
                    field.setAccessible(true);
                    if (field.get(overlayScreen) == screen) {
                        del.add(c);
                    }
                } catch (Exception e){throw new RuntimeException(e);}//lmao
            }
        }
        for(var d : del){
            client.currentScreen.remove(d);
        }
    }
}
