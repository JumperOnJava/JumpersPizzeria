package io.github.JumperOnJava.lavajumper.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class AskScreen<T> extends Screen {
    private final Consumer<T> onSuccess;
    private final Runnable onFail;
    AskScreen(Consumer<T> onSuccess, Runnable onFail){
        super(Text.empty());
        this.onSuccess = onSuccess;
        this.onFail = onFail;
    }
    public abstract static class Builder<T>{
        Consumer<T> onSuccess;
        Runnable onFail;

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
        AskScreenManager.close(this);
        AskScreenManager.close(this);
    }
    @Override
    public void close(){
        fail();
    }
}
