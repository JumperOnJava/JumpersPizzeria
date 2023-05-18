package io.github.javajump3r;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import io.github.javajumper.lavajumper.LavaJumper;
import net.minecraft.client.gui.screen.Screen;

public class ModMenuIntegration implements ModMenuApi {
    public ConfigScreenFactory<Screen> getModConfigScreenFactory(){
        return LavaJumper.LavaJumperConfig::getFinishedConfigScreen;
    }

}
