package io.github.JumperOnJava.jjpizza.pizzamenu.slices.runnable;

import io.github.JumperOnJava.lavajumper.gui.SubScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import static io.github.JumperOnJava.jjpizza.pizzamenu.slices.runnable.actionregistry.ActionTypeRegistry.gap;

class ActionEditScreen extends Screen {
    final RunnableSlice pizzaAction;

    protected ActionEditScreen(RunnableSlice pizzaAction) {
        super(Text.empty());
        this.pizzaAction = pizzaAction;
    }

    @Override
    protected void init() {

        var leftAction = new SubScreen();
        leftAction.init(gap / 2, gap / 2 + 20, this.width / 2 - gap / 2, this.height - gap / 2 - 40);
        leftAction.setScreen(pizzaAction.onLeftClick.getConfiguratorScreen());
        addDrawableChild(leftAction);


        var leftCycleButton = new ButtonWidget.Builder(Text.empty(), button -> {
            pizzaAction.onLeftClick = pizzaAction.manager.actionTypeRegistry.getNextFactoryForType(pizzaAction.onLeftClick).apply(true);
            RunnableScreen.setButtonType(button, pizzaAction.onLeftClick);
            leftAction.setScreen(pizzaAction.onLeftClick.getConfiguratorScreen());
            pizzaAction.onLeftClick.setParent(pizzaAction);
        }).size(width / 2 - gap / 2, 20).position(gap / 2, gap / 2).build();

        RunnableScreen.setButtonType(leftCycleButton, pizzaAction.onLeftClick);
        leftAction.setScreen(pizzaAction.onLeftClick.getConfiguratorScreen());

        addDrawableChild(leftCycleButton);

        ////////////////////////////////////

        var rightAction = new SubScreen();
        rightAction.init(this.width / 2 + gap / 2, gap / 2 + 20, this.width / 2 - gap / 2, this.height - gap / 2 - 40);
        rightAction.setScreen(pizzaAction.onLeftClick.getConfiguratorScreen());
        addDrawableChild(rightAction);


        var rightCycleButton = new ButtonWidget.Builder(Text.empty(), button -> {
            pizzaAction.onRightClick = pizzaAction.manager.actionTypeRegistry.getNextFactoryForType(pizzaAction.onRightClick).apply(true);
            RunnableScreen.setButtonType(button, pizzaAction.onRightClick);
            rightAction.setScreen(pizzaAction.onRightClick.getConfiguratorScreen());
            pizzaAction.onRightClick.setParent(pizzaAction);
        }).size(width / 2 - gap / 2, 20).position(width / 2 + gap / 2, gap / 2).build();

        RunnableScreen.setButtonType(rightCycleButton, pizzaAction.onRightClick);
        rightAction.setScreen(pizzaAction.onRightClick.getConfiguratorScreen());

        addDrawableChild(rightCycleButton);
    }
}
