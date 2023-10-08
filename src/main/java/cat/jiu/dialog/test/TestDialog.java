package cat.jiu.dialog.test;

import java.io.InputStreamReader;

import com.google.gson.JsonParser;

import cat.jiu.core.api.ITimer;
import cat.jiu.core.api.element.IText;
import cat.jiu.dialog.DialogAPI;
import cat.jiu.dialog.ModMain;
import cat.jiu.dialog.api.helper.DialogList;
import cat.jiu.dialog.api.helper.DialogOperation;
import cat.jiu.dialog.element.DialogOption;
import cat.jiu.dialog.element.option.OptionCheckbox;
import cat.jiu.dialog.element.option.OptionItemCheckbox;
import cat.jiu.dialog.element.option.OptionItemRadioButton;
import cat.jiu.dialog.element.option.OptionRadioButton;
import cat.jiu.dialog.element.option.timer.OptionTimerCheckbox;
import cat.jiu.dialog.element.option.timer.OptionTimerItemCheckbox;
import cat.jiu.dialog.element.option.timer.OptionTimerItemRadioButton;
import cat.jiu.dialog.element.option.timer.OptionTimerRadioButton;
import cat.jiu.dialog.event.DialogEvent;
import cat.jiu.dialog.utils.ButtonDisplayDialog;
import cat.jiu.dialog.utils.DialogBuilder;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

/**
 * @author small_jiu
 */
public class TestDialog {
	private static JsonParser parser;
	public static JsonParser getJsonParser() {
		if(parser==null) parser = new JsonParser();
		return parser;
	}
	private static DialogOption<TestDataUnit> testOption;
	public static DialogOption<TestDataUnit> getTestOption() {
		if(testOption==null) {
			testOption = DialogOption.register(new ResourceLocation(ModMain.MODID, "test_option"), TestDataUnit.class);
		}
		return testOption;
	}
	
	public static final ResourceLocation main = new ResourceLocation(ModMain.MODID, "test_main");
	
	public final DialogList list;
	
	public TestDialog(boolean saveTaskToJson) {
		MinecraftForge.EVENT_BUS.register(this);
		getTestOption();
		DialogList list_ = null;
		
		try {
			list_ = DialogList.get(getJsonParser().parse(new InputStreamReader(TestDialog.class.getResourceAsStream("/dialog_example.json"))).getAsJsonObject());
		}catch(Exception e) {
			list_ = new DialogList(true);
			DialogList list = list_;
			
			ResourceLocation checkbox = new ResourceLocation(ModMain.MODID, "checkbox");
			ResourceLocation radio = new ResourceLocation(ModMain.MODID, "radio");
			ResourceLocation item = new ResourceLocation(ModMain.MODID, "item");
			ResourceLocation timer = new ResourceLocation(ModMain.MODID, "timer");
			
			list.addDialogOperation(main, new DialogOperation(
					new DialogBuilder(IText.from("All option example in this dialog."))
						.addButton(IText.from("A button option.3211111111111111111111111111111111111111111111111111111111111q6w+eg4f586q6g5ew6rg54q65e+wrg4q6+erg546qr5g46"))
						.addTextField(IText.from("A text option"), false)
						.addButton(IText.from("All checkbox option in sub dialog."))
						.addButton(IText.from("All radio button option in sub dialog."))
						.addButton(IText.from("All item options in sub dialog."))
						.addButton(IText.from("All timer options in sub dialog."))
						.addOption(new TestDataUnit())
					, saveTaskToJson));
			DialogOperation main = list.getDialogOperation(TestDialog.main);
			main.setButtonTask(0, (parent, dialog, option, player, mouseButton)->{
				player.sendMessage(new TextComponentString("=============="));
				player.sendMessage(new TextComponentString(String.format("CallSide: %s, DialogID: %s, OptionIndex: %s, Player: %s, ClickButton: %s", player.world.isRemote ? Side.CLIENT : Side.SERVER, dialog, option, player.getName(), mouseButton)));
			});
			main.setEditTextTask(1, (parent, dialog, option, player, text)->{
				player.sendMessage(new TextComponentString("=============="));
				player.sendMessage(new TextComponentString(String.format("CallSide: %s, DialogID: %s, OptionIndex: %s, Player: %s, InputText: '%s'", player.world.isRemote ? Side.CLIENT : Side.SERVER, dialog, option, player.getName(), text)));
			});
			main.setButtonTask(2, new ButtonDisplayDialog(list, checkbox));
			main.setButtonTask(3, new ButtonDisplayDialog(list, radio));
			main.setButtonTask(4, new ButtonDisplayDialog(list, item));
			main.setButtonTask(5, new ButtonDisplayDialog(list, timer));
			
			// checkbox
			list.addDialogOperation(checkbox, new DialogOperation(new DialogBuilder(IText.from("All checkbox option in this dialog. I not recommended more than one checkbox option in one dialog, it may create bug."))
					.addOption(new OptionCheckbox(false, 4, new int[] {1},
							IText.from("a check option"), IText.from("a check option"), IText.from("a check option"), IText.from("a check option"), IText.from("a check option") 
					))
					.addTextField(IText.from("Just a separate."), false)
					.addOption(new OptionItemCheckbox(false, 4, null,
							new ItemStack(Items.DIAMOND,1), new ItemStack(Items.DIAMOND,1), new ItemStack(Items.DIAMOND,4),  new ItemStack(Items.DIAMOND,5), new ItemStack(Items.DIAMOND,1), new ItemStack(Items.DIAMOND,4) 
					))
					, saveTaskToJson)
			);
			
			list.getDialogOperation(checkbox)
				.setCheckBoxCheckTask(0, (parent, dialog, option, player, selectIndex, selectString, remove)->{
					player.sendMessage(new TextComponentString("=============="));
					player.sendMessage(new TextComponentString(String.format("CallSide: %s, DialogID: %s, OptionIndex: %s, Player: %s, OptionText: '%s', OptionIndex: %s, RemoveOption: %s", player.world.isRemote ? Side.CLIENT : Side.SERVER, dialog, option, player.getName(), selectString, selectIndex, remove)));
				})
				.setCheckBoxConfirmTask(0, (parent, dialog, option, player, selects)->{
					player.sendMessage(new TextComponentString("=============="));
					player.sendMessage(new TextComponentString(String.format("CallSide: %s, DialogID: %s, OptionIndex: %s, Player: %s, Selects: '%s'", player.world.isRemote ? Side.CLIENT : Side.SERVER, dialog, option, player.getName(), selects)));
					if(player.world.isRemote) {
						list.display(player, TestDialog.main);
					}
				})
				.setItemCheckboxSelectTask(2, (parent, dialog, option, player, selectIndex, stack, remove)->{
					player.sendMessage(new TextComponentString("=============="));
					player.sendMessage(new TextComponentString(String.format("CallSide: %s, DialogID: %s, OptionIndex: %s, Player: %s, SelectItem: '%s', SelectIndex: %s, SelectRemove: %s", player.world.isRemote ? Side.CLIENT : Side.SERVER, dialog, option, player.getName(), stack, selectIndex, remove)));
				})
				.setItemCheckboxConfirmTask(2, (parent, dialog, option, player, selects)->{
					player.sendMessage(new TextComponentString("=============="));
					player.sendMessage(new TextComponentString(String.format("CallSide: %s, DialogID: %s, OptionIndex: %s, Player: %s, Selects: '%s'", player.world.isRemote ? Side.CLIENT : Side.SERVER, dialog, option, player.getName(), selects)));
					if(player.world.isRemote) {
						list.display(player, TestDialog.main);
					}
				});
			
			// radio
			list.addDialogOperation(radio, new DialogOperation(new DialogBuilder(IText.from("All checkbox option in this dialog. I not recommended more than one radio button option in one dialog, it may create bug."))
					.addOption(new OptionRadioButton(false, 4, 1,
							IText.from("a check option"), IText.from("a check option"), IText.from("a check option"), IText.from("a check option"), IText.from("a check option") 
					))
					.addTextField(IText.from("Just a separate."), false)
					.addOption(new OptionItemRadioButton(false, 4, 0,
							new ItemStack(Items.DIAMOND,1), new ItemStack(Items.DIAMOND,1), new ItemStack(Items.DIAMOND,4),  new ItemStack(Items.DIAMOND,5), new ItemStack(Items.DIAMOND,1), new ItemStack(Items.DIAMOND,4) 
					))
					, saveTaskToJson)
			);
			
			list.getDialogOperation(radio)
				.setRadioButtonTask(0, (parent, dialog, option, player, selectIndex, optionString, confirm)->{
					player.sendMessage(new TextComponentString("=============="));
					player.sendMessage(new TextComponentString(String.format("CallSide: %s, DialogID: %s, OptionIndex: %s, Player: %s, SelectIndex: %s, SelectText: '%s', Confirm: %s", player.world.isRemote ? Side.CLIENT : Side.SERVER, dialog, option, player.getName(), selectIndex, optionString, confirm)));
					if(player.world.isRemote && confirm) {
						list.display(player, TestDialog.main);
					}
				})
				.setItemRadioButtonTask(2, (parent, dialog, option, player, selectIndex, stack, confirm)->{
					player.sendMessage(new TextComponentString("=============="));
					player.sendMessage(new TextComponentString(String.format("CallSide: %s, DialogID: %s, OptionIndex: %s, Player: %s, SelectIndex: %s, SelectItem: '%s', Confirm: %s", player.world.isRemote ? Side.CLIENT : Side.SERVER, dialog, option, player.getName(), selectIndex, stack, confirm)));
					if(player.world.isRemote && confirm) {
						list.display(player, TestDialog.main);
					}
				});
			
			// item
			list.addDialogOperation(item, new DialogOperation(new DialogBuilder(IText.from("All item option in this dialog. You can enable time expire auto confirm on timer option. I not recommended more than one radio button option in one dialog, it may create bug."))
					.addButton(IText.from("Normal item checkbox."))
					.addButton(IText.from("Timer item checkbox."))
					.addButton(IText.from("Normal item radio button."))
					.addButton(IText.from("Timer item radio button."))
					.addButton(IText.from("Back.").setCenter(true))
					, saveTaskToJson)
			);
			list.getDialogOperation(item)
				.setButtonTask(0, new ButtonDisplayDialog(list, new ResourceLocation(ModMain.MODID, "normal_item_checkbox")))
				.setButtonTask(1, new ButtonDisplayDialog(list, new ResourceLocation(ModMain.MODID, "timer_item_checkbox")))
				.setButtonTask(2, new ButtonDisplayDialog(list, new ResourceLocation(ModMain.MODID, "normal_item_radio")))
				.setButtonTask(3, new ButtonDisplayDialog(list, new ResourceLocation(ModMain.MODID, "timer_item_radio")))
				.setButtonTask(4, new ButtonDisplayDialog(list, TestDialog.main))
			;
			
			list.addDialogOperation(new ResourceLocation(ModMain.MODID, "normal_item_checkbox"), new DialogOperation(new DialogBuilder(IText.from("I not recommended more than one radio button option in one dialog, it may create bug."))
					.addOption(new OptionItemCheckbox(false, 4, new int[] {0}, 
							new ItemStack(Items.DIAMOND,1), new ItemStack(Items.DIAMOND,1), new ItemStack(Items.DIAMOND,4),  new ItemStack(Items.DIAMOND,5), new ItemStack(Items.DIAMOND,1), new ItemStack(Items.DIAMOND,4)
					)), saveTaskToJson)
			);
			list.getDialogOperation(new ResourceLocation(ModMain.MODID, "normal_item_checkbox"))
				.setItemCheckboxSelectTask(0, (parent, dialog, option, player, selectIndex, stack, remove)->{
					player.sendMessage(new TextComponentString("=============="));
					player.sendMessage(new TextComponentString(String.format("CallSide: %s, DialogID: %s, OptionIndex: %s, Player: %s, SelectItem: '%s', SelectIndex: %s, SelectRemove: %s", player.world.isRemote ? Side.CLIENT : Side.SERVER, dialog, option, player.getName(), stack, selectIndex, remove)));
				})
				.setItemCheckboxConfirmTask(0, (parent, dialog, option, player, selects)->{
					player.sendMessage(new TextComponentString("=============="));
					player.sendMessage(new TextComponentString(String.format("CallSide: %s, DialogID: %s, OptionIndex: %s, Player: %s, Selects: '%s'", player.world.isRemote ? Side.CLIENT : Side.SERVER, dialog, option, player.getName(), selects)));
					if(player.world.isRemote) {
						if(parent!=null) {
							list.display(player, parent);
						}else {
							list.display(player, item);
						}
					}
				});
			
			list.addDialogOperation(new ResourceLocation(ModMain.MODID, "timer_item_checkbox"), new DialogOperation(new DialogBuilder(IText.from("You can enable time expire auto confirm on timer option. I not recommended more than one radio button option in one dialog, it may create bug."))
					.addOption(new OptionTimerItemCheckbox(ITimer.from(false, 30, 19, 999), false, new OptionItemCheckbox(false, 3, new int[] {0}, 
							new ItemStack(Items.DIAMOND,1), new ItemStack(Items.DIAMOND,1), new ItemStack(Items.DIAMOND,4),  new ItemStack(Items.DIAMOND,5), new ItemStack(Items.DIAMOND,1), new ItemStack(Items.DIAMOND,4)
					))), saveTaskToJson));
			
			list.getDialogOperation(new ResourceLocation(ModMain.MODID, "timer_item_checkbox"))
				.setItemCheckboxSelectTask(0, (parent, dialog, option, player, selectIndex, stack, remove)->{
					player.sendMessage(new TextComponentString("=============="));
					player.sendMessage(new TextComponentString(String.format("CallSide: %s, DialogID: %s, OptionIndex: %s, Player: %s, SelectItem: '%s', SelectIndex: %s, SelectRemove: %s", player.world.isRemote ? Side.CLIENT : Side.SERVER, dialog, option, player.getName(), stack, selectIndex, remove)));
				})
				.setItemCheckboxConfirmTask(0, (parent, dialog, option, player, selects)->{
					player.sendMessage(new TextComponentString("=============="));
					player.sendMessage(new TextComponentString(String.format("CallSide: %s, DialogID: %s, OptionIndex: %s, Player: %s, Selects: '%s'", player.world.isRemote ? Side.CLIENT : Side.SERVER, dialog, option, player.getName(), selects)));
					if(player.world.isRemote) {
						if(parent!=null) {
							list.display(player, parent);
						}else {
							list.display(player, item);
						}
					}
				});
			
			list.addDialogOperation(new ResourceLocation(ModMain.MODID, "normal_item_radio"), new DialogOperation(new DialogBuilder(IText.from("I not recommended more than one radio button option in one dialog, it may create bug."))
					.addOption(new OptionItemRadioButton(false, 4, 0,
							new ItemStack(Items.DIAMOND,1), new ItemStack(Items.DIAMOND,1), new ItemStack(Items.DIAMOND,4),  new ItemStack(Items.DIAMOND,5), new ItemStack(Items.DIAMOND,1), new ItemStack(Items.DIAMOND,4) 
					)), saveTaskToJson)
			);
			list.getDialogOperation(new ResourceLocation(ModMain.MODID, "normal_item_radio"))
				.setItemRadioButtonTask(0, (parent, dialog, option, player, selectIndex, stack, confirm)->{
					player.sendMessage(new TextComponentString("=============="));
					player.sendMessage(new TextComponentString(String.format("CallSide: %s, DialogID: %s, OptionIndex: %s, Player: %s, SelectItem: '%s', SelectIndex: %s, Confirm: %s", player.world.isRemote ? Side.CLIENT : Side.SERVER, dialog, option, player.getName(), stack, selectIndex, confirm)));
					if(player.world.isRemote && confirm) {
						if(parent!=null) {
							list.display(player, parent);
						}else {
							list.display(player, item);
						}
					}
				});
			
			list.addDialogOperation(new ResourceLocation(ModMain.MODID, "timer_item_radio"), new DialogOperation(new DialogBuilder(IText.from("You can enable time expire auto confirm on timer option. I not recommended more than one radio button option in one dialog, it may create bug."))
					.addOption(new OptionTimerItemRadioButton(ITimer.from(false, 30, 19, 999), false, new OptionItemRadioButton(false, 4, 0,
							new ItemStack(Items.DIAMOND,1), new ItemStack(Items.DIAMOND,1), new ItemStack(Items.DIAMOND,4),  new ItemStack(Items.DIAMOND,5), new ItemStack(Items.DIAMOND,1), new ItemStack(Items.DIAMOND,4) 
					))), saveTaskToJson)
			);
			list.getDialogOperation(new ResourceLocation(ModMain.MODID, "timer_item_radio"))
				.setItemRadioButtonTask(0, (parent, dialog, option, player, selectIndex, stack, confirm)->{
					player.sendMessage(new TextComponentString("=============="));
					player.sendMessage(new TextComponentString(String.format("CallSide: %s, DialogID: %s, OptionIndex: %s, Player: %s, SelectIndex: %s, SelectItem: '%s', Confirm: %s", player.world.isRemote ? Side.CLIENT : Side.SERVER, dialog, option, player.getName(), selectIndex, stack, confirm)));
					if(player.world.isRemote && confirm) {
						if(parent!=null) {
							list.display(player, parent);
						}else {
							list.display(player, item);
						}
					}
				});
			
			// timer
			
			list.addDialogOperation(timer, new DialogOperation(new DialogBuilder(IText.from("All timer option in this dialog. You can enable time expire auto confirm on timer option. I not recommended more than one radio button option in one dialog, it may create bug."))
					.addButton(IText.from("Timer checkbox."))
					.addButton(IText.from("Timer item checkbox."))
					.addButton(IText.from("Timer radio button."))
					.addButton(IText.from("Timer item radio button."))
					.addButton(IText.from("Back.").setCenter(true))
					, saveTaskToJson)
			);
			list.getDialogOperation(timer)
				.setButtonTask(0, new ButtonDisplayDialog(list, new ResourceLocation(ModMain.MODID, "timer_checkbox")))
				.setButtonTask(1, new ButtonDisplayDialog(list, new ResourceLocation(ModMain.MODID, "timer_item_checkbox")))
				.setButtonTask(2, new ButtonDisplayDialog(list, new ResourceLocation(ModMain.MODID, "timer_radio")))
				.setButtonTask(3, new ButtonDisplayDialog(list, new ResourceLocation(ModMain.MODID, "timer_item_radio")))
				.setButtonTask(4, new ButtonDisplayDialog(list, TestDialog.main));
			
			list.addDialogOperation(new ResourceLocation(ModMain.MODID, "timer_checkbox"), new DialogOperation(new DialogBuilder(IText.from("You can enable time expire auto confirm on timer option. I not recommended more than one radio button option in one dialog, it may create bug."))
					.addOption(new OptionTimerCheckbox(ITimer.from(false, 29, 19, 999), false, new OptionCheckbox(false, 4, new int[] {0,3}, 
								IText.from("a check option"), IText.from("a check option"), IText.from("a check option"), IText.from("a check option"), IText.from("a check option")
							)))
					, saveTaskToJson));
			list.getDialogOperation(new ResourceLocation(ModMain.MODID, "timer_checkbox"))
				.setCheckBoxCheckTask(0, (parent, dialog, option, player, selectIndex, selectString, remove)->{
					player.sendMessage(new TextComponentString("=============="));
					player.sendMessage(new TextComponentString(String.format("CallSide: %s, DialogID: %s, OptionIndex: %s, Player: %s, OptionIndex: %s, OptionText: '%s', RemoveOption: %s", player.world.isRemote ? Side.CLIENT : Side.SERVER, dialog, option, player.getName(), selectIndex, selectString, remove)));
				})
				.setCheckBoxConfirmTask(0, (parent, dialog, option, player, selects)->{
					player.sendMessage(new TextComponentString("=============="));
					player.sendMessage(new TextComponentString(String.format("CallSide: %s, DialogID: %s, OptionIndex: %s, Player: %s, Selects: '%s'", player.world.isRemote ? Side.CLIENT : Side.SERVER, dialog, option, player.getName(), selects)));
					if(player.world.isRemote) {
						if(parent!=null) {
							list.display(player, parent);
						}else {
							list.display(player, timer);
						}
					}
				});
			
			list.addDialogOperation(new ResourceLocation(ModMain.MODID, "timer_radio"), new DialogOperation(new DialogBuilder(IText.from("You can enable time expire auto confirm on timer option. I not recommended more than one radio button option in one dialog, it may create bug."))
					.addOption(new OptionTimerRadioButton(ITimer.from(false, 29, 19, 999), false, new OptionRadioButton(false, 4, 3, 
								IText.from("a check option"), IText.from("a check option"), IText.from("a check option"), IText.from("a check option"), IText.from("a check option")
							)))
					, saveTaskToJson));
			list.getDialogOperation(new ResourceLocation(ModMain.MODID, "timer_radio"))
				.setRadioButtonTask(0, (parent, dialog, option, player, selectIndex, optionString, confirm)->{
					player.sendMessage(new TextComponentString("=============="));
					player.sendMessage(new TextComponentString(String.format("CallSide: %s, DialogID: %s, OptionIndex: %s, Player: %s, SelectIndex: %s, SelectText: '%s', Confirm: %s", player.world.isRemote ? Side.CLIENT : Side.SERVER, dialog, option, player.getName(), selectIndex, optionString, confirm)));
					if(player.world.isRemote && confirm) {
						if(parent!=null) {
							list.display(player, parent);
						}else {
							list.display(player, timer);
						}
					}
				});
		}
		this.list = list_;
	}
	
	public void unload() {
		MinecraftForge.EVENT_BUS.unregister(this);
		MinecraftForge.EVENT_BUS.unregister(this.list);
	}
	
	@SubscribeEvent
	public void breakBlock(BlockEvent.BreakEvent event) {
		if(event.getState().getBlock() == Blocks.REDSTONE_BLOCK) {
			DialogAPI.displayDialog(event.getPlayer(), list.getDialogOperation(main).copyDialog());
		}
	}
	
	@SubscribeEvent
	public void onOpenDialog(DialogEvent.Open event) {
//		String side = event.player.world.isRemote ? "Client" : "Server";
//		event.player.sendMessage(new TextComponentString("Player on " + side + " Open Dialog"));
	}
	@SubscribeEvent
	public void onCloseDialog(DialogEvent.Close event) {
//		String side = event.player.world.isRemote ? "Client" : "Server";
//		event.player.sendMessage(new TextComponentString("Player on " + side + " Close Dialog"));
	}
}
