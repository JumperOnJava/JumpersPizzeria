package io.github.JumperOnJava.lavajumper.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.loader.impl.lib.sat4j.core.Vec;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.Random;

public class GuiHelper {
    public static Vec3d transformCoords(MatrixStack matrixStack,Vec3d vec3d){
        return transformCoords(matrixStack,vec3d.x,vec3d.y,vec3d.z);
    }
    public static Vec3d transformCoords(MatrixStack matrixStack,double x,double y, double z){
        var tv = matrixStack.peek().getPositionMatrix().transform(new Vector4f((float) x, (float) y, (float) z,1));
        return new Vec3d(new Vector3f(tv.x,tv.y,tv.z));
    }
    public static Vec3d transformCoords(MatrixStack matrixStack,double x,double y){
        return transformCoords(matrixStack,x,y,0);
    }
    public static void renderSides(DrawContext context, int x, int y, int width, int height)
    {
        y-=1;
        height+=1;
        context.fill( x-7, y-7, x + width+7, y + height+7, 0xFF000000);
        context.fill( x-6, y-6, x + width+6, y + height+6, 0xFFC6C6C6);
        context.fill( x-6, y-6, x + width+4, y + height+4, 0xFFFFFFFF);
        context.fill( x-4, y-4, x + width+6, y + height+6, 0xFF555555);
        context.fill( x-4, y-4, x + width+4, y + height+4, 0xFFC6C6C6);
        /*RenderSystem.enableBlend();
        fill(matrices,x,y,x+width,y+height,0x3F000000 | (int)(Math.pow(x + width + y + height,5f)%Integer.MAX_VALUE));*/
    }
    public static TestScreen TestScreen(int color){
        return new TestScreen(color);
    }
    public static class TestScreen extends Screen {
        int color;
        public TestScreen(int color) {
            super(Text.empty());
            this.color=color;
        }
        @Override
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            super.render(context, mouseX, mouseY, delta);
            context.fill(0,0,width,height,color);
        }
    }
}
