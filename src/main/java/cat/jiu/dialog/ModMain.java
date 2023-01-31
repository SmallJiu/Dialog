package cat.jiu.dialog;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cat.jiu.dialog.net.DialogNetworkHandler;
import cat.jiu.dialog.ui.GuiHandler;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(
	modid = ModMain.MODID,
	name = ModMain.NAME,
	version = ModMain.VERSION,
	useMetadata = true,
	guiFactory = "cat.jiu.dialog.utils.DialogConfigGuiFactory"
)
public class ModMain {
	public static final String MODID = "dialog";
	public static final String NAME = "Dialog";
	public static final String OWNER = "small_jiu";
	public static final String VERSION = "1.0.0-a1";
	public static final Logger log = LogManager.getLogger(NAME);
	public static final DialogNetworkHandler network = new DialogNetworkHandler();

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		new GuiHandler();
		if(cat.jiu.dialog.utils.DialogConfig.Enable_Test_Dialog) {
			new cat.jiu.dialog.test.TestDialog(true);
			log.info("Enable Test Dialog.");
		}
	}
}
