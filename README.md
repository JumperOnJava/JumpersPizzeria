# Jumper's pizzeria!

This mod adds pizza menu on which you can add different actions. 
Currently supported actions are keybinds and chat messages/commands

Mod also adds small api that allows you add new action types

To add your action type you also need to register it like this
```java
PizzeriaApi.getRegistry().addActionType
(
    Function<Boolean,? extends ConfigurableRunnable> factory,
    TypeAdapter<? extends ConfigurableRunnable> typeAdapter
    //you can pass null into typeAdapter field so gson will
    //use default TypeAdapter
);
```
Your action should implement ConfigurableRunnable interface and contain constructor with one boolean parameter.

Example:
```java
public class ChatMessageActionProvider implements ConfigurableRunnable {
    @Expose
    private String message="Hello, world!";
    
    
    public ChatMessageActionProvider(Boolean isReal) {
        if(!isReal)
            return; 
        //Api may create extra objects for internal use,
        //so when it happens isReal parameter will be false
        //in this case you can safely return from constuctor
    }
    //you can recieve parent pizza slice, but you can ignore it in case
    //you don't need it
    public void setParent(ConfigurablePizzaSlice pizzaSlice) {
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
            field.setText(target.message);
            field.setChangedListener(s->target.message=s);
            addDrawableChild(field);
        }
    }
```