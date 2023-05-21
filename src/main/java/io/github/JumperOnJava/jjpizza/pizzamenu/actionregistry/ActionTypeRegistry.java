package io.github.JumperOnJava.jjpizza.pizzamenu.actionregistry;

import com.google.gson.*;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import io.github.JumperOnJava.jjpizza.pizzamenu.PizzaManager;
import io.github.JumperOnJava.jjpizza.pizzamenu.Registry;
import io.github.JumperOnJava.jjpizza.pizzamenu.actionproviders.ChatMessageActionProvider;
import io.github.JumperOnJava.jjpizza.pizzamenu.actionproviders.KeybindingActionProvider;
import io.github.JumperOnJava.jjpizza.pizzamenu.actionproviders.NullActionProvider;
import io.github.JumperOnJava.lavajumper.LavaJumper;

import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Function;

public class ActionTypeRegistry extends Registry<TypeInfo> {
    public static final int gap = 4;
    public ActionTypeRegistry(){
        typeFactories = new TreeSet<>();
        addActionType(KeybindingActionProvider::new,null);
        addActionType(NullActionProvider::new,null);
        addActionType(ChatMessageActionProvider::new,null);
    }
    private final Set<TypeInfo> typeFactories;
    private List<TypeInfo> asList(){
        return new ArrayList<>(typeFactories);
    }
    public void addActionType(Function<Boolean, ConfigurableRunnable> factory){
        typeFactories.add(new TypeInfo(factory,null));
    }
    public void addActionType(Function<Boolean,ConfigurableRunnable> factory, TypeAdapter<? extends ConfigurableRunnable> adapter){
        typeFactories.add(new TypeInfo(factory,adapter));
    }
    public Set<TypeInfo> getTypeFactories(){
        return new HashSet<>(typeFactories);
    }
    public TypeInfo getNextFactory(TypeInfo factory){
        var list = asList();
        var i = list.indexOf(factory);
        return list.get((i+1)%list.size());
    }

    public TypeInfo getNextFactoryForType(ConfigurableRunnable action) {
        for(var s : asList()){
            if(s.type == action.getClass()){
                return getNextFactory(s);
            }
        }
        return asList().get(0);
    }
    public Gson getGson(){
        var builder =
                new GsonBuilder()
                        .setPrettyPrinting()
                        .registerTypeAdapter(ConfigurableRunnable.class, new Adapter())
                        .registerTypeAdapter(PizzaManager.class, new PizzaAdapter());
        typeFactories.forEach(e -> {
            if(e.adapter!=null)
                builder.registerTypeAdapter(e.type,e.adapter);
        });
        return builder.create();
    }
    public TypeInfo getFirstFactory(){
        return asList().get(0);
    }
    public class Adapter implements JsonDeserializer<ConfigurableRunnable>, JsonSerializer<ConfigurableRunnable> {
        @Override
        public ConfigurableRunnable deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if(json instanceof JsonObject jsonObject)
            {
                try{
                var typeName = jsonObject.getAsJsonPrimitive("RunnableType").getAsString();
                    for(var tf : typeFactories){
                        if(tf.type.getName().equals(typeName)){
                            return context.deserialize(jsonObject.getAsJsonObject("Object"), tf.type);
                        }
                    }
                }
                catch (NullPointerException ignored){}
            }
            return new NullActionProvider(false);
        }
        public static int t=0;
        @Override
        public JsonElement serialize(ConfigurableRunnable src, Type typeOfSrc, JsonSerializationContext context) {
            var json = new JsonObject();
            json.addProperty("RunnableType",src.getClass().getName());
            var elm = context.serialize(src,src.getClass());
            json.add("Object",elm);
            LavaJumper.log("serializing"+t++);
            return json;
        }
    }

    private static class PizzaAdapter implements JsonSerializer<PizzaManager>, JsonDeserializer<PizzaManager> {
        @Override
        public PizzaManager deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return null;
        }
        @Override
        public JsonElement serialize(PizzaManager src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonObject();
        }
    }
}
