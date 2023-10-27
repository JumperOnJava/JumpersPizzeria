package io.github.JumperOnJava.lavajumper.gui.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.navigation.GuiNavigation;
import net.minecraft.client.gui.navigation.GuiNavigationPath;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Widget for rendering screens in screens.
 * Use init method so set widget dimensions and setScreen method to set/change screen in widget.
 * When screen is not set up or equals null widget will display empty screen with random color and "nullSubScreen" text in center
 */
public class SubScreen implements Drawable, ParentElement, Selectable, Widget {
    private Screen screen;
    private int x,y,width,height;
    public SubScreen(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * If screen paremeter equals null widget will display empty screen with random color and "nullSubScreen" text in center
     * @param screen
     */
    public SubScreen setScreen(Screen screen) {
        if(screen==null)
            screen=new NullSubScreen();
        this.screen = screen;
        screen.init(MinecraftClient.getInstance(),width,height);
        return this;
    }


    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.getMatrices().push();
        context.getMatrices().translate(x,y,0);
        screen.render(context, mouseX-x, mouseY-y, delta);
        context.getMatrices().pop();
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        screen.mouseMoved(mouseX-x, mouseY-y);
    }

    @Override
    public List<? extends Element> children() {
        return screen.children();
    }

    @Override
    public Optional<Element> hoveredElement(double mouseX, double mouseY) {
        return ParentElement.super.hoveredElement(mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return screen.mouseClicked(mouseX-x, mouseY-y, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return screen.mouseReleased(mouseX-x, mouseY-y, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        return screen.mouseDragged(mouseX-x, mouseY-y, button, deltaX, deltaY);
    }

    @Override
    public boolean isDragging() {
        return false;
    }

    @Override
    public void setDragging(boolean dragging) {

    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        return screen.mouseScrolled(mouseX-x, mouseY-y, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return screen.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        return screen.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        return screen.charTyped(chr, modifiers);
    }

    @Nullable
    @Override
    public Element getFocused() {
        return screen.getFocused();
    }

    @Override
    public void setFocused(@Nullable Element focused) {
        screen.setFocused(focused);
    }

    @Override
    public void setFocused(boolean focused) {
        ParentElement.super.setFocused(focused);
    }

    @Override
    public boolean isFocused() {
        return ParentElement.super.isFocused();
    }

    @Nullable
    @Override
    public GuiNavigationPath getFocusedPath() {
        return ParentElement.super.getFocusedPath();
    }

    @Override
    public ScreenRect getNavigationFocus() {
        return ParentElement.super.getNavigationFocus();
    }

    @Override
    public void setPosition(int x, int y) {
        Widget.super.setPosition(x, y);
    }


    @Override
    public void focusOn(@Nullable Element element) {
        ParentElement.super.focusOn(element);
    }

    @Nullable
    @Override
    public GuiNavigationPath getNavigationPath(GuiNavigation navigation) {
        return ParentElement.super.getNavigationPath(navigation);
    }


    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return mouseX >= this.x &&
               mouseY >= this.y &&
               mouseX < (this.x + this.width) &&
               mouseY < (this.y + this.height);
    }

    @Override
    public SelectionType getType() {
        return SelectionType.NONE;
    }

    @Override
    public boolean isNarratable() {
        return Selectable.super.isNarratable();
    }


    @Override
    public void setX(int x) {
        this.x=x;
    }

    @Override
    public void setY(int y) {
        this.y=y;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void forEachChild(Consumer<ClickableWidget> consumer) {

    }

    @Override
    public int getNavigationOrder() {
        return ParentElement.super.getNavigationOrder();
    }

    private class NullSubScreen extends Screen {
        public NullSubScreen() {
            super(Text.empty());
        }

        @Override
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            super.render(context, mouseX, mouseY, delta);
            RenderSystem.enableBlend();
            context.fill(0,0,width,height,(int)(Math.pow(x + width + y + height,5f)%Integer.MAX_VALUE)&0x00FFFFFF|0x3F000000);
            context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, "nullSubScreen",width/2,height/2,(int)(Math.pow(x + width + y + height,5f)%Integer.MAX_VALUE)&0x00FFFFFF|0x3F000000^0x00FFFFFF);
        }
    }
}
