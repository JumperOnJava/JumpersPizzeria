package io.github.JumperOnJava.jjpizza;

import io.github.JumperOnJava.jjpizza.pizzamenu.PizzaManager;
import net.fabricmc.api.ClientModInitializer;

public class PizzeriaInit implements ClientModInitializer {

	private static PizzaManager mainManager;
	/*public static PizzaManager getManager(){
		if(mainManager==null)
			mainManager=new PizzaManager();
		return mainManager;
	}*/
	@Override
	public void onInitializeClient() {
		mainManager=new PizzaManager();
	}
}
