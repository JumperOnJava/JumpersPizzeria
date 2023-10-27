package io.github.JumperOnJava.jjpizza.pizzamenu.slices.runnable;

import io.github.JumperOnJava.jjpizza.datatypes.Angle;
import io.github.JumperOnJava.jjpizza.datatypes.CircleSlice;
import io.github.JumperOnJava.jjpizza.pizzamenu.configurer.TextureListAsk;
import io.github.JumperOnJava.jjpizza.pizzamenu.slices.runnable.actionregistry.ConfigurableRunnable;
import io.github.JumperOnJava.lavajumper.common.Translation;
import io.github.JumperOnJava.lavajumper.gui.AskScreen;
import io.github.JumperOnJava.lavajumper.gui.widgets.SubScreen;
import io.github.JumperOnJava.lavajumper.gui.widgets.SliderWidget;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;
import net.minecraft.util.math.ColorHelper;

import java.util.function.Consumer;

import static io.github.JumperOnJava.jjpizza.pizzamenu.slices.runnable.actionregistry.ActionTypeRegistry.gap;

public class RunnableScreen extends Screen {
    RunnableSlice pizzaAction;
    Runnable updateCallback;

    public RunnableScreen(RunnableSlice pizzaAction, Runnable updateCallback) {
        super(Text.empty());
        this.pizzaAction = pizzaAction;
        this.updateCallback = updateCallback;
    }

    public void init() {
        var client = this.client;
        initDrawConfig();
        initColorConfig();
        initConfigSubScreens();
    }

    private void initConfigSubScreens() {
        var configScreen = new SubScreen(0, gap * 3 + 20 * 3, width, height - (gap * 3 + 20 * 2));
        configScreen.setScreen(new ActionEditScreen(pizzaAction));
        addDrawableChild(configScreen);
    }

    private void initColorConfig() {
        var colorTexts = java.util.List.of(
                Translation.get("jjpizza.runnable.color.alpha").setStyle(Style.EMPTY.withItalic(true)),
                Translation.get("jjpizza.runnable.color.red").setStyle(Style.EMPTY.withColor(0xFFFF0000)),
                Translation.get("jjpizza.runnable.color.green").setStyle(Style.EMPTY.withColor(0xFF00FF00)),
                Translation.get("jjpizza.runnable.color.blue").setStyle(Style.EMPTY.withColor(0xFF0000FF)));
        for (int i = 0; i < 4; i++) {
            var colorField = new SliderWidget(gap + width / 4 * i, gap * 2 + 20 * 2, width / 4 - gap, 20, colorTexts.get(i), 255d, ((pizzaAction.color >> (3 - i) * 8) & 255), 1);
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

                            argb[id] = (int) (double) d;

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

        var nameField = new TextFieldWidget(client.textRenderer, gap, gap, width / 4 - gap, 16, Text.translatable("Name"));
        nameField.setText(pizzaAction.name);
        nameField.setChangedListener(s -> {
            pizzaAction.name = s;
            update();
        });
        addDrawableChild(nameField);
        var iconField = new TextFieldWidget(client.textRenderer, gap + width / 4, gap, width / 4 * 2 - gap * 2, 16, Text.translatable("Name"));
        iconField.setMaxLength(Integer.MAX_VALUE);
        iconField.setText(pizzaAction.icon.toString());
        iconField.setChangedListener(s -> {
            try {
                pizzaAction.icon = new Identifier(s);
            } catch (InvalidIdentifierException e) {
                iconField.setEditableColor(0xffff7057);
            } finally {
                iconField.setEditableColor(0xffe0e0e0);
            }
            update();
        });
        addDrawableChild(iconField);
        var iconSelectField = new ButtonWidget.Builder(
                Translation.get("jjpizza.runnable.iconselect"), b -> {
            AskScreen.ask(
                    new TextureListAsk.Builder()
                            .onSuccess(i->iconField.setText(i.toString()))
                            .onFail(()->{})
                            .build()
            );
        }
        ).position(width / 4 * 3, gap / 2).size(width / 4, 20).build();
        addDrawableChild(iconSelectField);

        var startAngleField = new SliderWidget(
                gap,
                gap + 20,
                width / 2 - gap,
                20,
                Translation.get("jjpizza.runnable.startangle"),
                360,
                Math.round(pizzaAction.getSlice().startAngle.getDegree()), 5);
        startAngleField.setChangedListener(d -> {
            pizzaAction.setSlice(new CircleSlice(Angle.newDegree((float) (double) d), pizzaAction.getSlice().endAngle));
            update();
        });
        addDrawableChild(startAngleField);
        var endAngleField = new SliderWidget(
                gap + width / 2,
                gap + 20,
                width / 2 - gap,
                20,
                Translation.get("jjpizza.runnable.endangle"),
                360,
                Math.round(pizzaAction.getSlice().endAngle.getDegree()), 5);
        endAngleField.setChangedListener(d -> {
            pizzaAction.setSlice(new CircleSlice(pizzaAction.circleSlice.startAngle, Angle.newDegree((float) (double) d)));
            update();
        });
        addDrawableChild(endAngleField);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
    }

    private void update() {
        updateCallback.run();
    }

    public static void setButtonType(ButtonWidget button, ConfigurableRunnable obj) {
        button.setMessage(Translation.get("jjpizza.actions." + obj.getClass().getSimpleName()));
    }
}
