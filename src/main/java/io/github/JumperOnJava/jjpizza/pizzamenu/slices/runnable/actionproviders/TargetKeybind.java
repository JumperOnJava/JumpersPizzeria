package io.github.JumperOnJava.jjpizza.pizzamenu.slices.runnable.actionproviders;

import net.minecraft.text.Text;

public interface TargetKeybind {
    Text getButtonText();
    String getId();
    boolean matches(String search);
}
