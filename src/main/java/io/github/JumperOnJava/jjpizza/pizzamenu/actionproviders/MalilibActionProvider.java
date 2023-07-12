package io.github.JumperOnJava.jjpizza.pizzamenu.actionproviders;

import fi.dy.masa.malilib.event.InputEventHandler;
import fi.dy.masa.malilib.hotkeys.IHotkey;
import fi.dy.masa.malilib.hotkeys.KeyAction;
import fi.dy.masa.malilib.hotkeys.KeybindCategory;
import fi.dy.masa.malilib.hotkeys.KeybindMulti;
import io.github.JumperOnJava.jjpizza.pizzamenu.actionregistry.ConfigurableRunnable;
import io.github.JumperOnJava.lavajumper.common.Translation;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.*;

public class MalilibActionProvider implements ConfigurableRunnable, TargetKeybindStorage {
    public static Set<String> awaitingMatch = new HashSet<>();
    private String targetKeyBindingID = null;
    private KeyAction keyActionType = KeyAction.BOTH;

    public MalilibActionProvider(Boolean isReal) {
    }

    private IHotkey getTargetKeyBinding() {
        for (var kbgroup : InputEventHandler.getKeybindManager().getKeybindCategories()) {
            String prefix = kbgroup.getModName() + "." + kbgroup.getCategory();
            for (IHotkey key : kbgroup.getHotkeys()) {
                if(new IdentifiedHotkey(key,kbgroup).id.equals(targetKeyBindingID)){
                    return key;
                }
            }
        }
        return null;
    }


    @Override
    public Screen getConfiguratorScreen() {
        return new KeybindingActionProvider.KeyBindingEditScreen(this);
    }
    @Override
    public void run() {
        var targetKeyBinding = getTargetKeyBinding();
        if(targetKeyBinding==null)
            return;
        ((KeybindMulti)targetKeyBinding.getKeybind()).getCallback().onKeyAction(keyActionType,targetKeyBinding.getKeybind());
    }
    public List<TargetKeybind> getKeyBindings(){
        List<TargetKeybind> hotkeys = new LinkedList<>();
        for (var kbgroup : InputEventHandler.getKeybindManager().getKeybindCategories()) {
            String prefix = kbgroup.getModName() + "." + kbgroup.getCategory();
            for (IHotkey key : kbgroup.getHotkeys()) {
                hotkeys.add(new IdentifiedHotkey(key,kbgroup));
            }
        }
        return hotkeys;
    }

    @Override
    public void setTargetID(String id) {
        this.targetKeyBindingID = id;
    }

    @Override
    public String getTargetId() {
        return targetKeyBindingID;
    }

    @Override
    public Text getHoldText() {
        return Translation.get("jjpizza.keybind.mali.hold."+keyActionType);
    }

    public void nextHoldMode() {
        var values = KeyAction.values();
        keyActionType = values[(keyActionType.ordinal()+1)%values.length];
    }
    public static boolean singleCategoryInMod(KeybindCategory target) {
        int categoryCount = 0;
        for (var category : InputEventHandler.getKeybindManager().getKeybindCategories()) {
            if (category.getModName().equals(target.getModName())) {
                categoryCount++;
            }
        }
        return categoryCount == 1;
    }
    private static class IdentifiedHotkey implements TargetKeybind{
        private final KeybindCategory category;
        public final String id;
        public final IHotkey hotkey;
        public IdentifiedHotkey(IHotkey hotkey, KeybindCategory category){
            this.hotkey = hotkey;
            this.category = category;
            String prefix = category.getModName() + "." + category.getCategory();
            id = prefix + "." + hotkey.getName();
        }
        @Override
        public Text getButtonText() {
            String categoryText="";
            if(!singleCategoryInMod(category)){
                categoryText = " %s".formatted(category.getCategory());
            }
            return Text.literal(String.format("%s: %s",category.getModName(),hotkey.getName()));
        }

        @Override
        public String getId() {
            return id;
        }

        public boolean matches(String search) {
            return category.getModName().contains(search) ||
                    category.getCategory().contains(search) ||
                    hotkey.getName().contains(search);
        }
    }

}