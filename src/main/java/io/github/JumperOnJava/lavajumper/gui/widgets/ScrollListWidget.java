package io.github.JumperOnJava.lavajumper.gui.widgets;

import com.google.common.collect.Lists;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * Scroll list widget for general use.
 */
public class ScrollListWidget extends AlwaysSelectedEntryListWidget<ScrollListWidget.ScrollListEntry> {
    public ScrollListWidget(MinecraftClient client, int width, int height, int x, int y, int itemHeight) {
        super(client,width,height,y,height,itemHeight);
        setLeftPos(x);
        //setRenderBackground(false);
        //setRenderHeader(false,0);
    }
    @Override
    public int getRowWidth() {
        return this.width;
    }
    public int addEntry(ScrollListEntry entry){
        entry.activationConsumer = this::setSelectedEntry;
        entry.isHoveredFunction = this::isMouseOver;
        return super.addEntry(entry);
    }
    @Override
    protected int getScrollbarPositionX() {
        return width-6;
    }
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return super.mouseClicked(mouseX, mouseY, button);
    }
    private ScrollListEntry selectedEntry=new ScrollListEntry();
    public void setSelectedEntry(ScrollListEntry listEntry) {
        selectedEntry.setSelected(false);
        listEntry.setSelected(true);
        selectedEntry=listEntry;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.enableScissor(left,top,left+width,top+height-1);
        super.render(context, mouseX, mouseY, delta);
        context.disableScissor();
    }

    /**
     * Scroll list entry. Out of box does nothing but using addDrawableChild method you can add widgets for custom behaviour.
     */
    public static class ScrollListEntry extends AlwaysSelectedEntryListWidget.Entry<ScrollListEntry> {
        private final List<Drawable> drawables = Lists.newArrayList();
        private final List<Element> children = Lists.newArrayList();
        private boolean isSelected = false;
        private Consumer<ScrollListEntry> activationConsumer;
        private BiFunction<Integer,Integer,Boolean> isHoveredFunction;
        private List<Element> deactivate = Lists.newArrayList();

        @Override
        public Text getNarration() {
            return Text.empty();
        }
        int currentX, currentY;
        private void setSelected(boolean selected) {
            this.isSelected = selected;
            for (var d : deactivate) {
                if (d instanceof PressableWidget pw) {
                    pw.active = !isSelected;
                }
            }
        }
        @Override
        public void render(DrawContext context,
                           int index,
                           int y, int x,
                           int entryWidth,
                           int entryHeight,
                           int mouseX, int mouseY,
                           boolean hovered,
                           float delta) {
            for (var d : drawables) {
                context.getMatrices().push();
                context.getMatrices().translate(x, y, 0);
                if(!isHoveredFunction.apply(mouseX,mouseY)){
                    mouseX+=100000;
                    mouseY+=100000;
                }
                d.render(context, mouseX - x, mouseY - y, delta);
                currentX = x;
                currentY = y;
                context.getMatrices().pop();
            }
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (!isMouseOver(mouseX, mouseY))
                return false;
            for (var c : children) {
                c.mouseClicked(mouseX - currentX, mouseY - currentY, button);
            }
            return false;
        }

        @Override
        public boolean isMouseOver(double mouseX, double mouseY) {
            return super.isMouseOver(mouseX, mouseY) && isHoveredFunction.apply((int) mouseX, (int) mouseY);
        }

        /**
         * adds widget element to this entry.
         * @param drawableElement
         * @param deactivateOnSelect should be this element deactivated when selected. Works only when widget is instance of PressableWidget
         * @return
         * @param <T>
         */
        public <T extends Element & Drawable> T addDrawableChild(T drawableElement, boolean deactivateOnSelect) {
            this.drawables.add(drawableElement);
            this.children.add(drawableElement);
            if (deactivateOnSelect)
                this.deactivate.add(drawableElement);
            return drawableElement;
        }

        public void setMeActive() {
            if (activationConsumer == null)
                return;
            activationConsumer.accept(this);
        }
    }
}