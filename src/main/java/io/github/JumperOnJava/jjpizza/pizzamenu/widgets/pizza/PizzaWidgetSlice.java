package io.github.JumperOnJava.jjpizza.pizzamenu.widgets.pizza;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.JumperOnJava.jjpizza.datatypes.Angle;
import io.github.JumperOnJava.jjpizza.datatypes.CircleSlice;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec2f;

import static java.lang.Math.*;

public class PizzaWidgetSlice implements Drawable, Element, Selectable {
    public final PizzaSlice pizzaSlice;
    private final CircleSlice circleSlice;
    private final PizzaWidget parent;
    private final HoverManager hoverManager;

    public PizzaWidgetSlice(PizzaSlice slice, PizzaWidget parent){
        this.pizzaSlice = slice;
        this.circleSlice =pizzaSlice.getSlice();
        this.parent = parent;
        hoverManager = new HoverManager(4);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {

        hoverManager.tickHover(isMouseOver(mouseX,mouseY),delta);

        context.getMatrices().push();

        Tessellator tessellator = Tessellator.getInstance();

        RenderSystem.enableBlend();;
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();

        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.TRIANGLE_STRIP, VertexFormats.POSITION_COLOR);

        var peekMatrix = context.getMatrices().peek().getPositionMatrix();

        float res = (float) (PI/60);

        translateForward(context.getMatrices());

        if(circleSlice.endAngle.getRadian()!= circleSlice.startAngle.getRadian()) {
            for (var a = circleSlice.startAngle.getRadian();
                 a < circleSlice.endAngle.getRadian()+(circleSlice.startAngle.getRadian()> circleSlice.endAngle.getRadian() ? (2*PI) : 0);
                 a += res) {
                bufferBuilder.vertex(peekMatrix, (float) (-cos(a) * parent.radius), (float) (-sin(a) * parent.radius), 0f).color(pizzaSlice.getBackgroundColor()).next();
                bufferBuilder.vertex(peekMatrix, (float) (-cos(a) * parent.innerRadius), (float) (-sin(a) * parent.innerRadius), 0f).color(pizzaSlice.getBackgroundColor()).next();
            }
            bufferBuilder.vertex(peekMatrix, (float) (-cos(circleSlice.endAngle.getRadian()) * parent.radius), (float) (-sin(circleSlice.endAngle.getRadian()) * parent.radius), 0f).color(pizzaSlice.getBackgroundColor()).next();
            bufferBuilder.vertex(peekMatrix, (float) (-cos(circleSlice.endAngle.getRadian()) * parent.innerRadius), (float) (-sin(circleSlice.endAngle.getRadian()) * parent.innerRadius), 0f).color(pizzaSlice.getBackgroundColor()).next();
        }
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        RenderSystem.setShaderColor(1f,1f,1f,1f);

        tessellator.draw();

        RenderSystem.enableCull();
        RenderSystem.disableBlend();


        context.getMatrices().pop();
    }
    public void renderIcons(DrawContext context){
        context.getMatrices().push();
        translateForward(context.getMatrices());
        if(pizzaSlice.getIconTexture()==null) {
            context.getMatrices().pop();
            return;
        }
        context.drawTexture(pizzaSlice.getIconTexture(),(int) (getRenderPos().x-16), (int) (getRenderPos().y-16),0,0,32,32,32,32);
        context.getMatrices().pop();
    }
    public void renderText(DrawContext context) {
        context.getMatrices().push();
        translateForward(context.getMatrices());
        context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, pizzaSlice.getName(), (int) getRenderPos().x, (int) (getRenderPos().y + 18), 0xFFFFFFFF);
        context.getMatrices().pop();
    }

    private void translateForward(MatrixStack matrixStack) {
        var forward = smoothFunc(hoverManager.getHoverProgress())* parent.radius/30;
        matrixStack.translate(-cos(circleSlice.getMidAngle().getRadian())*forward,-sin(circleSlice.getMidAngle().getRadian())*forward,0);
    }
    private Vec2f getRenderPos(){
        var mid = pizzaSlice.getSlice().getMidAngle().getRadian();
        return new Vec2f((float) -cos(mid), (float) -sin(mid)).multiply(parent.radius*0.75f);
    }
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        var cond = isMouseOver(mouseX,mouseY);
        if(cond){
            switch (button)
            {
                case 0 -> pizzaSlice.onLeftClick();
                case 1 -> pizzaSlice.onRightClick();
            }

        }
        return cond;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        var cond = isMouseOver(mouseX,mouseY);
        if(cond){
            pizzaSlice.onScroll(horizontalAmount,verticalAmount);
        }
        return cond;
    }
    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        float mouseAngle = (float) Math.atan2(-(mouseY/2),-(mouseX/2));
        if(mouseAngle<0)
            mouseAngle+= PI*2;


        return circleSlice.inInSlice(Angle.newRadian(mouseAngle)) && parent.isMouseOverRel(mouseX,mouseY);
    }

    @Override
    public void setFocused(boolean focused) {

    }

    @Override
    public boolean isFocused() {
        return false;
    }

    @Override
    public SelectionType getType() {
        return SelectionType.HOVERED;
    }

    @Override
    public boolean isNarratable() {
        return Selectable.super.isNarratable();
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {
        //builder.put(NarrationPart.TITLE, "slice narration is not implemented");
    }
    private float smoothFunc(float x){
        return x*x*(3f-2f*x);
    }
}
