package cat.jiu.dialog;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cat.jiu.dialog.element.DialogOption;
import cat.jiu.dialog.event.OptionEvent;
import cat.jiu.dialog.net.DialogNetworkHandler;
import cat.jiu.dialog.ui.GuiHandler;
import cat.jiu.dialog.utils.ButtonDisplayDialog;

import net.minecraft.util.ResourceLocation;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;

@Mod(
	modid = ModMain.MODID,
	name = ModMain.NAME,
	version = ModMain.VERSION,
	useMetadata = true,
	guiFactory = "cat.jiu.dialog.utils.DialogConfigGuiFactory",
	dependencies = "after:jiucore@[1.1.6-a1,)"
)
public class ModMain {
	public static final String MODID = "dialog";
	public static final String NAME = "Dialog";
	public static final String OWNER = "small_jiu";
	public static final String VERSION = "1.0.1-a0";
	public static final Logger log = LogManager.getLogger(NAME);
	public static final DialogNetworkHandler NETWORK = new DialogNetworkHandler();

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		new GuiHandler();
		
		MinecraftForge.EVENT_BUS.post(new OptionEvent.Registry.Pre());
		DialogOption.register();
		MinecraftForge.EVENT_BUS.post(new OptionEvent.Registry.Post());
		
		DialogAPI.registerTask(ButtonDisplayDialog.class, (list, data)->new ButtonDisplayDialog(list, new ResourceLocation(data.get("next").getAsString())));
		
		if(cat.jiu.dialog.utils.DialogConfig.Enable_Test_Dialog) {
			cat.jiu.dialog.test.TestDialog.getTestOption();
		}
	}
	
	static cat.jiu.dialog.test.TestDialog test_dialog;
	@Mod.EventHandler
	public void onServerStarted(FMLServerStartedEvent event) {
		if(cat.jiu.dialog.utils.DialogConfig.Enable_Test_Dialog) {
			test_dialog = new cat.jiu.dialog.test.TestDialog(true);
			log.info("Enable Test Dialog.");
		}
	}
	@Mod.EventHandler
	public void onServerStopped(FMLServerStoppedEvent event) {
		if(test_dialog!=null) {
			test_dialog.unload();
			test_dialog = null;
		}
	}
}
