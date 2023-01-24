package cat.jiu.dialog.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * 该事件为对话框选项的事件<p>
 * 注意: 事件会在服务端与客户端各触发一次
 * @author small_jiu
 */
public class DialogOptionEvent extends Event {
	public final EntityPlayer player;
	/**
	 * 对话框的ID
	 */
	public final ResourceLocation dialogID;
	/**
	 * 选项的ID
	 */
	public final int optionID;
	public DialogOptionEvent(EntityPlayer player, ResourceLocation dialogID, int optionIndex) {
		this.player = player;
		this.dialogID = dialogID;
		this.optionID = optionIndex;
	}

	/**
	 * 该事件为对话框的 按钮 选项点击后所触发的事件<p>
	 * 注意: 事件会在服务端与客户端各触发一次
	 * @author small_jiu
	 */
	@Cancelable
	public static class ButtonClick extends DialogOptionEvent {
		/**
		 * 点击时使用的鼠标按键ID
		 */
		public final int mouseButton;
		public ButtonClick(EntityPlayer player, ResourceLocation dialogID, int index, int mouseButton) {
			super(player, dialogID, index);
			this.mouseButton = mouseButton;
		}
	}
	
	/**
	 * 该事件为对话框的 文本框 选项点击确定按钮所触发的事件<p>
	 * 注意: 事件会在服务端与客户端各触发一次
	 * @author small_jiu
	 */
	@Cancelable
	public static class TextConfirm extends DialogOptionEvent {
		/**
		 * 文本框的文本
		 */
		protected String text;
		public TextConfirm(EntityPlayer player, ResourceLocation dialogID, int index, String text) {
			super(player, dialogID, index);
			this.text = text;
		}
		public String getText() {
			return text;
		}
		public void setText(String text) {
			this.text = text;
		}
	}
}
