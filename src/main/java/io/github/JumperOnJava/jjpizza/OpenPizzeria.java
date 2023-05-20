package io.github.JumperOnJava.jjpizza;

import io.github.JumperOnJava.jjpizza.pizzamenu.PizzaManager;
import net.fabricmc.api.ClientModInitializer;

public class OpenPizzeria implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		PizzaManager.getManager();
	}
}
