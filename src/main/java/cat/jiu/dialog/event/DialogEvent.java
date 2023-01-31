package cat.jiu.dialog.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.Event;

public class DialogEvent extends Event {
	public final EntityPlayer player;
	public final ResourceLocation id;
	public DialogEvent(EntityPlayer player, ResourceLocation id) {
		this.id = id;
		this.player = player;
	}
	/**
	 * 对话框打开时触发的事件
	 * @author small_jiu
	 */
	public static class Open extends DialogEvent {
		public Open(EntityPlayer player, ResourceLocation id) {
			super(player, id);
		}
	}
	/**
	 * 对话框关闭时触发的事件
	 * @author small_jiu
	 */
	public static class Close extends DialogEvent {
		public Close(EntityPlayer player, ResourceLocation id) {
			super(player, id);
		}
	}
}
