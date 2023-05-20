package io.github.JumperOnJava.jjpizza.pizzamenu;

import io.github.javajumper.lavajumper.common.Translation;
import io.github.javajumper.lavajumper.gui.widgets.PizzaWidget;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class PizzaScreen extends Screen {
    private final PizzaWidget.PizzaConfiguration slices;
    private final Screen configuratorScreen;
    public PizzaWidget pizzaWidget;
    //ImmutableSlicesList slices;
    public PizzaScreen(PizzaWidget.PizzaConfiguration slices, Screen configuratorScreen) {
        super(Text.empty());
        this.slices = slices;
        this.configuratorScreen = configuratorScreen;
    }
    public void init(){
        pizzaWidget = new PizzaWidget();
        pizzaWidget.setupSize((int) (Math.min(width,height)/2f*.8f),(int) (Math.min(width,height)/2f*.8f/8),width/2,height/2);
        pizzaWidget.setupSlices(slices);
        addDrawableChild(pizzaWidget);
        if(configuratorScreen!=null)
        addDrawableChild(new ButtonWidget.Builder(Translation.get("jjpizza.screen.openconfig"),b->{
            MinecraftClient.getInstance().setScreen(configuratorScreen);
        }).position(10,10).size(60,20).build());
        if(FabricLoader.getInstance().isDevelopmentEnvironment()){
            addDrawableChild(new ButtonWidget.Builder(Text.literal("Export Translaslation"),b->Translation.generateTranlationMap()).position(10,32).width(120).build());
            addDrawableChild(new ButtonWidget.Builder(Text.literal("Force load"),b->{
                //PizzaManager.getManager().actions.clear();
                PizzaManager.getManager().actions=PizzaManager.getManager().load();
            }).position(10,54).width(70).build());

        }
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
        renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, delta);
    }
}
