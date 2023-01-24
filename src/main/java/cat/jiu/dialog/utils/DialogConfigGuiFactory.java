package cat.jiu.dialog.utils;

import java.util.Collections;
import java.util.Set;

import cat.jiu.dialog.ModMain;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;

public class DialogConfigGuiFactory implements IModGuiFactory {
	public void initialize(Minecraft minecraftInstance) {}
	public boolean hasConfigGui() { return true; }
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() { return Collections.emptySet(); }
	public GuiScreen createConfigGui(GuiScreen parentScreen) {
		return new GuiConfig(parentScreen, ConfigElement.from(DialogConfig.class).getChildElements(), ModMain.MODID, false, false, "Dialog");
	}
}
