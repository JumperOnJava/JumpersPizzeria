package io.github.JumperOnJava.jjpizza.pizzamenu.slices;

import com.google.gson.annotations.Expose;
import io.github.JumperOnJava.jjpizza.pizzamenu.PizzaManager;
import io.github.JumperOnJava.jjpizza.pizzamenu.actionregistry.ConfigurableRunnable;
import io.github.JumperOnJava.jjpizza.pizzamenu.actionproviders.NullActionProvider;
import io.github.JumperOnJava.lavajumper.common.Translation;
import io.github.JumperOnJava.lavajumper.datatypes.Angle;
import io.github.JumperOnJava.lavajumper.datatypes.CircleSlice;
import io.github.JumperOnJava.lavajumper.gui.SubScreen;
import io.github.JumperOnJava.lavajumper.gui.widgets.SliderWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;
import net.minecraft.util.math.ColorHelper;

import java.util.Random;
import java.util.function.Consumer;

import static io.github.JumperOnJava.jjpizza.pizzamenu.actionregistry.ActionTypeRegistry.gap;


public class RunnablePizzaSlice implements ConfigurablePizzaSlice {
    @Expose
    private CircleSlice circleSlice;
    @Expose
    private String name;
    @Expose
    private int color;
    @Expose
    private ConfigurableRunnable onLeftClick = new NullActionProvider(false);
    @Expose
    private ConfigurableRunnable onRightClick = new NullActionProvider(false);
    @Expose
    private Identifier icon = new Identifier("textures/item/diamond.png");

    @Override
    public PizzaManager getManager() {
        return manager;
    }
    public void setManager(PizzaManager manager){
        this.manager=manager;
    }
    private PizzaManager manager;


    public RunnablePizzaSlice(String name, CircleSlice circleSlice, PizzaManager manager){
        this.name=name;
        setSlice(circleSlice);

        var r = new Random();
        this.color = ColorHelper.Argb.getArgb(255,r.nextInt(255),r.nextInt(255),r.nextInt(255));
        //ActionTextRenderer.sendChatMessage(Integer.toHexString(this.color));
        this.manager = manager;
    }

    @Override
    public void onLeftClick() {
        MinecraftClient.getInstance().setScreen(null);
        onLeftClick.run();
    }

    @Override
    public void onRightClick() {
        MinecraftClient.getInstance().setScreen(null);
        onRightClick.run();
    }

    public Text getName(){
        return Text.literal(name);
    }
    public Identifier getIconTexture(){
        return icon;
    }
    @Override
    public CircleSlice getSlice() {
        return this.circleSlice;
    }
    public void setSlice(CircleSlice inCircleSlice){
        if(Math.abs(inCircleSlice.startAngle.getDegree() - inCircleSlice.endAngle.getDegree())<5)
            this.circleSlice =new CircleSlice(
                    inCircleSlice.startAngle.add(
                            Angle.newDegree(-5)
                    ),
                    inCircleSlice.endAngle.add(
                            Angle.newDegree(5)
                    )
            );
        else
            this.circleSlice = inCircleSlice;
    }
    public int getBackgroundColor(){
        return color;
    }

    public Screen getConfiguratorScreen(Runnable updateCallback) {
        return new RunnableConfigurator(this,updateCallback);
    }
    public static class RunnableConfigurator extends Screen{
        RunnablePizzaSlice pizzaAction;
        Runnable updateCallback;
        public RunnableConfigurator(RunnablePizzaSlice pizzaAction, Runnable updateCallback){
            super(Text.empty());
            this.pizzaAction=pizzaAction;
            this.updateCallback=updateCallback;
        }
        public void init(){
            var client = this.client;
            initDrawConfig();
            initColorConfig();
            initConfigSubScreens();
        }

        private void initConfigSubScreens() {
            var configScreen = new SubScreen();
            configScreen.init(0,gap*3+20*3,width,height-(gap*3+20*2));
            configScreen.setScreen(new ActionConfiguratorScreen(pizzaAction));
            addDrawableChild(configScreen);
        }

        private void initColorConfig() {
                var colorTexts = java.util.List.of(
                        Translation.get("jjpizza.runnable.color.alpha").setStyle(Style.EMPTY.withItalic(true)),
                        Translation.get("jjpizza.runnable.color.red").setStyle(Style.EMPTY.withColor(0xFFFF0000)),
                        Translation.get("jjpizza.runnable.color.green").setStyle(Style.EMPTY.withColor(0xFF00FF00)),
                        Translation.get("jjpizza.runnable.color.blue").setStyle(Style.EMPTY.withColor(0xFF0000FF)));
                for (int i = 0; i < 4; i++) {
                    var colorField = new SliderWidget(gap + width / 4 * i, gap*2 + 20 * 2, width / 4 - gap, 20,colorTexts.get(i),255d,((pizzaAction.color >> (3 - i) * 8) & 255),1);
                    var cons = new Consumer<Double>() {
                        int id;

                        @Override
                        public void accept(Double d) {
                            {
                                try {
                                    int[] argb = {0, 0, 0, 0};
                                    argb[0] = ColorHelper.Argb.getAlpha(pizzaAction.color);
                                    argb[1] = ColorHelper.Argb.getRed(pizzaAction.color);
                                    argb[2] = ColorHelper.Argb.getGreen(pizzaAction.color);
                                    argb[3] = ColorHelper.Argb.getBlue(pizzaAction.color);

                                    argb[id] = (int)(double)d;

                                    pizzaAction.color = ColorHelper.Argb.getArgb(argb[0], argb[1], argb[2], argb[3]);
                                    update();
                                } catch (Exception ignored) {

                                }
                            }
                            addDrawableChild(colorField);
                        }
                    };
                    cons.id = i;
                    colorField.setChangedListener(cons);
                    addDrawableChild(colorField);
                }
        }

        private void initDrawConfig() {

            var nameField = new TextFieldWidget(client.textRenderer,gap,gap, width/4-gap,16,Text.translatable("Name"));
            nameField.setText(pizzaAction.name);
            nameField.setChangedListener(s -> {
                pizzaAction.name = s;
                update();
            });
            addDrawableChild(nameField);
            var iconField = new TextFieldWidget(client.textRenderer,gap+width/4,gap, width/4*3-gap,16,Text.translatable("Name"));
            iconField.setMaxLength(Integer.MAX_VALUE);
            iconField.setText(pizzaAction.icon.toString());
            iconField.setChangedListener(s -> {
                try{
                    pizzaAction.icon = new Identifier(s);
                }
                catch (InvalidIdentifierException e){
                    iconField.setEditableColor(0xffff7057);
                }
                finally {
                    iconField.setEditableColor(0xffe0e0e0);
                }
                update();
            });
            addDrawableChild(iconField);
            var startAngleField = new SliderWidget(
                    gap,
                    gap+20,
                    width/2-gap,
                    20,
                    Translation.get("jjpizza.runnable.startangle"),
                    360,
                    Math.round(pizzaAction.getSlice().startAngle.getDegree()),5);
            startAngleField.setChangedListener(d ->{
                pizzaAction.setSlice(new CircleSlice(Angle.newDegree((float)(double)d),pizzaAction.getSlice().endAngle));
                update();
            });
            addDrawableChild(startAngleField);
            var endAngleField = new SliderWidget(
                    gap+width/2,
                    gap+20,
                    width/2-gap,
                    20,
                    Translation.get("jjpizza.runnable.endangle"),
                    360,
                    Math.round(pizzaAction.getSlice().endAngle.getDegree()),5);
            endAngleField.setChangedListener(d ->{
                pizzaAction.setSlice(new CircleSlice(pizzaAction.circleSlice.startAngle,Angle.newDegree((float)(double)d)));
                update();
            });
            addDrawableChild(endAngleField);
        }

        @Override
        public void render(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
            renderBackground(matrixStack);
            super.render(matrixStack, mouseX, mouseY, delta);
        }
        private void update(){
            updateCallback.run();
        }
        static class ActionConfiguratorScreen extends Screen {
            final RunnablePizzaSlice pizzaAction;
            protected ActionConfiguratorScreen(RunnablePizzaSlice pizzaAction) {
                super(Text.empty());
                this.pizzaAction=pizzaAction;
            }
            @Override
            protected void init() {

                var leftAction = new SubScreen();
                leftAction.init(gap / 2, gap / 2 + 20, this.width / 2 - gap / 2, this.height - gap / 2 - 40);
                leftAction.setScreen(pizzaAction.onLeftClick.getConfiguratorScreen());
                addDrawableChild(leftAction);


                var leftCycleButton = new ButtonWidget.Builder(Text.empty(),button -> {
                    pizzaAction.onLeftClick = pizzaAction.manager.actionTypeRegistry.getNextFactoryForType(pizzaAction.onLeftClick).apply(true);
                    setButtonType(button,pizzaAction.onLeftClick);
                    leftAction.setScreen(pizzaAction.onLeftClick.getConfiguratorScreen());
                    pizzaAction.onLeftClick.setParent(pizzaAction);
                }).size(width/2-gap/2,20).position(gap/2,gap/2).build();

                setButtonType(leftCycleButton,pizzaAction.onLeftClick);
                leftAction.setScreen(pizzaAction.onLeftClick.getConfiguratorScreen());

                addDrawableChild(leftCycleButton);

                ////////////////////////////////////

                var rightAction = new SubScreen();
                rightAction.init(this.width / 2 + gap / 2, gap / 2 + 20, this.width / 2 - gap / 2, this.height - gap / 2 - 40);
                rightAction.setScreen(pizzaAction.onLeftClick.getConfiguratorScreen());
                addDrawableChild(rightAction);


                var rightCycleButton = new ButtonWidget.Builder(Text.empty(),button -> {
                    pizzaAction.onRightClick = pizzaAction.manager.actionTypeRegistry.getNextFactoryForType(pizzaAction.onRightClick).apply(true);
                    setButtonType(button,pizzaAction.onRightClick);
                    rightAction.setScreen(pizzaAction.onRightClick.getConfiguratorScreen());
                    pizzaAction.onRightClick.setParent(pizzaAction);
                }).size(width/2-gap/2,20).position(width / 2 + gap/2,gap/2).build();

                setButtonType(rightCycleButton,pizzaAction.onRightClick);
                rightAction.setScreen(pizzaAction.onRightClick.getConfiguratorScreen());

                addDrawableChild(rightCycleButton);
            }
        }
        public static void setButtonType(ButtonWidget button, ConfigurableRunnable obj){
            button.setMessage(Translation.get("jjpizza.actions."+obj.getClass().getSimpleName()));
        }
    }
}
