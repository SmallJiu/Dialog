package cat.jiu.dialog.api;

import cat.jiu.dialog.DialogAPI;
import cat.jiu.dialog.api.helper.DialogList;
import cat.jiu.dialog.iface.IOptionButtonTask;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

/**
 * 按下按钮选项后将展示一个对话框
 * @author small_jiu
 */
public class ButtonDisplayDialog implements IOptionButtonTask {
	/**
	 * 对话框ID
	 */
	public final ResourceLocation id;
	protected final DialogList list;
	/**
	 * @param list 对话框列表
	 * @param id 需要展示的对话框ID
	 */
	public ButtonDisplayDialog(DialogList list, ResourceLocation id) {
		this.list = list;
		this.id = id;
	}
	@Override
	public void run(EntityPlayer player, int mouseButton) {
		if(player.world.isRemote
		&& mouseButton == 0
		&& this.list!=null && this.list.hasDialogOperation(this.id)) {
			DialogAPI.displayDialog(player, this.list.getDialogOperation(this.id).getDialog());
		}
	}
}
