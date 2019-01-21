package net.geforcemods.teleportals.gui;

import net.geforcemods.teleportals.Teleportals;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import cpw.mods.fml.client.config.GuiConfig;

@SuppressWarnings({"unchecked", "rawtypes"})
public class TeleportalGuiScreen extends GuiConfig{

	public TeleportalGuiScreen(GuiScreen parentScreen) {
		super(parentScreen, new ConfigElement(Teleportals.configFile.getCategory("options")).getChildElements(), "teleportals", false, false, GuiConfig.getAbridgedConfigPath(Teleportals.configFile.toString()));
	}

}
