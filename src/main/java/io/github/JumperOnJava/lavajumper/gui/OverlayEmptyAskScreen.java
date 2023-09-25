package io.github.JumperOnJava.lavajumper.gui;

import io.github.JumperOnJava.lavajumper.gui.widgets.SubScreen;
import net.minecraft.client.gui.screen.Screen;

public class OverlayEmptyAskScreen extends AskScreen<Integer> {
    private final Screen screen;
    SubScreen subScreen = new SubScreen();
    public OverlayEmptyAskScreen(Screen screen){
        super((s)->{},()->{});
        this.screen = screen;
    }

    @Override
    protected void init() {
        addDrawableChild(subScreen);
        subScreen.init(0,0,width,height);
        subScreen.setScreen(screen);
    }
}