package io.github.JumperOnJava.jjpizza.pizzamenu.actionproviders;

import io.github.JumperOnJava.jjpizza.pizzamenu.actionregistry.ConfigurableRunnable;
import io.github.JumperOnJava.jjpizza.pizzamenu.slices.ConfigurablePizzaSlice;
import io.github.JumperOnJava.lavajumper.common.Translation;
import io.github.JumperOnJava.lavajumper.gui.widgets.ScrollListWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.Text;
import java.util.*;

import static io.github.JumperOnJava.jjpizza.pizzamenu.actionregistry.ActionTypeRegistry.gap;

public class KeybindingActionProvider implements ConfigurableRunnable, TargetKeybindStorage {
    public static Set<String> awaitingMatch = new HashSet<>();
    private String targetKeyBindingID = null;
    private boolean hold = false;
    private transient ConfigurablePizzaSlice parent;

    public KeybindingActionProvider(Boolean isReal){}
    private KeyBinding getTargetKeyBinding(){
            for(var kb : MinecraftClient.getInstance().options.allKeys){
                if(kb.getTranslationKey().equals(targetKeyBindingID))
                    return kb;
            }
        return null;
    }

    @Override
    public void setParent(ConfigurablePizzaSlice pizzaSlice) {
        this.parent=pizzaSlice;
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
    public List<TargetKeybind> getKeyBindings(){
        var l = MinecraftClient.getInstance().options.allKeys;
        List<TargetKeybind> keybinds = new ArrayList<>();
        for(var k : l){
            keybinds.add(new VanillaKBWrapper(k));
        }
        return keybinds;
    }

    @Override
    public void setTargetID(String id) {
        this.targetKeyBindingID=id;
    }

    @Override
    public String getTargetId() {
        return targetKeyBindingID;
    }

    @Override
    public Text getHoldText() {
        return Translation.get("jjpizza.keybind.hold."+(hold?"on":"off"));
    }

    @Override
    public void nextHoldMode() {
        hold=!hold;
        var targetKeyBinding = getTargetKeyBinding();
        if(targetKeyBinding==null)
            return;
        targetKeyBinding.reset();
    }

    private static class VanillaKBWrapper implements TargetKeybind{
        private final KeyBinding keybind;

        public VanillaKBWrapper(KeyBinding binding){
            this.keybind = binding;
        }
        public Text getButtonText() {
            return Text.translatable(keybind.getCategory()).append(" : ").append(Text.translatable(keybind.getTranslationKey()));
        }

        @Override
        public String getId() {
            return keybind.getTranslationKey();
        }

        @Override
        public boolean matches(String search) {
            return keybind.getTranslationKey().contains(search) ||
                    I18n.translate(keybind.getTranslationKey()).contains(search);
        }
    }

    public static class KeyBindingEditScreen extends Screen{
        private final TargetKeybindStorage target;
        protected KeyBindingEditScreen(TargetKeybindStorage target) {
            super(Text.empty());
            this.target = target;
        }
        public void init(){

            var searchBox = new TextFieldWidget(client.textRenderer,2,2,width-4,20,Text.empty());
            var listWidget = new ScrollListWidget(client,width,height-20,24,22);
            addDrawableChild(listWidget);
            for(TargetKeybind keybind : target.getKeyBindings()){
                var listEntry = new ScrollListWidget.ScrollListEntry();
                listWidget.addEntry(listEntry);
                Text buttonText = keybind.getButtonText();
                var activateButton = new ButtonWidget.Builder(buttonText,b->{
                    listEntry.setMeActive();
                    target.setTargetID(keybind.getId());
                }).width(width-gap).build();
                listEntry.addDrawableChild(activateButton,true);
                if(keybind.getId().equals(target.getTargetId()))
                    listWidget.setSelectedEntry(listEntry);
            }
            var holdModeButton = new ButtonWidget.Builder(target.getHoldText(),this::holdButton)
                    .size(width-gap/2,20)
                    .position(0,height-20-gap/2).build();
            addDrawableChild(holdModeButton);
        }

        private void holdButton(ButtonWidget buttonWidget) {
            target.nextHoldMode();
            buttonWidget.setMessage(target.getHoldText());
        }
    }
}
