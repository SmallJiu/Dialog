package cat.jiu.dialog.iface;

import net.minecraft.entity.player.EntityPlayer;

public interface IOptionEditTextTask extends IOptionTask {
	/**
	 * 按下对话框的文本框选项后的处理方式
	 * @param player 玩家对象
	 * @param text 文本框的文本
	 */
	void run(EntityPlayer player, String text);
}
