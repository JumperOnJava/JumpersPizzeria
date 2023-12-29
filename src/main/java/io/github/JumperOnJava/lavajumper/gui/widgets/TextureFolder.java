package io.github.JumperOnJava.lavajumper.gui.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TextureFolder {
    public final Path path;
    public final String namespace;
    private Map<Identifier,Path> textures = new HashMap<>();
    private List<Path> toRegister;

    /**
     * Initializes texture folder
     * DOES NOT SCAN THIS FOLDER AUTOMATICALLY but creates it to prevent some crashes
     * Use redefineTextures() to scan and update textures from folder
     */
    public TextureFolder(Path path,String namespace){
        this.path = path;
        this.namespace = namespace;
        if(!Files.isDirectory(path)){
            throw new RuntimeException("Path %s is not a directory".formatted(path));
        }
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * redefines textures in given folder
     */
    public void redefineTextures(){
        redefineTextures(path);
    }
    private void redefineTextures(Path path){
        try{
            var objects = Files.list(path);
            for(var object : objects.toList()){
                if(Files.isDirectory(object))
                    redefineTextures(object);
                else if(object.endsWith(".png"))
                    queueTextureToRegister(path);
            }
            reregisterTextures();
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private void queueTextureToRegister(Path path) {
        this.toRegister.add(path);
    }
    private void reregisterTextures(){
        RenderSystem.recordRenderCall(()-> {
            var tman = MinecraftClient.getInstance().getTextureManager();
            textures.forEach((k, v) -> {
                tman.destroyTexture(k);
            });
            toRegister.forEach((p) -> {
                try {
                    var stream = Files.newInputStream(p, StandardOpenOption.READ);
                    var nativeImage = NativeImage.read(stream);
                    stream.close();

                    var texture = new NativeImageBackedTexture(nativeImage);

                    tman.registerTexture(getIdentifierFor(p), texture);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        });
    }

    private Identifier getIdentifierFor(Path p) {
        var s = p.toAbsolutePath().toString().replaceAll(path.toAbsolutePath().toString(),"");
        return new Identifier(namespace,s);
    }

    /**
     * return COPY of textures list, empty if no scan was done
     */
    public List<Identifier> getTextures(){
        return new ArrayList(this.textures.entrySet());
    }
}
