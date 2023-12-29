package io.github.JumperOnJava.lavajumper.gui.widgets;

import io.github.JumperOnJava.lavajumper.gui.AskScreen;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.function.Consumer;


public abstract class TextureListAsk extends AskScreen<Identifier> {
    ScrollListWidget list;
    //private static Map<Identifier, AbstractTexture> textures;
    /*static{
        var paths = new LinkedList<String>();
        for(var path : paths)
        {
            var manager = MinecraftClient.getInstance().getResourceManager();
            var resources = manager.findResources(path,i-> i.toString().endsWith(".png"));
            textures.addAll(resources.keySet().stream().toList());
        }
    }*/

    private Identifier selectedTexture = new Identifier("minecraft","empty");
    public static final int gap = 2;

    protected TextureListAsk(Consumer<Identifier> onSuccess, Runnable onFail){
        super(onSuccess, onFail);
    }
    public abstract List<Identifier> getTextures();
    private TextureWidget selectedTextureWidget;
    @Override
    protected void init() {
        list = new ScrollListWidget(client,width,height-22*2,0,22,40);
        filterList("");
        addDrawableChild(list);

        var search = new TextFieldWidget(client.textRenderer,0,0,width,20,Text.empty());
        search.setChangedListener(this::filterList);
        addDrawableChild(search);

        var accept = new ButtonWidget.Builder(Text.translatable("ljc.textureselect.accept"),b->success(selectedTexture))
                .dimensions((int) (40+gap),height-20-gap,100,20).build();
        var cancel = new ButtonWidget.Builder(Text.translatable("ljc.textureselect.cancel"),b->fail())
                .dimensions((int) (140+gap*1.5),height-20-gap,100,20).build();
        addDrawableChild(accept);
        addDrawableChild(cancel);
        selectedTextureWidget = new TextureWidget(new Identifier(""),gap/2,height-40-gap/2,40,40);
        addDrawableChild(selectedTextureWidget);
    }

    private void filterList(String s) {
        list.children().clear();
        list.setScrollAmount(0);
        for(var key : getTextures()){
            var id = key.toString();
            if(!id.toLowerCase().contains(s.toLowerCase()))
                continue;
            var button = new ButtonWidget.Builder(Text.literal(id),b->{
                this.selectedTexture = new Identifier(b.getMessage().getString());
                selectedTextureWidget.setTexture(selectedTexture);
            })
                    .position(40,10)
                    .size(width-40-6-gap,20)
                    .build();
            var entry = new ScrollListWidget.ScrollListEntry();
            entry.addDrawableChild(button,true);
            entry.addDrawableChild(new TextureWidget(key,0,0,40,40),false);
            list.addEntry(entry);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        context.drawText(
                textRenderer,
                Text.translatable("jjpizza.texture.selected").append(": ").append(selectedTexture.toString()),
                45,
                height-30-6-gap/2,
                0xFFFFFFFF,
                true);
    }
}
