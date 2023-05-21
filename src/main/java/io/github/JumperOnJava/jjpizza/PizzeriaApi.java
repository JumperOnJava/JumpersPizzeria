package io.github.JumperOnJava.jjpizza;

import io.github.JumperOnJava.jjpizza.pizzamenu.PizzaManager;
import io.github.JumperOnJava.jjpizza.pizzamenu.actionregistry.ActionTypeRegistry;
import net.fabricmc.api.ClientModInitializer;

public class PizzeriaApi implements ClientModInitializer {

	private static PizzaManager mainManager;
	public static ActionTypeRegistry getRegistry(){
		if(mainManager==null)
			mainManager=new PizzaManager();
		return mainManager.actionTypeRegistry;
	}
	@Override
	public void onInitializeClient() {
		getRegistry();
	}
}
