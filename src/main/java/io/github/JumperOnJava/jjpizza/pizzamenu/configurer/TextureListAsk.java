package io.github.JumperOnJava.jjpizza.pizzamenu.configurer;

import io.github.JumperOnJava.jjpizza.pizzamenu.widgets.TextureWidget;
import io.github.JumperOnJava.lavajumper.common.Tr;
import io.github.JumperOnJava.lavajumper.gui.AskScreen;
import io.github.JumperOnJava.lavajumper.gui.widgets.ScrollListWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.LinkedList;
import java.util.function.Consumer;

import static io.github.JumperOnJava.jjpizza.pizzamenu.slices.runnable.actionregistry.ActionTypeRegistry.gap;

public class TextureListAsk extends AskScreen<Identifier> {
    ScrollListWidget list;
    //private static Map<Identifier, AbstractTexture> textures;
    private static java.util.List<Identifier> textures = new LinkedList<>();
    static{
        var paths = new LinkedList<String>();
        paths.add("textures/item");
        paths.add("textures/block");
        paths.add("icon");
        paths.add("icons");
        paths.add("textures/particle");
        paths.add("textures/painting");
        paths.add("textures/mob_effect");
        paths.add("textures/gui/sprites/hud/heart");
        paths.add("textures/gui/sprites/icon");
        paths.add("textures/gui/sprites/pending_invite");
        paths.add("textures/gui/sprites/player_list");
        paths.add("textures/gui/sprites/server_list");
        paths.add("textures/gui/sprites/spectator");
        paths.add("textures/gui/sprites/statistics");
        for(var path : paths)
        {
            var manager = MinecraftClient.getInstance().getResourceManager();
            var resources = manager.findResources(path,i-> i.toString().endsWith(".png"));
            textures.addAll(resources.keySet().stream().toList());
        }
    }

    private Identifier selectedTexture = Identifier.of("empty");

    protected TextureListAsk(Consumer<Identifier> onSuccess, Runnable onFail){
        super(onSuccess, onFail);
    }
    private TextureWidget selectedTextureWidget;
    @Override
    protected void init() {
        list = new ScrollListWidget(client,width,height-22*2,0,22,40);
        filterList("");
        addDrawableChild(list);

        var search = new TextFieldWidget(client.textRenderer,0,0,width,20,Text.empty());
        search.setChangedListener(this::filterList);
        addDrawableChild(search);

        var accept = new ButtonWidget.Builder(Tr.get("jjpizza.texture.accept"), b->success(selectedTexture))
                .dimensions((int) (40+gap),height-20-gap,100,20).build();
        var cancel = new ButtonWidget.Builder(Tr.get("jjpizza.texture.cancel"), b->fail())
                .dimensions((int) (140+gap*1.5),height-20-gap,100,20).build();
        addDrawableChild(accept);
        addDrawableChild(cancel);
        
        selectedTextureWidget = new TextureWidget(Identifier.of(""),gap/2,height-40-gap/2,40,40);
        addDrawableChild(selectedTextureWidget);
    }

    private void filterList(String s) {
        list.children().clear();
        list.setScrollY(0);
        for(var key : textures){
            var id = key.toString();
            if(!id.toLowerCase().contains(s.toLowerCase()))
                continue;
            var button = new ButtonWidget.Builder(Text.literal(id),b->{
                this.selectedTexture = Identifier.of(b.getMessage().getString());
                selectedTextureWidget.setTexture(selectedTexture);
            })
                .position(40,10)
                .width(width-40-6-gap)
                .build();
            var entry = new ScrollListWidget.ScrollListEntry();
            entry.addDrawableChild(button,true);
            entry.addDrawableChild(new TextureWidget(key,0,0,40,40),false);
            list.addEntry(entry);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawText(
                textRenderer,
                Tr.get("jjpizza.texture.selected").append(": ").append(selectedTexture.toString()),
                45,
                height-30-6-gap/2,
                0xFFFFFFFF,
                true);
    }

    public static class Builder extends AskScreen.Builder<Identifier>{
        @Override
        public TextureListAsk build() {
            return new TextureListAsk(onSuccess,onFail);
        }
    }
}
