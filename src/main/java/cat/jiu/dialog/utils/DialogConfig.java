package cat.jiu.dialog.utils;

import cat.jiu.dialog.ModMain;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;

import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(
		modid = ModMain.MODID,
		name = "jiu/" + ModMain.MODID + "/main",
		category = "config_main")
@Config.LangKey("config.dialog.main")
@Mod.EventBusSubscriber(modid = ModMain.MODID)
public class DialogConfig {
	
	/**
	 * @see cat.jiu.dialog.iface.IDialogText#isVanillaWrap()
	 */
	@Config.LangKey("dialog.config.vanilla_wrap")
	@Config.Comment("use vanilla to wrap text if true, else will use Single char wrap.")
	public static boolean Enable_Vanilla_Wrap_Text = true;

	@Config.LangKey("dialog.config.test_dialog")
	@Config.RequiresWorldRestart
	@Config.Comment("enable test dialog, need break 'minecraft:redstone_block' to display")
	public static boolean Enable_Test_Dialog = false;

	@Config.LangKey("dialog.config.dialog_width")
	@Config.Comment("the dialog gui widht")
	public static int Dialog_Gui_Width = 256;
	
	@SubscribeEvent
	public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
		if(event.getModID().equals(ModMain.MODID)) {
			ConfigManager.sync(ModMain.MODID, Config.Type.INSTANCE);
		}
	}
}
