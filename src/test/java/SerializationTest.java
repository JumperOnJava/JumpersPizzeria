import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.JumperOnJava.jjpizza.pizzamenu.actions.RunnablePizzaSlice;
import io.github.JumperOnJava.jjpizza.pizzamenu.actions.actionproviders.ActionTypes;
import io.github.JumperOnJava.jjpizza.pizzamenu.actions.actionproviders.ConfigurableRunnable;
import io.github.JumperOnJava.jjpizza.pizzamenu.actions.actionproviders.KeybindingActionProvider;
import io.github.javajumper.lavajumper.common.FileReadWrite;
import io.github.javajumper.lavajumper.datatypes.CircleSlice;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.*;

public class SerializationTest {
    @Test
    public void tes(){
        List<RunnablePizzaSlice> slices = new LinkedList<>();
        var len = 4;
        for(int i=0;i<len;i++){
            var action = getRandomAction(i,len);
            var slice = new RunnablePizzaSlice("Empty action", CircleSlice.percent((float) i /len, (float) (i + 1) /len));
            try{
                FieldUtils.writeField(slice,"onLeftClick",action,true);
            }catch (Exception e){throw new RuntimeException(e);}

            slices.add(slice);
        }
        FileReadWrite.write(new File("test/Test.json"),new GsonBuilder().setPrettyPrinting().create().toJson(slices));
        LinkedList<RunnablePizzaSlice> newSlices = ActionTypes.getGson().fromJson(FileReadWrite.read(new File("test/Test.json")), LinkedList.class);
        FileReadWrite.write(new File("test/Test2.json"),new GsonBuilder().setPrettyPrinting().create().toJson(newSlices));

    }

    private ConfigurableRunnable getRandomAction(int i, int len) {
        var action = new KeybindingActionProvider(false);
        try{
            FieldUtils.writeField(action,"targetKeyBindingID","test.key.value."+i,true);
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
        return action;
    }

    public static void testLog(Object... objects) {
        var string = "";
        for (Object arg : objects) {
            if (arg == null)
                arg = "null";
            string = string + arg;

        }
        System.out.println(string);
    }
}
