package io.github.JumperOnJava.jjpizza.pizzamenu.actions.actionproviders;

import com.google.gson.TypeAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;
public class TypeInfo implements Function<Boolean,ConfigurableRunnable>, Comparable<TypeInfo> {
    private final Function<Boolean,ConfigurableRunnable> factory;
    private final int hashCode;
    public final Class<? extends ConfigurableRunnable> type;
    public final TypeAdapter<?extends ConfigurableRunnable> adapter;
    public TypeInfo(Function<Boolean, ConfigurableRunnable> configurableRunnableSupplier, TypeAdapter<? extends ConfigurableRunnable> adapter){
        this.factory=configurableRunnableSupplier;
        this.adapter=adapter;

        var testobj = factory.apply(false);
        this.type=testobj.getClass();
        this.hashCode=testobj.getClass().getName().hashCode();
    }
    @Override
    public int hashCode() {
        return this.hashCode;
    }

    public ConfigurableRunnable apply(Boolean isReal) {
        return factory.apply(isReal);
    }
    @Override
    public int compareTo(@NotNull TypeInfo o) {
        return hashCode()-o.hashCode();
    }
}
