package cat.jiu.dialog.test;

import java.io.InputStreamReader;

import com.google.gson.JsonParser;

import cat.jiu.dialog.DialogAPI;
import cat.jiu.dialog.ModMain;
import cat.jiu.dialog.api.BaseDialogOptionType;
import cat.jiu.dialog.api.ButtonDisplayDialog;
import cat.jiu.dialog.api.DialogBuilder;
import cat.jiu.dialog.api.DialogDimension;
import cat.jiu.dialog.api.helper.DialogList;
import cat.jiu.dialog.api.helper.DialogOperation;
import cat.jiu.dialog.element.DialogText;
import cat.jiu.dialog.element.option.DialogOptionDrawUnit;
import cat.jiu.dialog.event.DialogEvent;
import cat.jiu.dialog.iface.IDialogOptionDataUnit;
import cat.jiu.dialog.utils.DialogConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * example from Sea of Thieves
 * @author small_jiu
 */
@EventBusSubscriber
public class TestDialog {
	private static JsonParser parser;
	public static JsonParser getJsonParser() {
		if(parser==null) parser = new JsonParser();
		return parser;
	}
	private static BaseDialogOptionType testOption;
	public static BaseDialogOptionType getTestOption() {
		if(testOption==null) {
			testOption = new BaseDialogOptionType(new ResourceLocation(ModMain.MODID, "test_option"), TestDataUnit.class) {
					public DialogOptionDrawUnit getDrawUnit(ResourceLocation dialogID, int optionID, IDialogOptionDataUnit option, DialogDimension dialogDimension) {
						return new TestDrawUnit(dialogID, option, optionID, Minecraft.getMinecraft().fontRenderer, dialogDimension);
					}
				};
		}
		return testOption;
	}
	
	public static final ResourceLocation main = new ResourceLocation(ModMain.MODID, "test_main");
	
	public final DialogList list;
	
	public TestDialog(boolean useJson) {
		MinecraftForge.EVENT_BUS.register(this);
		getTestOption();
		
		if(useJson) {
			list = DialogList.get(getJsonParser().parse(new InputStreamReader(TestDialog.class.getResourceAsStream("/dialog_example.json"))).getAsJsonObject());
		}else {
			list = new DialogList(true);
			
			ResourceLocation main_0_0 = new ResourceLocation(ModMain.MODID, "test_main_0_0");
			ResourceLocation main_1_0 = new ResourceLocation(ModMain.MODID, "test_main_1_0");
			ResourceLocation main_2_0 = new ResourceLocation(ModMain.MODID, "test_main_2_0");
			ResourceLocation main_3_0 = new ResourceLocation(ModMain.MODID, "test_main_3_0");
			
			list.addDialogOperation(main, new DialogOperation(new DialogBuilder(main, new DialogText("dialog.dev.0.title"))
						.addButton(new DialogText("dialog.dev.0.btn.0"))
						.addButtonTooltip(0, new DialogText("dialog.dev.info"))
						.addButton(new DialogText("dialog.dev.0.btn.1"))
						.addButton(new DialogText("dialog.dev.0.btn.2"))
						.addButton(new DialogText("dialog.dev.0.btn.3"))
						.addTextField(new DialogText("dialog.dev.0.text.4"), true)
						.addTextFieldTooltip(4, new DialogText("dialog.dev.info"))
						.addOption(new TestDataUnit())
						
					)
					.setButtonTask(0, new ButtonDisplayDialog(list, main_0_0))
					.setButtonTask(1, new ButtonDisplayDialog(list, main_1_0))
					.setButtonTask(2, new ButtonDisplayDialog(list, main_2_0))
					.setButtonTask(3, new ButtonDisplayDialog(list, main_3_0)));
			
			
			ResourceLocation main_0_1 = new ResourceLocation(ModMain.MODID, "test_main_0_1");
			ResourceLocation main_0_2 = new ResourceLocation(ModMain.MODID, "test_main_0_2");
			ResourceLocation main_0_3 = new ResourceLocation(ModMain.MODID, "test_main_0_3");
			ResourceLocation main_0_4 = new ResourceLocation(ModMain.MODID, "test_main_0_4");
			list.addDialogOperation(main_0_0, new DialogOperation(new DialogBuilder(new DialogText("dialog.dev.100.title"))
					.addButton(new DialogText("dialog.text.continue")))
				.setButtonTask(0, new ButtonDisplayDialog(list, new ResourceLocation(ModMain.MODID, "test_main_0_1"))));
			
			list.addDialogOperation(main_0_1, new DialogOperation(new DialogBuilder(new DialogText("dialog.dev.101.title"))
					.addButton(new DialogText("dialog.text.continue")))
				.setButtonTask(0, new ButtonDisplayDialog(list, main_0_2)));

			list.addDialogOperation(main_0_2, new DialogOperation(new DialogBuilder(new DialogText("dialog.dev.102.title"))
					.addButton(new DialogText("dialog.text.continue")))
				.setButtonTask(0, new ButtonDisplayDialog(list, main_0_3)));

			list.addDialogOperation(main_0_3, new DialogOperation(new DialogBuilder(new DialogText("dialog.dev.103.title"))
					.addButton(new DialogText("dialog.text.continue")))
				.setButtonTask(0, new ButtonDisplayDialog(list, main_0_4)));

			list.addDialogOperation(main_0_4, new DialogOperation(new DialogBuilder(new DialogText("dialog.dev.104.title"))
					.addButton(new DialogText("dialog.text.back")))
				.setButtonTask(0, new ButtonDisplayDialog(list, main)));
			
			ResourceLocation main_1_1 = new ResourceLocation(ModMain.MODID, "test_main_1_1");
			ResourceLocation main_1_2 = new ResourceLocation(ModMain.MODID, "test_main_1_2");
			ResourceLocation main_1_3 = new ResourceLocation(ModMain.MODID, "test_main_1_3");
			list.addDialogOperation(main_1_0, new DialogOperation(new DialogBuilder(new DialogText("dialog.dev.200.title"))
					.addButton(new DialogText("dialog.text.continue")))
				.setButtonTask(0, new ButtonDisplayDialog(list, main_1_1)));

			list.addDialogOperation(main_1_1, new DialogOperation(new DialogBuilder(new DialogText("dialog.dev.201.title"))
					.addButton(new DialogText("dialog.text.continue")))
				.setButtonTask(0, new ButtonDisplayDialog(list, main_1_2)));

			list.addDialogOperation(main_1_2, new DialogOperation(new DialogBuilder(new DialogText("dialog.dev.202.title"))
					.addButton(new DialogText("dialog.text.continue")))
				.setButtonTask(0, new ButtonDisplayDialog(list, main_1_3)));

			list.addDialogOperation(main_1_3, new DialogOperation(new DialogBuilder(new DialogText("dialog.dev.203.title"))
					.addButton(new DialogText("dialog.text.back")))
				.setButtonTask(0, new ButtonDisplayDialog(list, main)));
			
			ResourceLocation main_2_1 = new ResourceLocation(ModMain.MODID, "test_main_2_1");
			ResourceLocation main_2_2 = new ResourceLocation(ModMain.MODID, "test_main_2_2");
			list.addDialogOperation(main_2_0, new DialogOperation(new DialogBuilder(new DialogText("dialog.dev.300.title"))
					.addButton(new DialogText("dialog.text.continue")))
				.setButtonTask(0, new ButtonDisplayDialog(list, main_2_1)));

			list.addDialogOperation(main_2_1, new DialogOperation(new DialogBuilder(new DialogText("dialog.dev.301.title"))
					.addButton(new DialogText("dialog.text.continue")))
				.setButtonTask(0, new ButtonDisplayDialog(list, main_2_2)));

			list.addDialogOperation(main_2_2, new DialogOperation(new DialogBuilder(new DialogText("dialog.dev.302.title"))
					.addButton(new DialogText("dialog.text.back")))
				.setButtonTask(0, new ButtonDisplayDialog(list, main)));
			
			ResourceLocation main_3_1 = new ResourceLocation(ModMain.MODID, "test_main_3_1");
			ResourceLocation main_3_2 = new ResourceLocation(ModMain.MODID, "test_main_3_2");
			ResourceLocation main_3_3 = new ResourceLocation(ModMain.MODID, "test_main_3_3");
			list.addDialogOperation(main_3_0, new DialogOperation(new DialogBuilder(new DialogText("dialog.dev.400.title"))
					.addButton(new DialogText("dialog.text.continue")))
				.setButtonTask(0, new ButtonDisplayDialog(list, main_3_1)));

			list.addDialogOperation(main_3_1, new DialogOperation(new DialogBuilder(new DialogText("dialog.dev.401.title"))
					.addButton(new DialogText("dialog.text.continue")))
				.setButtonTask(0, new ButtonDisplayDialog(list, main_3_2)));

			list.addDialogOperation(main_3_2, new DialogOperation(new DialogBuilder(new DialogText("dialog.dev.402.title"))
					.addButton(new DialogText("dialog.text.continue")))
				.setButtonTask(0, new ButtonDisplayDialog(list, main_3_3)));

			list.addDialogOperation(main_3_3, new DialogOperation(new DialogBuilder(new DialogText("dialog.dev.403.title"))
					.addButton(new DialogText("dialog.text.back")))
				.setButtonTask(0, new ButtonDisplayDialog(list, main)));
		}
		
		list.getDialogOperation(main).setEditTextTask(4, (p, s)->{
			if(p.world.isRemote) p.sendMessage(new TextComponentTranslation("dialog.dev.0.text.4.msg", s));
		});
		
		DialogConfig.Enable_Test_Dialog = false;
		ConfigManager.sync(ModMain.MODID, Config.Type.INSTANCE);
	}
	
	@SubscribeEvent
	public void breakBlock(BlockEvent.BreakEvent event) {
		if(event.getState().getBlock() == Blocks.REDSTONE_BLOCK) {
			DialogAPI.displayDialog(event.getPlayer(), list.getDialogOperation(main).getDialog());
		}
	}
	
	@SubscribeEvent
	public void onOpenDialog(DialogEvent.Open event) {
		String side = event.player.world.isRemote ? "Client" : "Server";
		event.player.sendMessage(new TextComponentString("Player on " + side + " Open Dialog"));
	}
	@SubscribeEvent
	public void onCloseDialog(DialogEvent.Close event) {
		String side = event.player.world.isRemote ? "Client" : "Server";
		event.player.sendMessage(new TextComponentString("Player on " + side + " Close Dialog"));
	}
}
