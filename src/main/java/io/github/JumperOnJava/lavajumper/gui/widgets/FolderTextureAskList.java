package io.github.JumperOnJava.lavajumper.gui.widgets;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.List;
import java.util.function.Consumer;

public class FolderTextureAskList extends TextureListAsk{
    private final TextureFolder folder;
    private List<Identifier> textures;

    public FolderTextureAskList(TextureFolder folder, Consumer<Identifier> onSuccess, Runnable onFail) {
        super(onSuccess, onFail);
        this.folder = folder;
    }

    @Override
    protected void init() {
        super.init();
        var cancel = new ButtonWidget.Builder(Text.translatable("ljc.folderselect.openfolder"), b->openCursorFolder())
                .dimensions((int) (140+gap*1.5),height-20-gap,100,20).build();
        addDrawableChild(cancel);
    }

    private void openCursorFolder() {
        Util.getOperatingSystem().open(folder.path.toFile());
    }

    @Override
    public List<Identifier> getTextures() {
        if (!this.textures.isEmpty())
            return textures;
        folder.redefineTextures();
        this.textures = folder.getTextures();
        return textures;
    }
}
