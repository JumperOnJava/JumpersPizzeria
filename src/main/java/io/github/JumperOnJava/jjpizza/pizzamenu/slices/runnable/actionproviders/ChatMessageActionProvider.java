package io.github.JumperOnJava.jjpizza.pizzamenu.slices.runnable.actionproviders;

import io.github.JumperOnJava.jjpizza.pizzamenu.slices.runnable.actionregistry.ConfigurableRunnable;
import io.github.JumperOnJava.jjpizza.pizzamenu.slices.ConfigurablePizzaSlice;
import io.github.JumperOnJava.lavajumper.common.Translation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

import static io.github.JumperOnJava.jjpizza.pizzamenu.slices.runnable.actionregistry.ActionTypeRegistry.gap;


public class ChatMessageActionProvider implements ConfigurableRunnable {
    private String message="Hello, world!";
    private transient ConfigurablePizzaSlice parent;

    public ChatMessageActionProvider(Boolean isReal) {
    }

    public void setParent(ConfigurablePizzaSlice pizzaSlice) {
        this.parent = pizzaSlice;
    }

    @Override
    public Screen getConfiguratorScreen() {
        return new ChatMessageEditScreen(this);
    }

    @Override
    public void run() {
        var n = MinecraftClient.getInstance().getNetworkHandler();
        if(message.startsWith("/"))
            n.sendChatCommand(message.substring(1));
        else
            n.sendChatMessage(message);

    }
    static class ChatMessageEditScreen extends Screen{
        final private ChatMessageActionProvider target;
        protected ChatMessageEditScreen(ChatMessageActionProvider target) {
            super(Text.empty());
            this.target=target;
        }
        protected void init(){
            var field = new TextFieldWidget(MinecraftClient.getInstance().textRenderer,gap/2,gap/2,width-gap,20, Translation.get("jjpizza.chat.messagehere"));
            field.setMaxLength(255);
            field.setText(target.message);
            field.setChangedListener(s->target.message=s);
            addDrawableChild(field);
        }
    }
}
