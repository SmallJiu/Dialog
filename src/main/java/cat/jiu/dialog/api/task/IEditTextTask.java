package cat.jiu.dialog.api.task;

import cat.jiu.dialog.api.IOptionTask;
import crafttweaker.annotations.ZenRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.ZenClass;

@ZenRegister
@ZenClass("dialog.option.task.Text")
public interface IEditTextTask extends IOptionTask {
	@Deprecated
	default void run(ResourceLocation parent, ResourceLocation dialog, int option, EntityPlayer player, String text) {
		this.onTextConfirm(parent, dialog, option, player, text);
	}
	
	/**
	 * 按下对话框的文本框选项后的处理方式
	 * @param player 玩家对象
	 * @param text 文本框的文本
	 */
	void onTextConfirm(ResourceLocation parent, ResourceLocation dialog, int option, EntityPlayer player, String text);
}
