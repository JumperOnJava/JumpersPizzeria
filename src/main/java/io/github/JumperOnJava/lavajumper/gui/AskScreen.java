package io.github.JumperOnJava.lavajumper.gui;

import io.github.JumperOnJava.lavajumper.gui.widgets.SubScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.function.Consumer;

public abstract class AskScreen<T> extends Screen {
    private final Consumer<T> onSuccess;
    private final Runnable onFail;
    public AskScreen(Consumer<T> onSuccess, Runnable onFail){
        super(Text.empty());
        this.onSuccess = onSuccess;
        this.onFail = onFail;
    }
    public abstract static class Builder<T>{
        protected Consumer<T> onSuccess;
        protected Runnable onFail;

        public Builder<T> onSuccess(Consumer<T> onSuccess) {
            this.onSuccess = onSuccess;
            return this;
        }

        public Builder<T> onFail(Runnable onFail) {
            this.onFail = onFail;
            return this;
        }
        public abstract AskScreen<T> build();
    }
    public void success(T ret){
        onSuccess.accept(ret);
        this.initClose();
    }
    public void fail(){
        onFail.run();
        this.initClose();
    }
    public void initClose(){
        closeScreen(this);
    }
    @Override
    public void close(){
        fail();
    }
    public static <T> void ask(AskScreen<T> askScreen){
        var client = MinecraftClient.getInstance();
        var currentScreen = client.currentScreen;
        if(currentScreen==null){
            client.setScreen(askScreen);
            return;
        }
        var askSubScreen = new OverlayScreen(0,0, currentScreen.width,currentScreen.height).setScreen(askScreen);
        currentScreen.children();
        currentScreen.drawables.add(0,askSubScreen);
        ((java.util.List<Element>)currentScreen.children()).add(0,askSubScreen);
    }

    private static <T extends AskScreen<?>> void closeScreen(T screen) {
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

    public static class OverlayScreen extends SubScreen {
        private OverlayScreen(int x, int y, int width, int height) {
            super(x, y, width, height);
        }

        @Override
        public boolean isMouseOver(double mouseX, double mouseY) {
            super.isMouseOver(mouseX, mouseY);
            return true;
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            super.mouseClicked(mouseX, mouseY, button);
            return true;
        }
        @Override
        public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount)
        {
            super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
            return true;
        }

        @Override
        public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
            super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
            return true;
        }

        @Override
        public boolean mouseReleased(double mouseX, double mouseY, int button) {
            super.mouseReleased(mouseX, mouseY, button);
            return true;
        }

    }
}
