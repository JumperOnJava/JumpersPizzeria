package io.github.JumperOnJava.lavajumper.common;

import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.Map;

public class Translation {
    static Map<String,String> translationMap = new HashMap<>();

    /**
     * Generates file for translation in game and puts it into .autoTranslateOutput folder
     */
    public static void generateTranlationMap()
    {
        var file = FabricLoader.getInstance().getGameDir().resolve(".autoTranslateOutput").resolve("output.txt").toFile();
        FileReadWrite.write(file,new GsonBuilder().setPrettyPrinting().create().toJson(translationMap));
    }
    public static void clearTranslationMap(){
        translationMap = new HashMap<>();
    }
    private static void addKeyToTranslation(String key)
    {
        if(!I18n.hasTranslation(key))
            translationMap.put(key,"");
    }
    /**
     * Gets translatable text component for key and adds it to list if it is doesn't have translation.
     * After getting all translations you should call generateTranslationMap() static method to save it to .autoTranslateOutput folder
     * @param key
     * @return
     */
    public static MutableText get(String key){
        addKeyToTranslation(key);
        return Text.translatable(key);
    }
}