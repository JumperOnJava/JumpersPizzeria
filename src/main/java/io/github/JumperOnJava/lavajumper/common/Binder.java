package io.github.JumperOnJava.lavajumper.common;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

import java.util.function.Consumer;

public class Binder {
    /**
     * Adds bind
     * @param displayName
     * @param category
     * @param defaultKey
     * @param callback
     */
    public static KeyBinding addBind(String displayName, String category, int defaultKey, Consumer<MinecraftClient> callback)
    {
        var bind = new KeyBinding(displayName, InputUtil.Type.KEYSYM, defaultKey, category); //GLFW.GLFW_KEY_DOWN
        KeyBindingHelper.registerKeyBinding(bind);
        ClientTickEvents.END_CLIENT_TICK.register(client ->
        {
            while (bind.wasPressed()) {
                callback.accept(client);
            }
        });
        return bind;
    }

    /**
     * Adds bind under "LavaJumper" category
     *
     * @param displayName
     * @param defaultKey
     * @param callback
     * @return
     * @api
     */
    public static KeyBinding addBind(String displayName, int defaultKey, Consumer<MinecraftClient> callback)
    {
        return addBind(displayName,"LavaJumper",defaultKey,callback);
    }

    /**
     * Adds bind without default key
     * @param displayName
     * @param category
     * @param callback
     */

    public static KeyBinding addBind(String displayName, String category, Consumer<MinecraftClient> callback){
        return addBind(displayName,category,-1,callback);
    }
}
