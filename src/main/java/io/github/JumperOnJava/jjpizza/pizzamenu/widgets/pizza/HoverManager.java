package io.github.JumperOnJava.jjpizza.pizzamenu.widgets.pizza;

import net.minecraft.util.math.MathHelper;

/**
 * Counts hover time for elements that should smoothly react on hovering
 * Example: Pizza widget slice
 * tickHover should be updated every frame
 */
public class HoverManager {
    public final float maxHoverTime;
    public float hoverTime;
    public HoverManager(float maxHoverTime){
        this.maxHoverTime=maxHoverTime;
    }

    /**
     * should be called in render method of screen/drawable element.
     * @param isMouseHovered
     * @param delta
     */
    public void tickHover(boolean isMouseHovered,float delta){
        hoverTime+=delta * (isMouseHovered?1:-2);
        hoverTime= MathHelper.clamp(hoverTime,0,maxHoverTime);
    }

    /**
     * Returns hover progress in range from 0 (not hovered) to 1 (hovered over element for [maxHoverTime] ticks)
     * @return
     */
    public float getHoverProgress(){
        return Math.min(hoverTime/maxHoverTime,1);
    }
}
