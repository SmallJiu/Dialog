package cat.jiu.dialog.api.task;

import cat.jiu.dialog.api.IOptionTask;
import cat.jiu.dialog.api.helper.DialogList;
import cat.jiu.dialog.utils.ButtonDisplayDialog;
import crafttweaker.annotations.ZenRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("dialog.option.task.Button")
public interface IButtonTask extends IOptionTask {
	@Deprecated
	default void run(ResourceLocation parent, ResourceLocation dialog, int option, EntityPlayer player, int mouseButton) {
		this.onButtonClick(parent, dialog, option, player, mouseButton);
	}
	
	/**
	 * 按下对话框的按钮选项后的处理方式
	 * @param player 玩家对象
	 * @param mouseButton 按下按钮时使用的鼠标按键
	 */
	void onButtonClick(ResourceLocation parent, ResourceLocation dialog, int option, EntityPlayer player, int mouseButton);
	
	@ZenMethod("display")
	static IButtonTask createDisplayDialogTask(DialogList list, ResourceLocation id) {
		return new ButtonDisplayDialog(list, id);
	}
}
