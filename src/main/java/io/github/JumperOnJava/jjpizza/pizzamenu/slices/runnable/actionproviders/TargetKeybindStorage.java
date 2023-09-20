package io.github.JumperOnJava.jjpizza.pizzamenu.slices.runnable.actionproviders;

import net.minecraft.text.Text;

import java.util.Collection;

public interface TargetKeybindStorage {
    Collection<TargetKeybind> getKeyBindings();
    void setTargetID(String id);

    String getTargetId();

    Text getHoldText();

    void nextHoldMode();
}
