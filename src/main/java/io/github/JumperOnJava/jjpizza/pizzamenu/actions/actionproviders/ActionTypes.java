package io.github.JumperOnJava.jjpizza.pizzamenu.actions.actionproviders;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import io.netty.channel.unix.IovArray;
import org.apache.commons.lang3.ObjectUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class
ActionTypes {
    public static final int gap = 4;
    //private static List<TypeAdapter<?>> typeAdapters = new ArrayList<>();

    static{
        typeFactories = new TreeSet<>();
        addActionType(KeybindingActionProvider::new,null);
        addActionType(NullActionProvider::new,null);
    }
    private static final Set<TypeInfo> typeFactories;
    private static List<TypeInfo> asList(){
        return new ArrayList<>(typeFactories);
    }
    public static void addActionType(Function<Boolean,ConfigurableRunnable> factory){
        typeFactories.add(new TypeInfo(factory,null));
    }
    public static void addActionType(Function<Boolean,ConfigurableRunnable> factory, TypeAdapter<? extends ConfigurableRunnable> adapter){
        typeFactories.add(new TypeInfo(factory,adapter));
    }
    public static Set<TypeInfo> getTypeFactories(){
        return new HashSet<>(typeFactories);
    }
    public static TypeInfo getNextFactory(TypeInfo factory){
        var list = asList();
        var i = list.indexOf(factory);
        return list.get((i+1)%list.size());
    }

    public static TypeInfo getNextFactoryForType(ConfigurableRunnable action) {
        for(var s : asList()){
            if(s.type == action.getClass()){
                return getNextFactory(s);
            }
        }
        return asList().get(0);
    }
    public static Gson getGson(){
        var builder = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(ConfigurableRunnable.class, new Adapter());
        typeFactories.forEach(e -> {
            if(e.adapter!=null)
                builder.registerTypeAdapter(e.type,e.adapter);
        });
        return builder.create();
    }
    public static TypeInfo getFirstFactory(){
        return asList().get(0);
    }
    public static class Adapter implements JsonDeserializer<ConfigurableRunnable>, JsonSerializer<ConfigurableRunnable> {
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

        @Override
        public JsonElement serialize(ConfigurableRunnable src, Type typeOfSrc, JsonSerializationContext context) {
            var json = new JsonObject();
            json.addProperty("RunnableType",src.getClass().getName());
            json.add("Object",getGson().toJsonTree(src));
            return json;
        }
    }
    static class TypeWrapper<T>{
        String className;
        T object;
    }
}
