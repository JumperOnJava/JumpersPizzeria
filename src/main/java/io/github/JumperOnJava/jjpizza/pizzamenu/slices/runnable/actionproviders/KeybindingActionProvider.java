package io.github.JumperOnJava.jjpizza.pizzamenu.slices.runnable.actionproviders;

import io.github.JumperOnJava.jjpizza.pizzamenu.slices.runnable.actionregistry.ConfigurableRunnable;
import io.github.JumperOnJava.jjpizza.pizzamenu.slices.ConfigurablePizzaSlice;
import io.github.JumperOnJava.jjpizza.pizzamenu.slices.runnable.actionregistry.ActionTypeRegistry;
import io.github.JumperOnJava.lavajumper.common.Tr;
import io.github.JumperOnJava.lavajumper.gui.widgets.ScrollListWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.Text;

import java.util.*;

public class KeybindingActionProvider implements ConfigurableRunnable, TargetKeybindStorage {
    public static Set<String> awaitingMatch = new HashSet<>();
    private String targetKeyBindingID = "";
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
    public ConfigurableRunnable copy() {
        var kb = new KeybindingActionProvider(true);
        kb.hold=hold;
        if(targetKeyBindingID == null)
            targetKeyBindingID = "";
        kb.targetKeyBindingID=new String(targetKeyBindingID);
        return kb;
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
        return Tr.get("jjpizza.keybind.hold."+(hold?"on":"off"));
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
            search=search.toLowerCase();
            return keybind.getTranslationKey().toLowerCase().contains(search) ||
                    I18n.translate(keybind.getTranslationKey()).toLowerCase().contains(search);
        }
    }

    public static class KeyBindingEditScreen extends Screen{
        private final TargetKeybindStorage target;
        protected KeyBindingEditScreen(TargetKeybindStorage target) {
            super(Text.empty());
            this.target = target;
        }
        public void init(){
            var listWidget = new ScrollListWidget(client,width,height-24*2,0,24,22);
            rebuildList(listWidget,"");
            addDrawableChild(listWidget);
            
            var searchBox = new TextFieldWidget(client.textRenderer,2,2,width-4,20,Text.empty());
            searchBox.setChangedListener(t -> rebuildList(listWidget,t));
            addDrawableChild(searchBox);
            
            var holdModeButton = new ButtonWidget.Builder(target.getHoldText(),this::holdButton)
                    .size(width- ActionTypeRegistry.gap/2,20)
                    .position(0,height-20- ActionTypeRegistry.gap/2).build();
            addDrawableChild(holdModeButton);
        }

        private void rebuildList(ScrollListWidget listWidget, String s) {
            listWidget.children().clear();
            listWidget.setScrollY(0);
            for(TargetKeybind keybind : target.getKeyBindings()){
                if(!keybind.matches(s))
                    continue;
                var listEntry = new ScrollListWidget.ScrollListEntry();
                listWidget.addEntry(listEntry);
                
                Text buttonText = keybind.getButtonText();
                var activateButton = new ButtonWidget.Builder(buttonText,b->{
                    listEntry.setMeActive();
                    target.setTargetID(keybind.getId());
                })
                    .width(width- 6 - ActionTypeRegistry.gap)
                    .build();
                
                listEntry.addDrawableChild(activateButton,true);
                if(keybind.getId().equals(target.getTargetId()))
                    listWidget.setSelectedEntry(listEntry);
            }
        }

        private void holdButton(ButtonWidget buttonWidget) {
            target.nextHoldMode();
            buttonWidget.setMessage(target.getHoldText());
        }
    }
}
