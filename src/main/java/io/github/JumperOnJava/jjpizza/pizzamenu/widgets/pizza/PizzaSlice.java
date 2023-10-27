package io.github.JumperOnJava.jjpizza.pizzamenu.widgets.pizza;

import io.github.JumperOnJava.lavajumper.datatypes.CircleSlice;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;

public interface PizzaSlice {
    default void onRightClick(){}
    default void onLeftClick(){}
    default void onScroll(double scrollX,double scrollY){}
    default int getBackgroundColor(){return ColorHelper.Argb.getArgb(255,0,0,0);}
    /**
     * Icon texture identifier. Can return null to not display anything
     * @return
     */
    default Identifier getIconTexture(){return null;}
    default Text getName(){return Text.empty();}

    /**
     * Piece of circle that this slice represents
     * @return
     */
    CircleSlice getSlice();

}
