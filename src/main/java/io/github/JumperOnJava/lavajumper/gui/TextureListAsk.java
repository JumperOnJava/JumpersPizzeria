package io.github.JumperOnJava.lavajumper.gui;

import fi.dy.masa.malilib.gui.widgets.WidgetBase;
import io.github.JumperOnJava.lavajumper.common.Translation;
import io.github.JumperOnJava.lavajumper.gui.widgets.ScrollListWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.TransferQueue;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static io.github.JumperOnJava.jjpizza.pizzamenu.slices.runnable.actionregistry.ActionTypeRegistry.gap;

public class TextureListAsk extends AskScreen<String>{
    ScrollListWidget list;
    private static Map<Identifier, AbstractTexture> textures;
    static{
        textures = MinecraftClient.getInstance().getTextureManager().textures;
    }

    private String successTexture;

    private TextureListAsk(Consumer<String> onSuccess, Runnable onFail){
        super(onSuccess, onFail);
    }
    @Override
    protected void init() {

        list = new ScrollListWidget(client,width,height-22*2,0,22,40);
        filterList("");
        addDrawableChild(list);

        var search = new TextFieldWidget(client.textRenderer,0,0,width,20,Text.empty());
        search.setChangedListener(this::filterList);
        addDrawableChild(search);

        var accept = new ButtonWidget.Builder(Translation.get("jjpizza.texture.accept"),b->success(successTexture))
                .dimensions(width-100+gap/2,height-20-gap/2,100,20).build();
        var cancel = new ButtonWidget.Builder(Translation.get("jjpizza.texture.accept"),b->fail())
                .dimensions(width-100+gap/2,height-20-gap/2,100,20).build();
        addDrawableChild(accept);
        addDrawableChild(cancel);
        addDrawableChild(new ButtonWidget.Builder(
                Text.literal("fail"),(b)->{
            fail();
        }
        ).dimensions(0,0,100,20).build());
        addDrawableChild(new ButtonWidget.Builder(
                Text.literal("succ ess"),(b)->{
            success(String.valueOf(new Random().nextInt()));
        }
        ).dimensions(0,22,100,20).build());
    }

    private void filterList(String s) {
        for(var key : textures.keySet()){
            var id = key.getNamespace()+":"+key.getPath();
            if(!id.toLowerCase().contains(s.toLowerCase()))
                return;
            var button = new ButtonWidget.Builder(Text.literal(id),b->this.successTexture = id)
                    .position(40,10)
                    .size(width-40-gap,20)
                    .build();
            var entry = new ScrollListWidget.ScrollListEntry();
            entry.addDrawableChild(button,true);
            entry.addDrawableChild(new TextureWidget(key,0,0,40,40),false);
            list.addEntry(entry);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
    }

    public
    static class Builder extends AskScreen.Builder<String>{
        @Override
        public TextureListAsk build() {
            return new TextureListAsk(onSuccess,onFail);
        }
    }
}
