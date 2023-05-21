package io.github.JumperOnJava.jjpizza.pizzamenu.actionproviders;

import io.github.JumperOnJava.jjpizza.pizzamenu.actionregistry.ConfigurableRunnable;
import io.github.JumperOnJava.lavajumper.common.Translation;
import io.github.JumperOnJava.lavajumper.gui.widgets.ScrollListWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;
import org.spongepowered.include.com.google.gson.annotations.Expose;
import java.util.*;

import static io.github.JumperOnJava.jjpizza.pizzamenu.actionregistry.ActionTypeRegistry.gap;

public class KeybindingActionProvider implements ConfigurableRunnable {
    public static Set<String> awaitingMatch = new HashSet<>();
    @Expose
    private String targetKeyBindingID = null;
    private boolean hold = false;
    public KeybindingActionProvider(Boolean isReal){}
    private KeyBinding getTargetKeyBinding(){
            for(var kb : getKeyBindings()){
                if(kb.getTranslationKey().equals(targetKeyBindingID))
                    return kb;
            }
        return null;
    }
    @Override
    public Screen getConfiguratorScreen() {
        return new KeyBindingEditScreen(this);
    }
    @Override
    public void run() {
        var targetKeyBinding = getTargetKeyBinding();
        if(targetKeyBinding==null)
            return;
        if(hold){
            targetKeyBinding.setPressed(!targetKeyBinding.isPressed());
        }
        else{
            awaitingMatch.add(targetKeyBindingID);
            targetKeyBinding.timesPressed++;
            var client = MinecraftClient.getInstance();
            client.keyboard.onKey(client.getWindow().getHandle(),-1,-1,1,-1);
        }
    }
    private void setHoldMode(boolean b){
        hold=b;
        var targetKeyBinding = getTargetKeyBinding();
        if(targetKeyBinding==null)
            return;
        targetKeyBinding.reset();
    }
    private boolean getHoldMode(){
        return hold;
    }
    private KeyBinding[] getKeyBindings(){
        return MinecraftClient.getInstance().options.allKeys;
    }
    public static class KeyBindingEditScreen extends Screen{
        private final KeybindingActionProvider target;
        protected KeyBindingEditScreen(KeybindingActionProvider target) {
            super(Text.empty());
            this.target = target;
        }
        public void init(){

            var listWidget = new ScrollListWidget(client,width,height-20,0,22);
            addDrawableChild(listWidget);
            for(var keybind : target.getKeyBindings()){
                var listEntry = new ScrollListWidget.ScrollListEntry();
                listWidget.addEntry(listEntry);
                var activateButton = new ButtonWidget.Builder(Text.translatable(keybind.getTranslationKey()),b->{
                    listEntry.setMeActive();
                    target.targetKeyBindingID = keybind.getTranslationKey();
                }).width(width-gap).build();
                listEntry.addDrawableChild(activateButton,true);
                if(keybind.getTranslationKey().equals(target.targetKeyBindingID))
                    listWidget.setSelectedEntry(listEntry);
            }
            var holdModeButton = new ButtonWidget.Builder(getHoldText(target.getHoldMode()),this::holdButton)
                    .size(width-gap/2,20)
                    .position(0,height-20-gap/2).build();
            addDrawableChild(holdModeButton);
        }

        private void holdButton(ButtonWidget buttonWidget) {
            target.setHoldMode(!target.getHoldMode());
            buttonWidget.setMessage(getHoldText(target.getHoldMode()));
        }
        private static Text getHoldText(boolean hold){
            return Translation.get("jjpizza.keybind.hold."+(hold?"on":"off"));
        }
    }
}
