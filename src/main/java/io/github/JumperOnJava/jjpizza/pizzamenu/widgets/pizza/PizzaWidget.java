package io.github.JumperOnJava.jjpizza.pizzamenu.widgets.pizza;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec2f;

import java.util.LinkedList;
import java.util.List;

/**
 * Pizza menu widget
 * Should be set up by using setupSize and setupSlices methods to work properly.
 */
public class PizzaWidget implements Drawable, Element, Selectable {
    private final LinkedList<PizzaWidgetSlice> slices = new LinkedList<>();
    public float radius;
    public float innerRadius;
    public int x,y;

    /**
     * Creates Pizza widget for slice list
     * @param radius Radius of pizza
     * @param x x coordinate of pizza center
     * @param y y coordinate of pizza center
     */
    public PizzaWidget setupSize(int radius,int innerRadius, int x, int y) {
        this.radius=radius;
        this.innerRadius=innerRadius;
        this.x=x;
        this.y=y;
        return this;
    }
    public PizzaWidget setupSlices(List<? extends PizzaSlice> slices){
        this.slices.clear();
        for(var slice : slices){
            this.slices.add(new PizzaWidgetSlice(slice,this));
        }
        return this;
    }
    public void render(DrawContext context, int mouseX, int mouseY, float delta){
        context.getMatrices().push();
        //ActionTextRenderer.sendChatMessag*e("x: ",mouseX," y: ",mouseY);
        var prof = MinecraftClient.getInstance().getProfiler();
        prof.push("Pizza");
        context.getMatrices().translate(x, y,0);
        slices.forEach(slice->{
            slice.render(context,mouseX-x,mouseY-y,delta);
        });
        slices.forEach(slice->{
            slice.renderText(context);
        });
        slices.forEach(slice->{
            slice.renderIcons(context);
        });
        prof.pop();
        context.getMatrices().pop();

    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean cond = false;
        for(var slice:new LinkedList<>(slices)){
            cond = cond || slice.mouseClicked(mouseX-x,mouseY-y,button);
        }
        return cond;
    }
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amountX, double amountY) {
        boolean cond = false;

        for(var slice:new LinkedList<>(slices)  ){
            cond = cond || slice.mouseScrolled(mouseX-x,mouseY-y,amountX,amountY);
        }
        return cond;
    }
    @Override
    public SelectionType getType() {
        return SelectionType.HOVERED;
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        var center = new Vec2f(x,y);
        var mouse = new Vec2f((float) mouseX, (float) mouseY);
        var distanceSq = center.distanceSquared(mouse);
        return distanceSq < radius*radius && distanceSq > innerRadius*innerRadius;
    }

    @Override
    public void setFocused(boolean focused) {

    }

    @Override
    public boolean isFocused() {
        return false;
    }

    public boolean isMouseOverRel(double mouseX, double mouseY){
        return isMouseOver(mouseX+x,mouseY+y);
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {

    }
}
