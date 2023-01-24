package cat.jiu.dialog.iface;

import net.minecraft.entity.player.EntityPlayer;

public interface IOptionButtonTask extends IOptionTask {
	/**
	 * 按下对话框的按钮选项后的处理方式
	 * @param player 玩家对象
	 * @param mouseButton 按下按钮时使用的鼠标按键
	 */
	void run(EntityPlayer player, int mouseButton);
}
